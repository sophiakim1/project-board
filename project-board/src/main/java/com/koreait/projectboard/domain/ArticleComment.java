package com.koreait.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    @Setter @ManyToOne(optional = false) private Article article; //게시글(id)
    @Setter @ManyToOne(optional = false) private UserAccount userAccount; //유저정보
    @Setter @Column(nullable = false, length = 500) private String content;

    protected ArticleComment() {}

    private ArticleComment(Article article, UserAccount userAccount, String content){
        this.article = article;
        this.userAccount = userAccount;
        this.content = content;
    }

    public static ArticleComment of(Article article, UserAccount userAccount, String content){
        return new ArticleComment(article, userAccount, content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof ArticleComment articleComment)) return false;
        return id != null && id.equals(articleComment.id);
    }
}
