package com.boot_board.service;

import com.boot_board.dto.CommentDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
class CommentServiceTests
{
    @Autowired
    private CommentService commentService;


    @Test
    void testRegister()
    {
        CommentDTO commentDTO = CommentDTO.builder()
                .commentText("댓글DTO Text")
                .commenter("댓글러00")
                .bno(104L)
                .build();

        log.info(commentService.register(commentDTO));
    }
}