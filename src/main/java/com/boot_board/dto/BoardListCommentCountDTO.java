package com.boot_board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListCommentCountDTO
{
    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;
    private Long commentCount;
}
