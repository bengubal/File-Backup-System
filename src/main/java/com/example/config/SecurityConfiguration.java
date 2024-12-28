package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // Password Encoder Bean
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Auth ile ilgili URL'lere erişimi izin ver
                        .requestMatchers("/fms/**").permitAll()  // FMS ile ilgili URL'lere erişimi izin ver
                        .requestMatchers("/admin/**").permitAll() // Admin ile ilgili URL'lere erişimi izin ver
                        .requestMatchers("/file-management").permitAll() // Bu URL'ye de izin ver
                        .requestMatchers("/login").permitAll() 
                        .requestMatchers("/static/**").permitAll()  // Statik dosyalar için
                        .requestMatchers("/login", "/static/**", "/public/**", "/css/**", "/js/**", "/images/**").permitAll() // "/login" ve statik kaynaklara izin ver
                        .anyRequest().authenticated()
                )
                        .formLogin(form -> form
                                .permitAll()
                        )
                        .logout(logout -> logout
                                .permitAll()
                        );
        return http.build();
    }

}