package com.drinkeg.drinkeg.global.config;

import com.drinkeg.drinkeg.global.logging.filter.HttpLoggingFilter;
import com.drinkeg.drinkeg.global.security.jwt.*;
import com.drinkeg.drinkeg.infra.redis.RedisClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final RedisClient redisClient;


    // AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                    .requestMatchers("/join/**","/login/apple/**","/login/kakao/**",
                            "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html/**", "/v3/api-docs/**", "/swagger-ui/index.html#/**",
                            "/clientSecret","/check-environment","reissue",
                            "/actuator/**");// 필터를 타면 안되는 경로
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("""
                ROLE_ADMIN > ROLE_USER
                """);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable);

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        // 쿠키를 반환하여 JWT를 전달하기 때문에 허용 필요
                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        http.addFilterBefore(new HttpLoggingFilter(), UsernamePasswordAuthenticationFilter.class);

        //JWT 필터 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, tokenService, redisClient),
                        UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, redisClient), LogoutFilter.class);


        http
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()));

        //경로별 인가 작업
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html/**", "/v3/api-docs/**", "/swagger-ui/index.html#/**").permitAll()
                        .requestMatchers("/", "/join/**", "/login", "/reissue","/login/apple","/login/kakao","/clientSecret","/check-environment","reissue").permitAll()
                        .requestMatchers("/actuator/**").permitAll()

                        .requestMatchers(HttpMethod.GET,"/home").hasRole("USER")
                        .requestMatchers("/wine/**").hasRole("USER")
                        .requestMatchers("/tasting-note/**").hasRole("USER")
                        .requestMatchers("/wine-wishlist/**").hasRole("USER")
                        .requestMatchers("/my-wine/**").hasRole("USER")
                        .requestMatchers("/wine-class/**").hasRole("USER")
                        .requestMatchers("/parties/**").hasRole("USER")
                        .requestMatchers("/comments/**").hasRole("USER")
                        .requestMatchers("/partyJoin/**").hasRole("USER")

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated());


        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}