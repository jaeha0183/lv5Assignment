package com.sparta.blog.dto;

import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String username;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();
    private Integer likeCount;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.username = board.getUser().getUsername();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        for (Comment comment : board.getCommentsList()) {
                commentList.add(new CommentResponseDto(comment));
        }
        this.likeCount = board.getLikeList().size();

    }
}
