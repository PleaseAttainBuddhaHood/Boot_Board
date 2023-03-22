package com.boot_board.config;

import com.boot_board.security.CustomUserDetailsService;
import com.boot_board.security.handler.Custom403Handler;
import com.boot_board.security.handler.CustomSocialLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // 사전 or 사후 권한 체크
@Configuration
public class CustomSecurityConfig
{
    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(); // 해시 알고리즘으로 패스워드를 암호화 처리
    }


    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler()
    {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        log.info("--------------configure----------");

        http.formLogin().loginPage("/member/login");
        http.csrf().disable();
        http.rememberMe()
                .key("12345678")
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(60 * 60 * 24 * 30); // 30일

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler()); // 403

        http.oauth2Login()
                .loginPage("/member/login")
                .successHandler(authenticationSuccessHandler());

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler()
    {
        return new Custom403Handler();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository()
    {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();

        repo.setDataSource(dataSource);

        return repo;
    }


    // 정적 파일들(css 등)에 대한 불필요한 시큐리티 적용 해제
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()
    {
        log.info("------ web Configure------");

        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }



}
