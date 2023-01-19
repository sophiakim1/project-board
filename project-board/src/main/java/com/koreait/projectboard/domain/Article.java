package com.koreait.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

// Entity: 데이터베이스와 연동될 애들
@Getter
@ToString(callSuper = true)// 자기거라 부모것도 불러와라
// 부하를 많이 주는 것: select. 그중에 큰 것이 order by. -> 부하 덜 가게 하는 기능이 indexing
// 내용이 긴데 거기서 select한다면 부하 매우 큼  // 다른 프로그램? 안쓰고 개발단계에서 해결할 수 있는 방법임
@Table(indexes = { //@Table은 엔티티와 매핑할 테이블을 지정, 여러 필드에 대해 주고 싶으면 중괄호 해서 안에 써주기
        @Index(columnList = "title"), // 결과가 여러개 라서 columnList
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
// name을 추가하면 테이블이름이 name값으로 설정이 되고 생략시 Entity이름으로 테이블이 만들어지는 것을 확인할 수 있다.
})
@Entity // 프라이머리키 설정 안하면 오류남
//@Entity 어노테이션은 JPA를 사용해 테이블과 매핑할 클래스에 붙여주는 어노테이션이다. 이 어노테이션을 붙임으로써 JPA가 해당 클래스를 관리하게 된다.
public class Article extends AuditingFields{ // 문제(ex.??)가 많기 떄문에 원래는 setter 이런거 함부로 쓰면 안됨 -> @Data 대신 필요한 것만 골라서 선언
    @Id // Entity 입력할 경우 프라이머리 키를 선언해 줘야한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 DB에 위임 (Mysql), DBMS마다 다름. MySQL의 경우 strategy를 IDENTITY로 해줌
    private Long id;
    @Setter @ManyToOne(optional = false) @JoinColumn(name="userId") private UserAccount userAccount;    // 유저정보,  알티클은 많이 들어가고 유저는 하나만 들어가야 한다.
    @Setter @Column(nullable = false) private String title; // 제목   // 얘네 셋은 변경할 수 있도록 Setter 설정,
    @Setter @Column(nullable = false, length = 10000) private String content; // 본문   // @Column(nullable = false): Null이 안되도록 함  // length=10000 1만자까지 가능하도록 설정
    @Setter private String hashtag; // 해시태그

    //JPA에서 DB Table의 Column을 Mapping 할 때 @Column Annotation을 사용한다.

    @ToString.Exclude // join하는 순간 toString에러가 있음. 그러니 ToString을 제외하고 값 뽑아 달라
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // mappedBy = "article": article과 연결   // cascade = CascadeType.ALL: crud에 대해서 에러 안나고 자유롭게 만들어 줄 수 있음??
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>(); // hashSet인데 Link로 되어있는 것??

    protected Article() {}

    private Article(UserAccount userAccount, String title, String content, String hashtag) {
        // 굳이 외부에서 부를 일 없고 내부에서만 쓸 것이기 때문에 private
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(UserAccount userAccount, String title, String content, String hashtag) { // of함수 객체를 만들 수 있는 함수
        // 외부에서 쓸 때는 이 함수를 통해 Article을 호출해서 사용
        return new Article(userAccount, title, content, hashtag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) { // 완벽하게 같은 객체인지 확인하는 메소드
        if(this == obj) return true; // 값 비교
        if(!(obj instanceof Article article)) return false; // 객체 비교
        return id != null && id.equals(article.id); // 완벽히 같으면 true
    }
}
