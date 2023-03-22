package com.boot_board.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BoardListAllDTO
{
    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;
    private Long commentCount;
    private List<BoardImageDTO> boardImages;
}
