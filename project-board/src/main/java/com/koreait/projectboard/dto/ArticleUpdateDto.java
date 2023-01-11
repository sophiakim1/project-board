package com.koreait.projectboard.dto;

public record ArticleUpdateDto(
        String title,
        String content,
        String hashTag
) {
    public static ArticleUpdateDto of(String title, String content, String hashtag) {
        return new ArticleUpdateDto(title, content, hashtag);
    }
}
