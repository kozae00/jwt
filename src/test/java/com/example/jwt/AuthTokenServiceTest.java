package com.example.jwt;

import com.example.jwt.domain.member.member.entity.Member;
import com.example.jwt.domain.member.member.service.AuthTokenService;
import com.example.jwt.domain.member.member.service.MemberService;
import com.example.jwt.standard.Ut;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthTokenServiceTest {

    @Autowired
    private AuthTokenService authTokenServiceService;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("AuthTokenService 생성")
    void init() {
        assertThat(authTokenServiceService).isNotNull();
    }

    @Test
    @DisplayName("jwt 생성")
    void createToken() {
        // 토큰 만료기간 : 1년
        int expireSeconds = 60 * 60 * 24 * 365;

        // 토큰 시크릿 키 -> 도장 찍는 롤
        SecretKey secretKey = Keys.hmacShaKeyFor("abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890".getBytes());

        // 정보
        Claims claims = Jwts.claims()
                .add("name", "Paul")
                .add("age", 23)
                .build();

        String jwtStr = Ut.Jwt.createToken(secretKey, expireSeconds, Map.of("name", "john", "age", 23));
        assertThat(jwtStr).isNotBlank();

        Jwt<?, ?> parseJwt = Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parse(jwtStr);

        System.out.println("jwt = " + jwtStr);
    }

    @Test
    @DisplayName("access token 생성")
    void accessToke() {

        // jwt -> access token jwt
        Member member = memberService.findByUsername("user1").get();
        String accessToken = authTokenServiceService.genAccessToken(member);

        assertThat(accessToken).isNotBlank();
        System.out.println("accessToken = " + accessToken);
    }

}
