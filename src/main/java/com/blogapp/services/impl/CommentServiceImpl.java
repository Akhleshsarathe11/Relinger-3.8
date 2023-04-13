package com.blogapp.services.impl;

import com.blogapp.entities.Comment;
import com.blogapp.entities.Post;
import com.blogapp.exceptions.BlogAPIException;
import com.blogapp.exceptions.ResourceNotFoundException;
import com.blogapp.payloads.CommentDto;
import com.blogapp.repositories.CommentRepository;
import com.blogapp.repositories.PostRepository;
import com.blogapp.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto createOneComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // set post to comment entity
        comment.setPost(post);

        // comment entity to DB
        Comment newComment =  commentRepo.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getAllCommentByPost(long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);

        List<CommentDto> commentDtos = comments.stream().map(s -> mapToDTO(s)).collect(Collectors.toList());
        return commentDtos;
    }

    @Override
    public CommentDto updateOneComment(long postId, long id, CommentDto commentDto) {

        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId)
        );

        Comment comment = commentRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", id)
        );

        comment.setEmail(commentDto.getEmail());
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        Comment save = commentRepo.save(comment);
        return mapToDTO(save);
    }

    @Override
    public void deleteOneComment(long postId, long id) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId)
        );

        Comment comment = commentRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", id)
        );
        
        commentRepo.deleteById(id);
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        // retrieve post entity by id
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepo.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return mapToDTO(comment);
    }


    public CommentDto mapToDTO(Comment comment){

        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return  commentDto;
    }

    public Comment mapToEntity(CommentDto commentDto){

        Comment comment = modelMapper.map(commentDto, Comment.class);
//        Comment comment = new Comment();
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return  comment;
    }

}
