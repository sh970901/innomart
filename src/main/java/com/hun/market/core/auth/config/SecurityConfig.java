package com.hun.market.core.auth.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 애노테이션 활성화
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//                        .requestMatchers("/backoffice/**").hasRole("ADMIN")
                        .requestMatchers("/resource/**", "/", "/main", "/login","/images/**", "/css/**", "/js/**", "/common/**", "/api/v1/items/**", "/api/v1/m/send-password-email", "/backoffice/**", "/tree/**","/welcome/**","/letter/**","/error","/tree-view/**").permitAll()
//                        .requestMatchers("/backoffice/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
//                        .anyRequest().permitAll())
//                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .csrf(AbstractHttpConfigurer::disable)
//                        .ignoringRequestMatchers(new AntPathRequestMatcher("/**")))
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                .formLogin((formLogin) -> formLogin
                        .loginPage("/welcome") //get
                        .loginProcessingUrl("/login") //post
                        .usernameParameter("mbName")
                        .passwordParameter("mbPassword")
                        .defaultSuccessUrl("/main", true))
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                .sessionManagement((auth) -> auth
                        .maximumSessions(3)
                        .maxSessionsPreventsLogin(false))

        ;
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**", "/favicon.ico");
    }
}
