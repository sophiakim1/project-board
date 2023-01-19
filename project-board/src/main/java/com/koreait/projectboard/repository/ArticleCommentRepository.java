package com.koreait.projectboard.repository;

import com.koreait.projectboard.domain.ArticleComment;
import com.koreait.projectboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    List<ArticleComment> findByArticle_Id(Long articleId);
    void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root){ //추상메소드 안에 적는 법 =default
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content, root.createdAt, root.createdBy); // 추가해라
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); //like 검색이 된다.
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); //날짜는 like검색 불가   //범위값주는건 다음에
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
