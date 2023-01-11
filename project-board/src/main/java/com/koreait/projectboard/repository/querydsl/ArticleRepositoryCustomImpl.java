package com.koreait.projectboard.repository.querydsl;

import com.koreait.projectboard.domain.Article;
import com.koreait.projectboard.domain.QArticle;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {
    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    // select distinct(hashtag) from article where hashtag is not null;
    @Override
    public List<String> findAllDistinctHashtags(){
        QArticle article = QArticle.article;
        return from(article)
                .distinct()
                .select(article.hashtag)
                .where(article.hashtag.isNotNull())
                .fetch();
    }

}
