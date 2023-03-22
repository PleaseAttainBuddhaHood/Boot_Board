package com.boot_board.security.handler;

import com.boot_board.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler
{

    private final PasswordEncoder passwordEncoder;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException
    {
        log.info("----------------------------");
        log.info("CustomLoginSuccessHandler onAuthenticationSuccess...");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        String encodedPw = memberSecurityDTO.getMpw();

        // 소셜 로그인이고 회원 패스워드가 1111이면
        if(memberSecurityDTO.isSocial() && (memberSecurityDTO.getMpw().equals("1111")
                                        || passwordEncoder.matches("1111", memberSecurityDTO.getMpw())))
        {
            log.info("패스워드 변경 필수");
            log.info("멤버 수정으로 리다이렉트");

            response.sendRedirect("/member/modify");
            return;
        }
        else
        {
            response.sendRedirect("/board/list");
        }
    }


}
