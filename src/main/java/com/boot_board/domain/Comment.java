package com.boot_board.domain;

import lombok.*;

import javax.persistence.*;

@ToString(exclude = "board")
@Table(name = "Comment", indexes = @Index(name = "idx_comment_board_bno", columnList = "board_bno"))
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 지연 로딩. 필요한 순간까지 DB와 연결하지 않음
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String commentText;
    private String commenter;


    public void changeText(String text)
    {
        this.commentText = text;
    }

}
