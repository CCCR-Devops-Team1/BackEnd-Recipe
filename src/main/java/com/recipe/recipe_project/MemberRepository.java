package com.recipe.recipe_project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  boolean existsByAccount(String account);

  Optional<Member> findByAccount(String account);
}
