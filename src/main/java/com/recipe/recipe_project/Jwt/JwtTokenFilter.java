package com.recipe.recipe_project.Jwt;

import com.recipe.recipe_project.Exception.BaseException;
import com.recipe.recipe_project.Member;
import com.recipe.recipe_project.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.recipe.recipe_project.Dto.Response.ResponseStatus.FAIL_AUTH_JWT;
import static com.recipe.recipe_project.Dto.Response.ResponseStatus.INVALID_JWT;

public class JwtTokenFilter extends OncePerRequestFilter {
  private JwtTokenProvider jwtTokenProvider;
  private MemberRepository memberRepository;
  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository){
    this.jwtTokenProvider = jwtTokenProvider;
    this.memberRepository = memberRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    System.out.println("JwtTokenFilter 실행");
    String token = request.getHeader("Authorization");
    try{
      // String.utils()를 통해서 유효성 검사 가능
      if(token == null || !token.startsWith("Bearer ")){
        throw new BaseException(INVALID_JWT);
      }
      token = token.replace("Bearer ","");

      Authentication authentication = jwtTokenProvider.getAuthentication(token);
      String account = authentication.getName();
      System.out.println(account);
      if(account != null){
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }catch (BaseException e){
      throw new BaseException(INVALID_JWT);
    } catch(Exception e){
      e.printStackTrace();
      throw new BaseException(FAIL_AUTH_JWT);
    }
    filterChain.doFilter(request,response);
  }
}
