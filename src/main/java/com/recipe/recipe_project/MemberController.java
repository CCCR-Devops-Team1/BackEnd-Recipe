package com.recipe.recipe_project;

import com.recipe.recipe_project.Dto.LoginDto;
import com.recipe.recipe_project.Dto.Response.ResponseDto;
import com.recipe.recipe_project.Dto.Response.ResponseStatus;
import com.recipe.recipe_project.Dto.SignDto;
import com.recipe.recipe_project.Dto.TokenDto;
import com.recipe.recipe_project.Util.MemberValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
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
    @PostMapping(value="/user/login")
    public ResponseDto login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult){
        List<String> error_list = MemberValidation.getValidationError(bindingResult);

        if(!error_list.isEmpty()){
            return new ResponseDto(error_list);
        }
        TokenDto tokenDto = memberService.login(loginDto);
        // Security Context에 저장된 사용자 확인
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(redisTemplate.opsForValue().get("account"));
        return new ResponseDto(tokenDto);
    }
    @PutMapping("/user")
    public ResponseDto updatePw(Principal principal, Member memberDto){
        memberService.updatePw(memberDto);

        return new ResponseDto(memberRepository.findByAccount(principal.getName()).get());
    }
    @DeleteMapping("/user")
    public ResponseDto delUser(Principal principal){
        memberService.delete(principal.getName());
        return new ResponseDto(ResponseStatus.SUCCESS);

    }
    @GetMapping("/user")
    public ResponseDto getUserId(Principal principal){
        Member member = memberService.getUser(principal.getName());
        return new ResponseDto(member);
    }
}
