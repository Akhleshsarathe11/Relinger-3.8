package com.blogapp.services;

import com.blogapp.payloads.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createOneComment(long postId, CommentDto commentDto);

    List<CommentDto> getAllCommentByPost(long postId);

    CommentDto updateOneComment(long postId, long id, CommentDto commentDto);

    void deleteOneComment(long postId, long id);

    CommentDto getCommentById(Long postId, Long commentId);
}
