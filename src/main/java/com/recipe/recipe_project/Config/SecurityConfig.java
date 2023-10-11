package com.recipe.recipe_project.Config;

import com.recipe.recipe_project.Jwt.JwtExceptionFilter;
import com.recipe.recipe_project.Jwt.JwtTokenFilter;
import com.recipe.recipe_project.Jwt.JwtTokenProvider;
import com.recipe.recipe_project.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer(){
    return (web) -> {
      web.ignoring().requestMatchers("/","/user/signup","/user/login");
    };
  }
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    return http
        .csrf(c -> c.disable())
        .formLogin( f -> f.disable())
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic( h -> h.disable())
        .addFilterBefore(new JwtTokenFilter(jwtTokenProvider, memberRepository), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtExceptionFilter(), JwtTokenFilter.class)
        .build();
  }
  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }
}
