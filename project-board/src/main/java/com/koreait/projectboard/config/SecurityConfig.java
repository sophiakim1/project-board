package com.koreait.projectboard.config;

import com.koreait.projectboard.dto.UserAccountDto;
import com.koreait.projectboard.dto.security.BoardPrincipal;
import com.koreait.projectboard.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 직접 빈 설정파일에 빈을 등록하는 방법
@Configuration//컨피그로 적용을 하기 위해 사용, 이를 사용하기 위해서는 메인에서 @ConfigurationPropertiesScan 필요
// 위 페이지는 사용자가 직접 만드는 페이지가 아니기 때문에 괜찮음
// @Bean 을 수동으로 등록하고 있는 모습이다.
public class SecurityConfig {
    @Bean // 하단 부 빈 주입하는 방법(SecurityConfig 설정 주입) -> 해당 사용자는 접근할 수 없는 보안 기능
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(
                                HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin().and()
                .logout()
                    .logoutSuccessUrl("/")
                    .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository){
        return username -> userAccountRepository.findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}



// @Bean : 오래된 방식의 java 오브젝트, 속성으로는 class, id, scope, constructor-arg 가 있으며 ID 컨테이너에 의해 생성 및 관리된다.
// 해당 클래스를 직접 만드는 것이 아닌 가져다 쓰는 클래스인 경우에는 @Bean을 등록해 줘야한다. (+ 공유 기능, + 유지보수 용이)
// 자바에서는 new 연산자를 사용하여 객체를 생성하였다면, Spring 에서는 이를 Bean이라고 한다.
// Bean은 Spring의 ApplicationContext 가 담고있는 객체라고 생각하면 편하겠다.

// 자동 등록 방법
//@ComponentScan 어노테이션은 어느 지점부터 컴포넌트를 찾으라고 알려주는 역할
// @Component 어노테이션이 있는 클래스들을 찾아서 자동으로 빈 등록 (자동 등록)
