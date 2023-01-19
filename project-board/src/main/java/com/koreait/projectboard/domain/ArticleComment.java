package com.koreait.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class ArticleComment extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private Article article;    // 게시글(id) // 객체로 쓰려고 뒤에 id 안붙임  // 게시글 번호 하나에 댓글 여러개 등록될 수 있음. @ManyToOne, optional = false: null 값이 되더라도 값을 false로 두고 처리해야 되도록 만들어줌.
    @Setter @ManyToOne(optional = false) @JoinColumn(name="userId") private UserAccount userAccount; // 유저정보
    @Setter @Column(nullable = false, length = 500) private String content;    // 본문

    protected ArticleComment() {}

    private ArticleComment(Article article, UserAccount userAccount, String content){ // 굳이 외부에서 부를 일 없고 내부에서만 쓸 것이기 때문에 private
        this.article = article;
        this.userAccount = userAccount;
        this.content = content;
    }

    public static ArticleComment of(Article article, UserAccount userAccount, String content){
        return new ArticleComment(article, userAccount, content); // 외부에서 받아올 수 있도록 선언해주기
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) { // 완벽하게 같은 객체인지 확인하는 메소드
        if(this == obj) return true; // 값 비교
        if(!(obj instanceof ArticleComment articleComment)) return false; // 객체 비교
        return id != null && id.equals(articleComment.id); // 완벽히 같으면 true
    }
}
