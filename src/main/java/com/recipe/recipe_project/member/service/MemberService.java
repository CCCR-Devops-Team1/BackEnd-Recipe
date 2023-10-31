package com.recipe.recipe_project.member.service;

import com.recipe.recipe_project.member.dto.LoginDto;
import com.recipe.recipe_project.member.dto.TokenDto;
import com.recipe.recipe_project.global.exception.BaseException;
import com.recipe.recipe_project.global.jwt.JwtTokenProvider;
import com.recipe.recipe_project.global.security.CustomAuthenticationProvider;
import com.recipe.recipe_project.member.entity.Member;
import com.recipe.recipe_project.member.repository.MemberRepository;
import com.recipe.recipe_project.notice.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.recipe.recipe_project.global.dto.response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;
  private final ArticleRepository articleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final CustomAuthenticationProvider customAuthenticationProvider;
  public void signUp(String account, String pw) {
    if(memberRepository.existsByAccount(account)){
      throw new BaseException(SIGNUP_DUPLI_MEMBER);
    }
    Member member = Member.builder()
        .account(account)
        .pw(passwordEncoder.encode(pw))
        .build();
    memberRepository.save(member);
  }

  public TokenDto login(LoginDto loginDto) {
    String access_token = "";
    String refresh_token = "";
    try{
      Authentication authentication = customAuthenticationProvider.authenticate(
          new UsernamePasswordAuthenticationToken(loginDto.getAccount(), loginDto.getPw()));
      SecurityContext sc = SecurityContextHolder.createEmptyContext();
      sc.setAuthentication(authentication);
      SecurityContextHolder.setContext(sc);

      access_token = jwtTokenProvider.createAccessToken(loginDto.getAccount());
      refresh_token = jwtTokenProvider.createRefreshToken(loginDto.getAccount());
    } catch(UsernameNotFoundException e){
      throw new BaseException(NOT_FOUND_MEMBER);
    } catch(Exception e){
      e.printStackTrace();
      if(e.getMessage().equals("비밀번호가 틀렸습니다.")){
        throw new BaseException(POST_PASSWORD_INCORRECT);
      }
      throw new BaseException(LOGIN_FAIL);
    }

    return new TokenDto(access_token,refresh_token);
  }

  public void updatePw(Member memberDto, String account) {
    Member member = memberRepository.findByAccount(account).get();
//    Authentication authentication = customAuthenticationProvider.authenticate(
//            new UsernamePasswordAuthenticationToken(account, memberDto.getPw()));
//    SecurityContext sc = SecurityContextHolder.createEmptyContext();
//    sc.setAuthentication(authentication);
//    SecurityContextHolder.setContext(sc);
    System.out.println(memberDto.getPw());
    member.setPw(passwordEncoder.encode(memberDto.getPw()));
    memberRepository.save(member);
  }
  @Transactional
  public void delete(String name) {
    Member member = memberRepository.findByAccount(name).get();
    articleRepository.deleteAllByMemberId(member.getId());
    memberRepository.delete(member);
  }

  public Member getUser(String name) {
    return memberRepository.findByAccount(name).get();
  }
}
