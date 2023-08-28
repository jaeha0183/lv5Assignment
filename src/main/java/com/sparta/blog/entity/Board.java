package com.sparta.blog.entity;

import com.sparta.blog.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "blog")
@NoArgsConstructor
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;
    @Column(name = "title")
    private String title;
    @Column(name = "password", nullable = false)
    private String password;

    public Board(BoardRequestDto boardrequestDto) {
        this.username = boardrequestDto.getUsername();
        this.contents = boardrequestDto.getContents();
        this.title = boardrequestDto.getTitle();
        this.password = boardrequestDto.getPassword();
    }

    public void update(BoardRequestDto boardrequestDto) {
        this.username = boardrequestDto.getUsername();
        this.contents = boardrequestDto.getContents();
        this.title = boardrequestDto.getTitle();
        this.password = boardrequestDto.getPassword();
    }
}
