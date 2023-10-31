package com.recipe.recipe_project.global.utils;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
  public static boolean isRegexAccount(String target) {
    String regex = "^[0-9a-zA-Z]{8,20}$";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(target);
    return matcher.find();
  }
  public static boolean isRegexPw(String target) {
    // 최소 8자리에 숫자, 문자, 특수 문자 각각 1개 이상 포함
    String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(target);
    return matcher.find();
  }
  public static List<String> getValidationError(BindingResult bindingResult){
    List<String> error_list = new ArrayList<>();

    if(bindingResult.hasErrors()){
      bindingResult.getFieldErrors().forEach(error -> {
        error_list.add(error.getDefaultMessage());
      });
    }
    return error_list;
  }
}
