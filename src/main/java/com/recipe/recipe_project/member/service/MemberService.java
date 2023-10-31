package com.recipe.recipe_project.member.service;

import static com.recipe.recipe_project.global.dto.response.ResponseStatus.LOGIN_FAIL;
import static com.recipe.recipe_project.global.dto.response.ResponseStatus.NOT_EXIST_REFRESH_JWT;
import static com.recipe.recipe_project.global.dto.response.ResponseStatus.NOT_FOUND_MEMBER;
import static com.recipe.recipe_project.global.dto.response.ResponseStatus.POST_PASSWORD_INCORRECT;
import static com.recipe.recipe_project.global.dto.response.ResponseStatus.SIGNUP_DUPLI_MEMBER;

import com.recipe.recipe_project.global.exception.BaseException;
import com.recipe.recipe_project.global.jwt.JwtTokenProvider;
import com.recipe.recipe_project.global.security.CustomAuthenticationProvider;
import com.recipe.recipe_project.member.dto.LoginDto;
import com.recipe.recipe_project.member.dto.TokenDto;
import com.recipe.recipe_project.member.entity.Member;
import com.recipe.recipe_project.member.repository.MemberRepository;
import com.recipe.recipe_project.notice.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public void signUp(String account, String pw) {
        if (memberRepository.existsByAccount(account)) {
            throw new BaseException(SIGNUP_DUPLI_MEMBER);
        }
        memberRepository.save(dtoToEntity(account, pw));
    }

    public TokenDto login(LoginDto loginDto) {
        String access_token = "";
        String refresh_token = "";
        try {
            Authentication authentication = customAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getAccount(), loginDto.getPw()));
            SecurityContext sc = SecurityContextHolder.createEmptyContext();
            sc.setAuthentication(authentication);
            SecurityContextHolder.setContext(sc);

            access_token = jwtTokenProvider.createAccessToken(loginDto.getAccount());
            refresh_token = jwtTokenProvider.createRefreshToken(loginDto.getAccount());
        } catch (UsernameNotFoundException e) {
            throw new BaseException(NOT_FOUND_MEMBER);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().equals("비밀번호가 틀렸습니다.")) {
                throw new BaseException(POST_PASSWORD_INCORRECT);
            }
            throw new BaseException(LOGIN_FAIL);
        }

        return new TokenDto(access_token, refresh_token);
    }

    public void logout(String refreshToken) {
        // Refresh Token 검증
        jwtTokenProvider.validateToken(refreshToken);
        // Access Token 에서 User 가져옴
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        // Redis에서 저장된 Refesh Token 값 가져옴
        String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete(authentication.getName());
        }
    }

    @Transactional
    public void updatePw(Member memberDto, String account) {
        Member member = memberRepository.findByAccount(account).get();
        member.setPw(passwordEncoder.encode(memberDto.getPw()));
        memberRepository.save(member);
    }

    @Transactional
    public void delete(String name) {
        Member member = memberRepository.findByAccount(name).get();
        articleRepository.deleteAllByMemberId(member.getId());
        memberRepository.delete(member);
    }

    public TokenDto reissueToken(String refreshToken) throws BaseException {
        // Refresh Token 검증
        jwtTokenProvider.validateToken(refreshToken);
        // Access Token 에서 User 가져옴
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        // Redis에서 저장된 Refesh Token 값 가져옴
        String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if (!redisRefreshToken.equals(refreshToken)) {
            throw new BaseException(NOT_EXIST_REFRESH_JWT);
        }
        // 토큰 재발행
        TokenDto tokenDto = new TokenDto(
                jwtTokenProvider.createAccessToken(authentication.getName()),
                jwtTokenProvider.createRefreshToken(authentication.getName())
        );
        return tokenDto;
    }

    public Member getUser(String name) {
        return memberRepository.findByAccount(name).get();
    }

    private Member dtoToEntity(String account, String pw) {
        return Member.builder()
                .account(account)
                .pw(passwordEncoder.encode(pw))
                .build();
    }
}
