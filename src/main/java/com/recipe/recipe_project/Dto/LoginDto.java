package com.recipe.recipe_project.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
  @NotBlank(message= "account는 필수 입력 사항입니다.")
  private String account;

  @NotBlank(message= "비밀번호는 필수 입력 사항입니다.")
  private String pw;
}