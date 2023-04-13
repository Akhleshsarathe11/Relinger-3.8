package com.blogapp.services;

import com.blogapp.payloads.PostDto;
import com.blogapp.payloads.PostResponse;

import java.util.List;

public interface PostService {
    PostDto saveOnePost(PostDto postDto);

   PostDto getPostById(long id);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto updateOnePost(PostDto postDto, long id);

    void deletePostById(long id);
}
