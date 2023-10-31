package com.recipe.recipe_project.member.controller;

import com.recipe.recipe_project.global.dto.response.ResponseDto;
import com.recipe.recipe_project.global.dto.response.ResponseStatus;
import com.recipe.recipe_project.global.utils.Validation;
import com.recipe.recipe_project.member.dto.LoginDto;
import com.recipe.recipe_project.member.dto.SignDto;
import com.recipe.recipe_project.member.dto.TokenDto;
import com.recipe.recipe_project.member.entity.Member;
import com.recipe.recipe_project.member.repository.MemberRepository;
import com.recipe.recipe_project.member.service.MemberService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;

    @PostMapping("/user/signup")
    public ResponseDto signup(@Valid @RequestBody SignDto signDto, BindingResult bindingResult) {
        List<String> error_list = Validation.getValidationError(bindingResult);

        if (!error_list.isEmpty()) {
            return new ResponseDto(false, HttpStatus.BAD_REQUEST.value(), error_list);
        } else {
            if (!Validation.isRegexAccount(signDto.getAccount())) {
                return new ResponseDto(ResponseStatus.SIGNUP_ACCOUNT_INVALID);
            }
            if (!Validation.isRegexPw(signDto.getPw())) {
                return new ResponseDto(ResponseStatus.SIGNUP_PW_INVALID);
            }
            if (!signDto.getPw().equals(signDto.getConfirm_pw())) {
                return new ResponseDto(ResponseStatus.SIGNUP_PASSWORD_DIFF);
            }
            memberService.signUp(signDto.getAccount(), signDto.getPw());
            return new ResponseDto(ResponseStatus.SIGNUP_SUCCESS);
        }
    }

    @PostMapping(value = "/user/login")
    public ResponseDto login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {
        List<String> error_list = Validation.getValidationError(bindingResult);

        if (!error_list.isEmpty()) {
            return new ResponseDto(error_list);
        }
        TokenDto tokenDto = memberService.login(loginDto);
        // Security Context에 저장된 사용자 확인
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(
                redisTemplate.opsForValue().get(SecurityContextHolder.getContext().getAuthentication().getName()));
        return new ResponseDto(tokenDto);
    }

    @PostMapping("/user/logout")
    public ResponseDto logout(Principal principal, @RequestBody TokenDto tokenDto) {
        memberService.logout(tokenDto.getRefresh_token());
        System.out.println(
                redisTemplate.opsForValue().get(SecurityContextHolder.getContext().getAuthentication().getName()));
        return new ResponseDto(ResponseStatus.SUCCESS);
    }

    @PutMapping("/user")
    public ResponseDto updatePw(Principal principal, @RequestBody Member memberDto) {
        memberService.updatePw(memberDto, principal.getName());

        return new ResponseDto(memberRepository.findByAccount(principal.getName()).get());
    }

    @DeleteMapping("/user")
    public ResponseDto delUser(Principal principal) {
        memberService.delete(principal.getName());
        return new ResponseDto(ResponseStatus.SUCCESS);

    }

    @GetMapping("/user")
    public ResponseDto getUserId(Principal principal) {
        Member member = memberService.getUser(principal.getName());
        return new ResponseDto(member);
    }
}