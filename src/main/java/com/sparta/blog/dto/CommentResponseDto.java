package com.sparta.blog.dto;

import com.sparta.blog.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    long id;
    String comment;
    String username;
    private Integer likeCount;


    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.username = comment.getUser().getUsername();
        this.likeCount = comment.getLikeList().size();
    }
}
