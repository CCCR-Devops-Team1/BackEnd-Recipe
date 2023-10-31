package com.recipe.recipe_project.global.security;

import com.recipe.recipe_project.member.entity.Member;
import com.recipe.recipe_project.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
    Member member = memberRepository.findByAccount(account)
        .orElseThrow(() -> {
          throw new UsernameNotFoundException("사용자 찾을 수 없음");
        });
    return new User(member.getAccount(), member.getPw(), member.getGrantedAuthorities());
  }
}
