package com.koreait.projectboard.dto;

import com.koreait.projectboard.domain.Article;
import com.koreait.projectboard.domain.ArticleComment;
import com.koreait.projectboard.domain.UserAccount;

import java.time.LocalDateTime;

public record ArticleCommentDto(
        Long id,
        Long articleId,
        UserAccountDto userAccountDto,
        String content,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {
    public static ArticleCommentDto of(Long id, Long articleId, UserAccountDto userAccountDto, String content, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt){
        return new ArticleCommentDto(id, articleId, userAccountDto, content, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static ArticleCommentDto of(Long articleId, UserAccountDto userAccountDto, String content) {
        return new ArticleCommentDto(null, articleId, userAccountDto, content, null, null, null, null);
    }

    public static ArticleCommentDto from(ArticleComment entity){
        return new ArticleCommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getModifiedBy(),
                entity.getModifiedAt()
        );
    }

    public ArticleComment toEntity(Article article, UserAccount userAccount){
        return ArticleComment.of(
                article,
                userAccountDto.toEntity(),
                content
        );
    }
}
