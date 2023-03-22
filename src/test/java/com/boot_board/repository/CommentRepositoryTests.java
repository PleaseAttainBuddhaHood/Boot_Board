package com.boot_board.repository;

import com.boot_board.domain.Board;
import com.boot_board.domain.Comment;
import com.boot_board.dto.BoardListCommentCountDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class CommentRepositoryTests
{
    @Autowired
    private CommentRepository commentRepository;


    @Test
    void testInsert()
    {
        Long bno = 100L;

        Board board = Board.builder().bno(bno).build();

        Comment comment = Comment.builder()
                .board(board)
                .commentText("댓글!")
                .commenter("댓글러1")
                .build();

        commentRepository.save(comment);
    }


    @Transactional
    @Test
    void testBoardComments()
    {
        Long bno = 100L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());

        Page<Comment> result = commentRepository.listOfBoard(bno, pageable);

        result.getContent().forEach(log::info);
    }

}