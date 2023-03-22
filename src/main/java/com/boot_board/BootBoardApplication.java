package com.boot_board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BootBoardApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(BootBoardApplication.class, args);
    }

}
