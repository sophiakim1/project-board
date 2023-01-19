package com.koreait.projectboard.service;

import com.koreait.projectboard.domain.Article;
import com.koreait.projectboard.domain.UserAccount;
import com.koreait.projectboard.domain.type.SearchType;
import com.koreait.projectboard.dto.ArticleDto;
import com.koreait.projectboard.dto.ArticleWithCommentsDto;
import com.koreait.projectboard.repository.ArticleRepository;
import com.koreait.projectboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j //lombok이 지원하는 log를 찍는 방법
@Transactional //springframework꺼를 임포트해야함 주의!!
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true) // 데이터에 권한을 주지 않는다 //읽기만 가능하게 한다!
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable){
        if(searchKeyword == null || searchKeyword.isBlank()){
            //map으로 변환시켜서 내보낸다. ArticleDto울 from 메소드를 통해..
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }
        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag(searchKeyword, pageable).map(ArticleDto::from);
        };
        //자바 17이라서 쓸 수 있는 새로운 switch문
//        return Page.empty();    //일단 아무것도 없는 페이지야!라고 리턴하고 나중에 데이터 입력해줄거임
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId){
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId){
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }
    //stream을 이용하는 메소드는 이렇게 쓸 수 있음 ::는 객체생성없이,static이라 사용가능..

    // 저장하는 곳, 일반적인 트렌젝션, 변하는 경우 데이터 권한을 주어야 하기 때문에 아무것도 안쓰는것이 맞다
    public void saveArticle(ArticleDto dto){
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
        articleRepository.save(dto.toEntity(userAccount));
    }

    public void updateArticle(Long articleId, ArticleDto dto){// 바로 세이브 하는것이 아니라 있는지 없는지 확인을 하고 업데이트를 해야한다. getReferenceById : 셀렉트 없이 실행
        try{
            Article article = articleRepository.getReferenceById(articleId); // save : select하고 있으면 변경 -- DB에 부당을 준다.
            //-> getOne 사용 select를 안함(자바17에서는--그어짐) -> 그래서 getReferenceById
            if(dto.title() != null) { article.setTitle(dto.title());} //get이 없다!  -- record라서!! 그냥 .id()=get .id(값)=set 새로운 문법
            if(dto.content() != null) { article.setContent(dto.content());} //get이 없다!  -- record라서!! 그냥 .id()=get .id(값)=set 새로운 문법
            article.setHashtag(dto.hashtag());
            // dto.title() 없으면 get ()안에 머가 있으면 set -> 자바17버전
        }catch(EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없음 - dto: {}", dto); // 디버깅할때 메리트 있는
        }
    }

    public void deleteArticle(long articleId, String userId) {
        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
    }

    public long getArticleCount(){
        return articleRepository.count();
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticleViaHashTag(String hashtag, Pageable pageable){
        if(hashtag == null || hashtag.isBlank()){
            return Page.empty(pageable);
        }
        return articleRepository.findByHashtag(hashtag, pageable).map(ArticleDto::from);
    }

    public List<String> getHashtags(){
        return articleRepository.findAllDistinctHashtags();
    }
}
