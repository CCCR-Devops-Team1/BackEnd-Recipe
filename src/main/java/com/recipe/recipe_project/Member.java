package com.recipe.recipe_project;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String account;
  private String pw;
  @CreatedDate
  private LocalDateTime createDate;
  @LastModifiedDate
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
