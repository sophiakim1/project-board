package com.koreait.projectboard.repository;

import com.koreait.projectboard.domain.Article;
import com.koreait.projectboard.domain.QArticle;
import com.koreait.projectboard.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        ArticleRepositoryCustom,
        QuerydslPredicateExecutor<Article>,// like검색이 아닌 전체단어 일치 검색 //적용하고자하는 엔티티 이름: 완벽히 =로 검색하는건 가능

// 쿼리에 적용하고자 하는 엔티티, 기본기능 중 완벽하게 동일한 것만 구분 가능(like는 아님)
        QuerydslBinderCustomizer<QArticle> { // like검색을 위해 오버라이드 필요함 //수정하는기능을 쓸때 반영하고자하는 엔티티 이름- qclass :
    //엔티티가 아닌 것을로 return 을 받기위해 queryDSL 사용
    //pageable : 한번에 가저올 수를 의미한다.
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    //findByTitleContaining : 정화한 타이틀을 찾는것이 아닌 일부만으로 찾을 수 있다.
    Page<Article> findByContentContaining(String title, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable); //유저아이디로 검색하고
    // UserAccount와 같은 객체를 찾을 경우에는 _ Userid를 이용하여 찾는다.
    //다른객체를 찾을 땐 언더바를 이용
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);

    void deleteByIdAndUserAccount_UserId(Long articleId, String userid);

    @Override //QuerydslBinderCustomizer<QArticle>할때 필수!
    default void customize(QuerydslBindings bindings, QArticle root){ //추상메소드 안에 적는 법 =default
        //default 1.8이상부터 인터페이스에서 추상메소드 구현 가능
        bindings.excludeUnlistedProperties(true);
        //모든거의 where절이 검색할 수 있지만 막아줌!
        //내가 필요한 애들만 추가할거임!
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); //like 검색이 된다.
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); //날짜는 like검색 불가   //범위값주는건 다음에
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
