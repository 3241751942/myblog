package com.zzl.myblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain  securityFilterChain(HttpSecurity http)throws Exception{
        http
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/","/css/**","/js/**","/image/**","/public/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/admin/**").authenticated()
                        .anyRequest().authenticated())
                .formLogin(form->form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/admin/home",true)//大坑，不填true会跳转到之前的页面
                        .failureUrl("/login?error=true"))
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()))
                .logout(logout->logout.permitAll())
                .csrf(csrf->csrf.disable());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("admin")
                .password("{noop}123456") // {noop} 表示不加密
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
