package com.sparta.blog.service;

import com.sparta.blog.dto.BoardRequestDto;
import com.sparta.blog.dto.BoardResponseDto;
import com.sparta.blog.entity.Board;
import com.sparta.blog.entity.Like;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BoardRepository;
import com.sparta.blog.repository.LikeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service

public class BoardService {

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;


    public BoardService(BoardRepository boardRepository, LikeRepository likeRepository) {
        this.boardRepository = boardRepository;
        this.likeRepository = likeRepository;
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

    public BoardResponseDto getBoardById(Long id) {
        Board board = boardRepository.findBoardById(id).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다"));
        return new BoardResponseDto(board);
    }

    //수정
    @Transactional
    public ResponseEntity<String> updateBoard(Long id, BoardRequestDto boardRequestDto, User user) {
        Board board = findBoard(id);

        // ADMIN
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            board.update(boardRequestDto, user);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : [ADMIN] 게시글 수정 성공");
        }

        // 일반
        if (!board.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value()  + " 메세지 : 본인 게시글이 아닙니다.");
        }
        board.update(boardRequestDto, user);
        return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 게시글 수정 성공");
    }
    // 삭제
    public ResponseEntity<String> deleteBoard(Long id, User user) {
        Board board = findBoard(id);

        // ADMIN
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            boardRepository.delete(board);
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 게시글 삭제 성공");
        }

        // 일반
        if(!board.getUser().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(400).body("상태코드 : " + HttpStatus.BAD_REQUEST.value() + " 메세지 : 본인 게시글이 아닙니다.");
        }
        boardRepository.delete(board);
        return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 게시글 삭제 성공");
    }

    //검색
    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 없습니다."));
    }

    public ResponseEntity<String> likeBoard(Long id, User user) {
        Board board = findBoard(id);

        Optional<Like> like = likeRepository.findByUserIdAndBoardId(user.getId(), id);
        if (like.isEmpty()) {
            likeRepository.save(new Like(user, board));
            return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 게시글 좋아요 추가 성공");
        }
        likeRepository.delete(like.get());
        return ResponseEntity.status(200).body("상태코드 : " + HttpStatus.OK.value() + " 메세지 : 게시글 좋아요 삭제성공");
    }

}
