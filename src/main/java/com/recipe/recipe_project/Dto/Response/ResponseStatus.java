package com.recipe.recipe_project.Dto.Response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseStatus {
  SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),
  SIGNUP_SUCCESS(true, HttpStatus.OK.value(),"회원가입에 성공하였습니다"),
  SIGNUP_PASSWORD_DIFF(false, HttpStatus.BAD_REQUEST.value(), "비밀번호 확인 값이 다릅니다. 다시 입력해주세요."),
  SIGNUP_PW_INVALID(false, HttpStatus.BAD_REQUEST.value(), "비밀번호 형식이 올바르지 않습니다."),
  UNEXPECTED_ERROR(false, HttpStatus.BAD_REQUEST.value(), "예상치 못한 에러가 발생했습니다."),
  POST_PASSWORD_INCORRECT(false, HttpStatus.BAD_REQUEST.value(), "비밀번호가 틀렸습니다."),
  LOGIN_FAIL(false, HttpStatus.BAD_REQUEST.value(), "로그인에 실패했습니다."),
  INVALID_JWT(false, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 JWT입니다."),
  FAIL_AUTH_JWT(false, HttpStatus.BAD_REQUEST.value(), "JWT인증 실패입니다."),
  NOT_FOUND_MEMBER(false,HttpStatus.BAD_REQUEST.value(), "가입되지 않은 email입니다."),
  SIGNUP_DUPLI_MEMBER(false,HttpStatus.BAD_REQUEST.value(), "이미 사용중인 Email입니다."),
  SIGNUP_ACCOUNT_INVALID(false, HttpStatus.BAD_REQUEST.value(), "계정ID는 8~20자 내로 입력해주세요."),
  EXPIRED_JWT(false, HttpStatus.FORBIDDEN.value(),"만료된 Access 토큰입니다. Refresh 토큰을 이용해서 새로운 Access토큰을 발급받으세요."),
  REDIS_ERROR(false, HttpStatus.BAD_REQUEST.value(), "redis 연결에 실패했습니다.");

  private final boolean isSuccess;
  private final int code;
  private final String msg;


  private ResponseStatus(boolean isSuccess, int code, String msg){
    this.isSuccess = isSuccess;
    this.code = code;
    this.msg = msg;
  }
}
