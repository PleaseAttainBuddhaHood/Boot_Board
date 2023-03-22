package com.boot_board.service;


import com.boot_board.dto.CommentDTO;
import com.boot_board.dto.PageRequestDTO;
import com.boot_board.dto.PageResponseDTO;

public interface CommentService
{
    Long register(CommentDTO commentDTO);

    CommentDTO read(Long rno);

    void modify(CommentDTO commentDTO);

    void remove(Long rno);

    PageResponseDTO<CommentDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
