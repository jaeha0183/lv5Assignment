package com.sparta.blog.repository;

import com.sparta.blog.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByModifiedAtDesc();
    List<Board> findAllByContentsContainsOrderByModifiedAtDesc(String keyword);
    Optional<Board> findBoardById(Long id);

}
