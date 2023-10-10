package com.recipe.recipe_project.Dto.Response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.recipe.recipe_project.Dto.Response.ResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "result"})
public class ResponseDto<T> {
  private final Boolean isSuccess;
  private final int code;
  private T result;

  public ResponseDto(T result){
    this.isSuccess = SUCCESS.isSuccess();
    this.code = SUCCESS.getCode();
    this.result = result;
  }
  // 요청 실패 응답객체
  public ResponseDto(boolean isSuccess, int code, T result){
    this.isSuccess = isSuccess;
    this.code = code;
    this.result = result;
  }
  // ResponseStatus 이용한 응답객체
  public ResponseDto(ResponseStatus responseStatus){
    this.isSuccess = responseStatus.isSuccess();
    this.code = responseStatus.getCode();
    this.result = (T) responseStatus.getMsg();
  }
}
