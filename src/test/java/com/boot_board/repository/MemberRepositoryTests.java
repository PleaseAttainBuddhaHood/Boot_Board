package com.boot_board.repository;

import com.boot_board.domain.Member;
import com.boot_board.domain.MemberRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@SpringBootTest
class MemberRepositoryTests
{
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void insertMembers()
    {
        IntStream.rangeClosed(1, 100).forEach(i ->
        {
            Member member = Member.builder()
                    .mid("member" + i)
                    .mpw(passwordEncoder.encode("1111"))
                    .email("email" + i + "@aaa.bbb")
                    .build();

            member.addRole(MemberRole.USER);

            if(i >= 90)
            {
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        });
    }


    @Test
    void testRead()
    {
        Optional<Member> result = memberRepository.getWithRoles("member100");

        Member member = result.orElseThrow();

        log.info(member);
        log.info(member.getRoleSet());

        member.getRoleSet().forEach(memberRole -> log.info(memberRole.name()));
    }


    @Test
    void testUpdate()
    {
        String mid = "oksk9633@gmail.com";
        String mpw = passwordEncoder.encode("1234");

        memberRepository.updatePassword(mpw, mid);
    }
}