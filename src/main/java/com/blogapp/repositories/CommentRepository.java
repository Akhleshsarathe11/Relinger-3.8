package com.blogapp.repositories;

import com.blogapp.entities.Comment;
import com.blogapp.payloads.CommentDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(long postId);
}
