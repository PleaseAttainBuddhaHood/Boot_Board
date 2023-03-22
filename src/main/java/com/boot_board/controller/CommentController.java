package com.boot_board.controller;

import com.boot_board.dto.CommentDTO;
import com.boot_board.dto.PageRequestDTO;
import com.boot_board.dto.PageResponseDTO;
import com.boot_board.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController
{
    private final CommentService commentService;


    @ApiOperation(value = "Comments POST", notes = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult)
            throws BindException
    {
        log.info(commentDTO);

        if(bindingResult.hasErrors())
        {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = commentService.register(commentDTO);

        resultMap.put("rno", rno);

        return resultMap;
    }


    @ApiOperation(value = "Comment of Board", notes = "GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<CommentDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO)
    {
        return commentService.getListOfBoard(bno, pageRequestDTO);
    }


    // 특정 댓글 조회
    @ApiOperation(value = "Read Comment", notes = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public CommentDTO getCommentDTO(@PathVariable("rno") Long rno)
    {
        return commentService.read(rno);
    }

    // 삭제
    @ApiOperation(value = "Delete Comment", notes = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno)
    {
        commentService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }


    // 수정
    @ApiOperation(value = "Modify Comment", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(@PathVariable("rno") Long rno, @RequestBody CommentDTO commentDTO)
    {
        commentDTO.setRno(rno);

        commentService.modify(commentDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }


}
