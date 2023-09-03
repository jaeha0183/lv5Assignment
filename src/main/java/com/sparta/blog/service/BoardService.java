package com.sparta.blog.service;

import com.sparta.blog.dto.BoardRequestDto;
import com.sparta.blog.dto.BoardResponseDto;
import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.security.UserDetailsImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    //조회
    public List<BoardResponseDto> getBoard() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }
    //생성
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, User user) {
        Board board = new Board(boardRequestDto, user);
        Board saveBoard = boardRepository.save(board);
        BoardResponseDto boardResponseDto = new BoardResponseDto(saveBoard);

        return boardResponseDto;
    }
    //키워드 조회
//    public List<BoardResponseDto> getBoardByKeyword(String keyword) {
//        return boardRepository.findAllByContentsContainsOrderByModifiedAtDesc(keyword).stream().map(BoardResponseDto::new).toList();
//    }
    public BoardResponseDto getBoardById(Long id) {
        Board board = boardRepository.findBoardById(id).orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다"));
        return new BoardResponseDto(board);
    }

    //수정
    @Transactional
    public void updateBoard(Long id, BoardRequestDto boardRequestDto, User user) throws IllegalArgumentException {
        Board board = findBoard(id);
        if (board.getUser().getUsername().equals(user.getUsername())) {
            board.update(boardRequestDto, user);
        } else {
            throw new IllegalArgumentException("본인 게시물이 아닙니다.");
        }
    }
    //삭제
    public BoardResponseDto deleteBoard(Long id, BoardRequestDto boardRequestDto, User user) throws IllegalArgumentException {
//        String password = boardRequestDto.getPassword();
        Board board = findBoard(id);

        if(board.getUser().getUsername().equals(user.getUsername())) {
            boardRepository.delete(board);
            return new BoardResponseDto(board);
        } else {
            throw new IllegalArgumentException("본인 게시물이 아닙니다.");
        }

    }
    //검색
    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 없습니다."));
    }

//    private Board passwordCheck(Board board, String password) {
//        if(board.getPassword().equals(password)) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        } else {
//            return board;
//        }
//    }
}
