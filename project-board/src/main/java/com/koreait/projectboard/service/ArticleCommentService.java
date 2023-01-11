package com.koreait.projectboard.service;

import com.koreait.projectboard.dto.ArticleCommentsDto;
import com.koreait.projectboard.repository.ArticleCommentRepository;
import com.koreait.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentsRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentsDto> searchArticleComment(Long articleId){
        return List.of();
    }

    public void saveArticleComment(ArticleCommentsDto dto){

    }

    public void updateArticleComment(ArticleCommentsDto dto){

    }
    public void deleteArticleComment(Long articleCommentId){

    }
}
