package com.recipe.recipe_project.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
  @NotBlank(message= "member_id는 필수입니다.")
  private long member_id;
}
