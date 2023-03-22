package com.boot_board.repository;

import com.boot_board.domain.Board;
import com.boot_board.domain.BoardImage;
import com.boot_board.dto.BoardListAllDTO;
import com.boot_board.dto.BoardListCommentCountDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
class BoardRepositoryTests
{
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testInsert()
    {
        IntStream.rangeClosed(1, 100).forEach(i ->
        {
            Board board = Board.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .writer("user" + (i % 10))
                    .build();

            Board result = boardRepository.save(board);

            log.info("Bno : " + result.getBno());
        });
    }

    @Test
    void testSelect()
    {
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        log.info(board);
    }

    @Test
    void testUpdate()
    {
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        board.change("update..title 100", "update content 100");

        boardRepository.save(board);
    }

    @Test
    void testDelete()
    {
        Long bno = 99L;

        boardRepository.deleteById(bno);
    }

    @Test
    void testPaging()
    {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count : " + result.getTotalElements());
        log.info("total pages : " + result.getTotalPages());
        log.info("page number : " + result.getNumber());
        log.info("page size : " + result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(log::info);
    }



    @Test
    void testSearch1()
    {
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }

    @Test
    void testSearchAll()
    {
        String[] types = {"t", "c", "w"};
        String keyword = "1";

        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious() + " : " + result.hasNext());

        result.getContent().forEach(log::info);
    }


    @Test
    void testSearchCommentCount()
    {
        String[] types = {"t", "c", "w"};
        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<BoardListCommentCountDTO> result = boardRepository.searchWithCommentCount(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious() + " : " + result.hasNext());

        result.getContent().forEach(log::info);
    }


    @Test
    void testInsertWithImages()
    {
        Board board = Board.builder()
                .title("이미지 테스트")
                .content("첨부파일 테스트")
                .writer("테스터")
                .build();

        for (int i = 0; i < 3; i++)
        {
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    void testReadWithImages()
    {
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        log.info(board);
        log.info("-----------------------");

        for(BoardImage boardImage : board.getImageSet())
        {
            log.info(boardImage);
        }
    }


    @Transactional
    @Commit
    @Test
    void testModifyImages()
    {
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        board.clearImages();

        for (int i = 0; i < 2; i++)
        {
            board.addImage(UUID.randomUUID().toString(), "updatefile" + i + ".jpg");
        }

        boardRepository.save(board);
    }


    @Transactional
    @Commit
    @Test
    void testRemoveAll()
    {
        Long bno = 1L;

        commentRepository.deleteByBoard_Bno(bno);
        boardRepository.deleteById(bno);
    }


    @Test
    void testInsertAll()
    {
        for(int i = 1; i <= 100; i++)
        {
            Board board = Board.builder()
                    .title("제목" + i)
                    .content("내용" + i)
                    .writer("작성자" + i)
                    .build();

            for (int j = 0; j < 3; j++)
            {
                if(i % 5 == 0) continue;

                board.addImage(UUID.randomUUID().toString(), i + "file" + j + ".jpg");
            }

            boardRepository.save(board);
        }
    }


    @Transactional
    @Test
    void testSearchImageCommentCount()
    {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);

        log.info("------------------");
        log.info(result.getTotalElements());

        result.getContent().forEach(log::info);
    }
}
