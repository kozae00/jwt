package com.example.jwt.domain.member.member.service;

import com.example.jwt.domain.member.member.entity.Member;
import com.example.jwt.standard.util.Ut;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTokenService {
    public String genAccessToken(Member member) {

        int expireSeconds = 60 * 60 * 24 * 365;

        return Ut.Jwt.createToken(
                Keys.hmacShaKeyFor("abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890".getBytes()),
                expireSeconds,
                Map.of("id", member.getId(), "username", member.getUsername())
        );
    }
}
