package com.recipe.recipe_project;

import com.recipe.recipe_project.Dto.LoginDto;
import com.recipe.recipe_project.Exception.BaseException;
import com.recipe.recipe_project.Jwt.JwtTokenProvider;
import com.recipe.recipe_project.Security.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.recipe.recipe_project.Dto.Response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;
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

  public String login(LoginDto loginDto) {
    String token = "";
    try{
      Authentication authentication = customAuthenticationProvider.authenticate(
          new UsernamePasswordAuthenticationToken(loginDto.getAccount(), loginDto.getPw()));
      SecurityContext sc = SecurityContextHolder.createEmptyContext();
      sc.setAuthentication(authentication);
      SecurityContextHolder.setContext(sc);

      token = jwtTokenProvider.createToken(loginDto.getAccount(), loginDto.getPw());
    }catch(UsernameNotFoundException e){
      throw new BaseException(NOT_FOUND_MEMBER);
    } catch(Exception e){
      e.printStackTrace();
      if(e.getMessage().equals("비밀번호가 틀렸습니다.")){
        throw new BaseException(POST_PASSWORD_INCORRECT);
      }
      throw new BaseException(LOGIN_FAIL);
    }

    return token;
  }
}
