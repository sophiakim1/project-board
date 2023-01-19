package com.koreait.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
//        @Index(columnList = "userid", unique = true),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingFields {
    // 테이블은 user, account는 만들지 말자 기본 변수로 선택된 경우도 있어 많은 오류를 발생시키기 때문이다.
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

//    @Setter
//    ID로 바꿔주면서 @Setter  nullable = false !변경 및 밑쪽의 id도 userID로 변경
    @Column(length = 50)
    private String userId;

    @Setter @Column(nullable = false) private String userPassword;
    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;
    @Setter
    private String memo;

    protected UserAccount() {}

    public UserAccount(String userId, String userPassword, String email, String nickname, String memo) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
    }

    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
        return new UserAccount(userId, userPassword, email, nickname, memo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public boolean equals(Object obj) { // 완벽하게 같은 객체인지 확인하는 메소드
        if(this == obj) return true; // 값 비교
        if(!(obj instanceof UserAccount userAccount)) return false; // 객체 비교
        return userId != null && userId.equals(userAccount.userId);  // 완벽히 같으면 true
    }
}
