package com.recipe.recipe_project.member.controller;

import com.recipe.recipe_project.global.dto.response.ResponseDto;
import com.recipe.recipe_project.member.dto.TokenDto;
import com.recipe.recipe_project.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/auth/reissue")
    public ResponseDto<TokenDto> reissue(@RequestBody TokenDto tokenDto){
        System.out.println(tokenDto.getRefresh_token());
        return new ResponseDto<>(authService.reissueToken(tokenDto.getRefresh_token()));
    }
}
