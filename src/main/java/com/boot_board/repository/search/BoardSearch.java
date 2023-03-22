package com.boot_board.repository.search;


import com.boot_board.domain.Board;
import com.boot_board.dto.BoardListAllDTO;
import com.boot_board.dto.BoardListCommentCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch
{
    Page<Board> search1(Pageable pageable);
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListCommentCountDTO> searchWithCommentCount(String[] types, String keyword, Pageable pageable);
    Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);
}
