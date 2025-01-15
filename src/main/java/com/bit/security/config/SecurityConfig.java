package com.bit.security.config;

import com.bit.security.service.UserDetailsServiceImpl;
import com.bit.security.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

// 이 클래스는 설정 클래스로 Spring 시작되면 자동 호출
@Configuration
// 웹 주소마다 접근 가능 여부를 Security 통해 처리
@EnableWebSecurity
// 메소드마다 접근 가능 여부를 Security 통해 처리
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, UserService userService, UserDetailsServiceImpl userDetailsServiceImpl) throws Exception {
        security
                // cors: Cross Origin Resource Sharing
                // 브라우저에서 보낸 요청과 돌아온 응답의 프로토콜, 도메인, 포트번호가 동일할 때에만 정상 동작하고 아니면 에러
                // 하지만 우리가 나중에 restfulAPI 서버까지 도입하게 되면 에러가 발생하기 때문에 cors를 꺼주기로 함.
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> // 접근 권한 설정
                                request
                                        // JSP 뷰 폴더는 누구나 다 접근 가능
                                        .requestMatchers("/WEB-INF/views/**").permitAll()
                                        // 최상위 인덱스 페이지는 누구나 다 접근 가능
                                        .requestMatchers("/").permitAll()
                                        //로그인용 페이지는 누구나 다 접근 가능
                                        .requestMatchers("/user/auth/").permitAll()

                                        .requestMatchers("/user/logout").permitAll()
                                        // /error, /favicon은 누구나 접근 가능
                                        .requestMatchers("/error", "/favicon.ico", "/resources/**").permitAll()

                                        // hasRole("ADMIN") -> ADMIN 만 해당 페이지 접근 가능
                                        .requestMatchers("/board/write").hasRole("ADMIN")
//                                        .requestMatchers("/board/upload").authenticated()

                                        // /board/는 로그인한 유저만 접근 가능
                                        .requestMatchers("/board/**").authenticated()

                                        // /upload/는 누구나 다 접근 가능
                                        .requestMatchers("/upload/**").permitAll()

                                        // /admin/은 관리자만 접근 가능
                                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        // 위에 설정되지 않은 모든 요청은 전부 로그인된 유저만 볼 수 있음
                        /*.anyRequest().authenticated()*/
                )
                // 커스텀 로그인 페이지 처리
                .formLogin(form ->
                        form
                                // 로그인할 때 어느 URL을 접근할지 지정
                                .loginPage("/")
                                .defaultSuccessUrl("/board/upload", true)
                                // 로그인을 처리할 URL
                                // 실제로는 우리의 컨트롤러가 처리하는 것이 아니라
                                // Spring Security가 처리하게 되고 form 태그에서
                                // action에 적을 URL을 적어주면 됨
                                .loginProcessingUrl("/user/auth")
                )
                .rememberMe(config -> config
                                // 로그인할 때 리멤버 미 옵션이 어떤 이름으로 들어올 지 지정.
                                // 만약 따로 지정하지 않으면 remember-me 라는 이름으로 자동 지정.(쿠키 생성)
                                .rememberMeParameter("remember-me")
                                // 리멤버 미가 유지될 시간은 초 단위이므로 잘 계산해서 넣기
                                .tokenValiditySeconds(86400 * 30)
                                // 리멤버 미를 사용할 때에는 반드시 DB에 현재 로그인한 사람의 정보를 저장해야하므로,
                                // service 객체를 등록하여 해당 service 객체가 리멤버 미를 DB에 저장.
                                .userDetailsService(userDetailsServiceImpl)
                                // 리멤버 미를 통해서 로그인을 성공했을 때
                                // 어떠한 액션을 취할지를 logInSuccessHandler 객체를 통해 해결
//                        .authenticationSuccessHandler(authenticationSuccessHandler())
                                // 로그인 을 성공한 사용자가 토큰을 저장할 때 어떤 이름으로 저장하고
                                // 리멤버 미가 정상 작동할 때 어떠한 이름의 키를 받아올지 지정.
                                .key("bit_security")

                )
                .logout(logout -> logout
                        // 로그아웃을 할 때 사용할 URL
                        .logoutUrl("/user/logout")
                        // 로그아웃을 할 때 삭제할 쿠키
                        .deleteCookies("remember-me")
                        // 로그아웃을 할 때 어떤 행동을 할것인지를 담당하는 로그아웃핸들러 만들어주기
                        .addLogoutHandler(
                                (request, response, authentication) -> {
                                    // 현재 사용자가 접속한 세션을 무효화 처리
                                    HttpSession session = request.getSession();
                                    if (session != null) {
                                        session.invalidate();
                                    }
                                }
                        )
                        // 로그아웃의 성공하면 어떠한 행동을 할 것인지를 담당하는 로그아웃 석세스핸들러 만들기
                        .logoutSuccessHandler((request, response, authentication) -> {
                                    // 로그아웃이 성공하면 인덱스 화면으로 돌아감
                                    response.sendRedirect("/");
                                }
                        )
                );
        return security.build();
    }

}
