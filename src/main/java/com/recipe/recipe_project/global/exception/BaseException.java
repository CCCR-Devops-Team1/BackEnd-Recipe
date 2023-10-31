package com.recipe.recipe_project.global.exception;

import com.recipe.recipe_project.global.dto.response.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException{
  private ResponseStatus status;

  public BaseException(ResponseStatus status){
    super(status.getMsg());
    this.status = status;
  }

}
