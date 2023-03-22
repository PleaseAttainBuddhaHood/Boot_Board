package com.boot_board.repository;

import com.boot_board.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
    @Query("select r from Comment r where r.board.bno = :bno")
    Page<Comment> listOfBoard(Long bno, Pageable pageable);

    void deleteByBoard_Bno(Long bno);
}
