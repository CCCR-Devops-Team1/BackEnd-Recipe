package com.recipe.recipe_project.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignDto {
  @NotBlank(message="account는 필수 입력 사항입니다.")
  private String account;
  @NotBlank(message="pw는 필수 입력 사항입니다.")
  private String pw;
  @NotBlank(message="confirm_pw는 필수 입력 사항입니다.")
  private String confirm_pw;
}
