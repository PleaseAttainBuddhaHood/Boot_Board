package com.boot_board.controller;

import com.boot_board.security.dto.MemberJoinDTO;
import com.boot_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController
{
    private final MemberService memberService;


    @GetMapping("/login")
    public void loginGET(String error, String logout)
    {
        log.info("로그인 GET..");
        log.info("로그아웃 : " + logout);

        if(logout != null)
        {
            log.info("사용자 로그아웃..");
        }
    }


    @GetMapping("/join")
    public void joinGET()
    {
        log.info("회원가입 GET()..");
    }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes)
    {
        log.info("회원가입 POST()..");
        log.info(memberJoinDTO);

        try
        {
            memberService.join(memberJoinDTO);
        }
        catch (MemberService.MidExistException e)
        {
            redirectAttributes.addFlashAttribute("error", "mid");

            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/member/login"; // 회원가입 후 로그인 페이지
    }



}
