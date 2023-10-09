package com.recipe.recipe_project;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Value("${jwt.secretKey}")
    String token ;
    @GetMapping("/")
    public void main(){
        System.out.println(token);
    }

}
