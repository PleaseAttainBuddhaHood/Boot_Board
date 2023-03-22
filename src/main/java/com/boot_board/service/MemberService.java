package com.boot_board.service;


import com.boot_board.security.dto.MemberJoinDTO;

public interface MemberService
{
    static class MidExistException extends Exception
    {

    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;
}
