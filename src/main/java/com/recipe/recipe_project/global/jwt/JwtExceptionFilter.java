package com.recipe.recipe_project.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.recipe_project.global.exception.BaseException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    try{
      filterChain.doFilter(request, response);
    }catch(BaseException e){
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), e.getMessage());
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
  }
  @Data
  public static class ErrorResponse{
    private final Boolean isSuccess = false;
    private final Integer code;
    private final String message;
  }
}
