package com.recipe.recipe_project;

import com.recipe.recipe_project.Dto.Response.ResponseDto;
import com.recipe.recipe_project.Dto.Response.ResponseStatus;
import com.recipe.recipe_project.Dto.SignDto;
import com.recipe.recipe_project.Util.MemberValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/user/signup")
    public ResponseDto signup(@Valid @RequestBody SignDto signDto, BindingResult bindingResult){
        List<String> error_list = MemberValidation.getValidationError(bindingResult);

        if(!error_list.isEmpty()){
            return new ResponseDto(false, HttpStatus.BAD_REQUEST.value(), error_list);
        }else{
            if(!MemberValidation.isRegexAccount(signDto.getAccount())){
                return new ResponseDto(ResponseStatus.SIGNUP_ACCOUNT_INVALID);
            }
            if(!MemberValidation.isRegexPw(signDto.getPw())){
                return new ResponseDto(ResponseStatus.SIGNUP_PW_INVALID);
            }
            if(!signDto.getPw().equals(signDto.getConfirm_pw())){
                return new ResponseDto(ResponseStatus.SIGNUP_PASSWORD_DIFF);
            }
            memberService.signUp(signDto.getAccount(), signDto.getPw());
            return new ResponseDto(ResponseStatus.SIGNUP_SUCCESS);
        }
    }

}
