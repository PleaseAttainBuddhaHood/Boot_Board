package com.boot_board.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Board extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 키 생성 전략. Indentity => DB에 위임
    private Long bno;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    // CascadeType.ALL : Board 엔터티 객체의 모든 상태 변화에 BoardImage 객체들 같이 변경
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "board",
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY,
               orphanRemoval = true)
    @Builder.Default
    private Set<BoardImage> imageSet = new HashSet<>();


    public void addImage(String uuid, String fileName)
    {
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();

        imageSet.add(boardImage);
    }


    public void clearImages()
    {
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));

        this.imageSet.clear();
    }


    public void change(String title, String content)
    {
        this.title = title;
        this.content = content;
    }

}
