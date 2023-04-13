package com.blogapp.payloads;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CommentDto {
    private long id;
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    @Size(min = 3, message = "name should have at least 3 characters")
    private String name;
    @NotEmpty
    private String body;
}
