package com.blogapp.controller;

import com.blogapp.payloads.CommentDto;
import com.blogapp.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@Valid @PathVariable("postId") long postId,
                                                    @RequestBody CommentDto commentDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        CommentDto oneComment = commentService.createOneComment(postId, commentDto);
        return new ResponseEntity<>(oneComment, HttpStatus.CREATED);
    }
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getAllComments(@PathVariable("postId") long postId){
        return commentService.getAllCommentByPost(postId);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@Valid @PathVariable("postId") long postId, @PathVariable("id") long id,
                                                    @RequestBody CommentDto commentDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        CommentDto dto = commentService.updateOneComment(postId, id, commentDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") Long postId,
                                                     @PathVariable(value = "id") Long commentId){
        CommentDto commentDto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("postId") long postId, @PathVariable("id") long id){
        commentService.deleteOneComment(postId,id);
        return new ResponseEntity<>("Comment Deleted Successfully", HttpStatus.OK);
    }


}
