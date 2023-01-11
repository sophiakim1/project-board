package com.koreait.projectboard.dto;

import com.koreait.projectboard.domain.Article;
import com.koreait.projectboard.domain.ArticleComment;

import java.time.LocalDateTime;

public record ArticleCommentsDto(
        Long id,
        Long articleId,
        UserAccountDto userAccountDto,
        String content,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {
    public static ArticleCommentsDto of(Long id, Long articleId, UserAccountDto userAccountDto, String content, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new ArticleCommentsDto(id, articleId, userAccountDto, content, createdBy, createdAt, modifiedBy, modifiedAt);
    }
    public static ArticleCommentsDto from(ArticleComment entity){
        return new ArticleCommentsDto(
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
    public ArticleComment toEntity(Article entity){
        return ArticleComment.of(
                entity,
                userAccountDto.toEntity(),
                content
        );
    }
}