package com.recipe.recipe_project;

import com.recipe.recipe_project.Exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.recipe.recipe_project.Dto.Response.ResponseStatus.SIGNUP_DUPLI_MEMBER;

@Service
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

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
}
