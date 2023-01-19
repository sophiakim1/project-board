package com.koreait.projectboard.service;

import com.koreait.projectboard.domain.Article;
import com.koreait.projectboard.domain.UserAccount;
import com.koreait.projectboard.dto.ArticleCommentDto;
import com.koreait.projectboard.repository.ArticleCommentRepository;
import com.koreait.projectboard.repository.ArticleRepository;
import com.koreait.projectboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId){
        return List.of();
    }

    public void saveArticleComment(ArticleCommentDto dto){
        Article article = articleRepository.getReferenceById(dto.articleId());
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
        articleCommentRepository.save(dto.toEntity(article, userAccount));
    }

    public void updateArticleComment(ArticleCommentDto dto){

    }

    public void deleteArticleComment(Long articleCommentId, String userId) {
        articleCommentRepository.deleteByIdAndUserAccount_UserId(articleCommentId, userId);
    }

}
