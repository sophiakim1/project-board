package com.koreait.projectboard.service;

import com.koreait.projectboard.domain.type.SearchType;
import com.koreait.projectboard.dto.ArticleDto;
import com.koreait.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @InjectMocks private ArticleService sut;
    @Mock private ArticleRepository articleRepository;

    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환")
    @Test
    void list(){
//        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword");
//        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환")
    @Test
    void read(){
//        ArticleDto articles = sut.searchArticle(1L);
//        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성")
    @Test
    void create(){
//        sut.saveArticle(ArticleDto.of("title", "content", "#java", "orange", LocalDateTime.now()));
    }

    @DisplayName("게시글의 id와 수정 정보를 입력하면, 게시글을 수정")
    @Test
    void update(){
        // sut.updateArticle(1L, ArticleUpdateDto.of("title2", "content2", "#spring"));
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제")
    @Test
    void delete(){
//        sut.deleteArticle(1L);
    }

}