package com.recipe.recipe_project.Exception;

import com.recipe.recipe_project.Dto.Response.ResponseDto;
import com.recipe.recipe_project.Dto.Response.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
  @ExceptionHandler(BaseException.class)
  public ResponseDto<ResponseStatus> BaseExceptionHandle(BaseException exception){
    return new ResponseDto<>(exception.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseDto<ResponseStatus> ExceptionHandle(Exception exception){
    exception.printStackTrace();
    return new ResponseDto<>(ResponseStatus.UNEXPECTED_ERROR);
  }
}
