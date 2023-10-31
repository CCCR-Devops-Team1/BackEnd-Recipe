package com.recipe.recipe_project.member.service;

import com.recipe.recipe_project.member.dto.TokenDto;
import com.recipe.recipe_project.global.exception.BaseException;
import com.recipe.recipe_project.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.recipe.recipe_project.global.dto.response.ResponseStatus.NOT_EXIST_REFRESH_JWT;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenDto reissueToken(String refreshToken) throws BaseException {
        // Refresh Token 검증
        jwtTokenProvider.validateToken(refreshToken);
        // Access Token 에서 User 가져옴
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        // Redis에서 저장된 Refesh Token 값 가져옴
        String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if(!redisRefreshToken.equals(refreshToken)) {
            throw new BaseException(NOT_EXIST_REFRESH_JWT);
        }
        // 토큰 재발행
        TokenDto tokenDto = new TokenDto(
                jwtTokenProvider.createAccessToken(authentication.getName()),
                jwtTokenProvider.createRefreshToken(authentication.getName())
        );
        return tokenDto;
    }
}
