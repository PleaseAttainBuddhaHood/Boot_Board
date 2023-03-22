package com.boot_board.service;

import com.boot_board.domain.Board;
import com.boot_board.dto.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class BoardServiceTests
{
    @Autowired
    private BoardService boardService;

    @Test
    void testRegister()
    {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("샘플 제목")
                .content("샘플 내용")
                .writer("유저00")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info("bno : " + bno);
    }

    @Test
    void testModify()
    {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("업데이트101")
                .content("업데이트 내용 101")
                .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID() + "_zzz.jpg"));

        boardService.modify(boardDTO);
    }

    @Test
    void testList()
    {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }


    @Test
    void testRegisterWithImages()
    {
        log.info(boardService.getClass().getName());


        BoardDTO boardDTO = BoardDTO.builder()
                .title("파일 샘플 제목")
                .content("샘플 내용")
                .writer("유저00")
                .build();

        boardDTO.setFileNames(Arrays.asList(
                UUID.randomUUID() + "_aaa.jpg",
                UUID.randomUUID() + "_bbb.jpg",
                UUID.randomUUID() + "_ccc.jpg"));

        Long bno = boardService.register(boardDTO);

        log.info("bno : " + bno);
    }


    @Test
    void testReadAll()
    {
        Long bno = 101L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for(String fileName : boardDTO.getFileNames())
        {
            log.info(fileName);
        }
    }


    @Test
    void testRemoveAll()
    {
        Long bno = 1L;

        boardService.remove(bno);
    }


    @Test
    void testListWithAll()
    {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO ->
        {
            log.info(boardListAllDTO.getBno() + ":" + boardListAllDTO.getTitle());

            if(boardListAllDTO.getBoardImages() != null)
            {
                for(BoardImageDTO boardImage : boardListAllDTO.getBoardImages())
                {
                    log.info(boardImage);
                }
            }

            log.info("------------------");
        });
    }


}