package com.recipe.recipe_project;

import com.recipe.recipe_project.Dto.Response.ResponseDto;
import com.recipe.recipe_project.Dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
