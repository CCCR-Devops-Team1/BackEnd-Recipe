package com.recipe.recipe_project;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String account;
  private String pw;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
//  public List<? extends GrantedAuthority> getGrantedAuthorities() {
//    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//    authorities.add(new SimpleGrantedAuthority("member"));
//    if(isAdmin()){
//      authorities.add(new SimpleGrantedAuthority("admin"));
//    }
//    return authorities;
//  }

}
