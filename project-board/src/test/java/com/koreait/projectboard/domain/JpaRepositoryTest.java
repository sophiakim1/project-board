package com.koreait.projectboard.domain;

import com.koreait.projectboard.config.JpaConfig;
import com.koreait.projectboard.repository.ArticleCommentRepository;
import com.koreait.projectboard.repository.ArticleRepository;
import com.koreait.projectboard.repository.UserAccountRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.criterion.Projections.count;

//@Disabled("JPA 테스트는 불필요하므로 제외시킴")
@Import(JpaConfig.class)
@DisplayName("JPA 연결테스트")
@DataJpaTest
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository,
            @Autowired UserAccountRepository userAccountRepository
    ){
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void select(){
        List<Article> articles = articleRepository.findAll();
        assertThat(articles)
                .isNotNull().hasSize(1000);
    }

    @DisplayName("insert 테스트")
    @Test
    void insert(){
        long prevCount = articleRepository.count(); // articleRepository에서 카운트 하게되면
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("banana", "2222", "banana@banana.com", "반하나", ""));
        Article article = Article.of(userAccount, "새로운 글", "새로운 글을 입력합니다!", "#spring"); //새로운 article 객채 생성
        articleRepository.save(article);
        assertThat(articleRepository.count()).isEqualTo(prevCount + 1); // 이전 갯수보다 하나가 늘었으면 통과 아니면 에러
    }

    @DisplayName("update 테스트")
    @Test
    void update(){
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateHashTag = "#Spring";
        article.setHashtag(updateHashTag);

        Article savedArticle = articleRepository.saveAndFlush(article); //롤백때문에 확인하기 힘드니 flush를 이용해 저장
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updateHashTag);
    }

    @DisplayName("delete 테스트")
    @Test
    void delete(){
        Article article = articleRepository.findById(1L).orElseThrow();
        long preArticleCount = articleRepository.count();
        long preArticleCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();

        articleRepository.delete(article);
        assertThat(articleRepository.count()).isEqualTo(preArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(preArticleCommentCount - deletedCommentsSize);
    }
}