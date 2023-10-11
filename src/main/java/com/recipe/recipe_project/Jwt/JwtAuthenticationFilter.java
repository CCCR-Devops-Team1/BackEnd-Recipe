package com.recipe.recipe_project.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.recipe_project.Dto.LoginDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    System.out.println("jwtAuthenticationFilter");
    ObjectMapper om = new ObjectMapper();
    LoginDto loginDto = null;
    String token = request.getHeader("Authorization");
    try{
      loginDto = om.readValue(request.getInputStream(), LoginDto.class);
    } catch (Exception e) {
      e.printStackTrace();
    }

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(loginDto.getAccount(), loginDto.getPw());

    Authentication authentication =
        authenticationManager.authenticate(authenticationToken);
    return authentication;
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
    User userDetails = (User) authentication.getPrincipal();
    String jwtToken = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getPassword());

    response.addHeader("Authorization", "Bearer "+jwtToken);
  }
}