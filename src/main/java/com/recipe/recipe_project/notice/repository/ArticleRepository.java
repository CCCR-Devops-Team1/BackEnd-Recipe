package com.recipe.recipe_project.notice.repository;

import com.recipe.recipe_project.notice.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
  Optional<Article> findByIdAndMemberId(long id, long memberId);

  void deleteByIdAndMemberId(long id, long memberId);

  List<Article> findAllByMemberId(long id);

  void deleteAllByMemberId(long id);
}
