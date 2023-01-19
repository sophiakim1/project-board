package com.koreait.projectboard.controller;

import com.koreait.projectboard.domain.type.FormStatus;
import com.koreait.projectboard.domain.type.SearchType;
import com.koreait.projectboard.dto.UserAccountDto;
import com.koreait.projectboard.dto.request.ArticleRequest;
import com.koreait.projectboard.dto.response.ArticleResponse;
import com.koreait.projectboard.dto.response.ArticleWithCommentResponse;
import com.koreait.projectboard.dto.security.BoardPrincipal;
import com.koreait.projectboard.service.ArticleService;
import com.koreait.projectboard.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("articles")
@Controller
@RequiredArgsConstructor // 생성자 추가시 작성
public class ArticleController {

    private final ArticleService articleService; // 생성자
    private final PaginationService paginationService;

    @GetMapping
    public String articles(
            @RequestParam(required = false)SearchType searchType,
            @RequestParam(required = false)String searchValue,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, ModelMap map
    ){
//        map.addAttribute("articles", List.of()); //처음에 만든거 주석
//        map.addAttribute("articles",articleService.searchArticles(searchType,searchValue, pageable).map(ArticleResponse::from)); //페이징 만들고나서 주석
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages()); //메소드를 만들어서 이렇게 가져올 수 있음
        map.addAttribute("articles", articles); //10개씩 쪼개진 데이터
        map.addAttribute("paginationBarNumbers", barNumbers); //페이지 정보
        map.addAttribute("searchTypes", SearchType.values());
        return "articles/index"; // articles의 index 전달
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map){
        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.getArticleWithComments(articleId));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentsResponses());
        map.addAttribute("totalCount", articleService.getArticleCount());
        return "articles/detail";
    }

    @GetMapping("/search-hashtag")
    public String searchArticleHashtag( //searchType이 hashtag로 정해져 있어서 지움!
                                        @RequestParam(required = false)String searchValue,
                                        @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, ModelMap map
    ){
        Page<ArticleResponse> articles = articleService.searchArticleViaHashTag(searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages()); //메소드를 만들어서 이렇게 가져올 수 있음
        List<String> hashTags = articleService.getHashtags();

        map.addAttribute("articles", articles); //10개씩 쪼개진 데이터
        map.addAttribute("hashtags", hashTags);
        map.addAttribute("paginationBarNumbers", barNumbers); //페이지 정보
        map.addAttribute("searchTypes", SearchType.values());
        return "articles/search-hashtag";
    }

    @GetMapping("form")
    public String articleForm(ModelMap map){
        map.addAttribute("formStatus", FormStatus.CREATE);
        return "articles/form";
    }

    @PostMapping ("/form")
    public String postNewArticle(
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            ArticleRequest articleRequest
    ) {
        articleService.saveArticle(articleRequest.toDto(boardPrincipal.toDto()));
        return "redirect:/articles";
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map){
        ArticleResponse article = ArticleResponse.from(articleService.getArticle(articleId));
        map.addAttribute("article", article);
        map.addAttribute("formStatus", FormStatus.UPDATE);
        return "articles/form";
    }

    @PostMapping ("/{articleId}/form")
    public String updateArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal,
            ArticleRequest articleRequest
    ) {
        articleService.updateArticle(articleId, articleRequest.toDto(boardPrincipal.toDto()));
        return "redirect:/articles/" + articleId;
    }

    @PostMapping("/{articleId}/delete")
    public String deleteArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ) {
        articleService.deleteArticle(articleId, boardPrincipal.getUsername());
        return "redirect:/articles";
    }
}
