package com.recipe.recipe_project.Security;

import com.recipe.recipe_project.Exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.recipe.recipe_project.Dto.Response.ResponseStatus.POST_PASSWORD_INCORRECT;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
  private final CustomUserDetailsService customUserDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
    String email = token.getName();
    String password = (String) token.getCredentials();

    User user = (User) customUserDetailsService.loadUserByUsername(email);

    if(!(user.getUsername().equals(email) && passwordEncoder.matches(password, user.getPassword()))){
      throw new BaseException(POST_PASSWORD_INCORRECT);
    }
    return new UsernamePasswordAuthenticationToken(user, password);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
