package com.boot_board.repository.search;


import com.boot_board.domain.Board;
import com.boot_board.domain.QBoard;
import com.boot_board.domain.QComment;
import com.boot_board.dto.BoardImageDTO;
import com.boot_board.dto.BoardListAllDTO;
import com.boot_board.dto.BoardListCommentCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class BoardSearchImpl extends QuerydslRepositorySupport
                             implements BoardSearch
{

    public BoardSearchImpl()
    {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable)
    {
        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board);

        query.where(board.title.contains("1"));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable)
    {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if((types != null && types.length > 0) && keyword != null)
        {
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types)
            {
                switch(type)
                {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;

                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;

                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }

            query.where(booleanBuilder);
        }

        query.where(board.bno.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<BoardListCommentCountDTO> searchWithCommentCount(String[] types, String keyword, Pageable pageable)
    {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;

        JPQLQuery<Board> query = from(board);

        query.leftJoin(comment).on(comment.board.eq(board));
        query.groupBy(board);

        if((types != null && types.length > 0) && keyword != null)
        {
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types)
            {
                switch(type)
                {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }

            query.where(booleanBuilder);
        }

        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListCommentCountDTO> dtoQuery =
                query.select(Projections.bean(BoardListCommentCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        comment.count().as("commentCount")));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListCommentCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }


    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable)
    {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(comment).on(comment.board.eq(board));

        if(types != null && types.length > 0)
        {
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types)
            {
                switch (type)
                {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }

            boardJPQLQuery.where(booleanBuilder);
        }

        boardJPQLQuery.groupBy(board);

        getQuerydsl().applyPagination(pageable, boardJPQLQuery);


        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, comment.countDistinct());

        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple ->
        {
            Board board1 = (Board) tuple.get(board);
            long commentCount = tuple.get(1, Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .commentCount(commentCount)
                    .build();

            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                                    .uuid(boardImage.getUuid())
                                    .fileName(boardImage.getFileName())
                                    .ord(boardImage.getOrd())
                                    .build()
                        ).collect(Collectors.toList());

            dto.setBoardImages(imageDTOS);

            return dto;
        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }

}
