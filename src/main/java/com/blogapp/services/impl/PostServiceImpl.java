package com.blogapp.services.impl;

import com.blogapp.entities.Post;
import com.blogapp.exceptions.ResourceNotFoundException;
import com.blogapp.payloads.PostDto;
import com.blogapp.payloads.PostResponse;
import com.blogapp.repositories.PostRepository;
import com.blogapp.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public PostDto saveOnePost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post newPost = postRepo.save(post);
        PostDto dto = mapToDto(newPost);
        return dto;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", id));
    return mapToDto(post);
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> posts = postRepo.findAll(pageable);
        List<Post> content = posts.getContent();
        List<PostDto> postDtos = content.stream().map(s -> mapToDto(s)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }


    @Override
    public PostDto updateOnePost(PostDto postDto, long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", id));

        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        post.setTitle(postDto.getTitle());

        Post updatePost = postRepo.save(post);

       return mapToDto(updatePost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", id)
        );

        postRepo.delete(post);
    }


    public Post mapToEntity(PostDto postDto){
        Post post = modelMapper.map(postDto, Post.class);
//        Post post = new Post();
//        post.setContent(postDto.getContent());
//        post.setDescription(postDto.getDescription());
//        post.setTitle(postDto.getTitle());
        return post;
    }

    public PostDto mapToDto(Post post){
        PostDto dto = modelMapper.map(post, PostDto.class);
//        PostDto dto = new PostDto();
//        dto.setId(post.getId());
//        dto.setContent(post.getContent());
//        dto.setDescription(post.getDescription());
//        dto.setTitle(post.getTitle());
        return dto;
    }


}