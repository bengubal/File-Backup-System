package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // Password Encoder Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS Configuration Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");  // Tüm domainlerden erişime izin verir
        configuration.addAllowedMethod("*");        // Tüm HTTP metodlarına izin verir
        configuration.addAllowedHeader("*");        // Tüm başlıklara izin verir
        configuration.setAllowCredentials(true);    // Kimlik doğrulama bilgilerini iletmek için
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Tüm URL'lere bu CORS yapılandırmasını uygular
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF korumasını devre dışı bırakıyoruz
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS yapılandırmasını ekliyoruz
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // Yetkilendirme gerektirmeyen yollar
                .requestMatchers("/fms/**").permitAll()
                .requestMatchers("/admin/**").permitAll()
                .requestMatchers("/file-management").permitAll()
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated() // Geri kalan her istekte doğrulama gerektir
            )
            .httpBasic(httpBasic -> {}); // httpBasic() için yeni önerilen Customizer yöntemi
        return http.build();
    }
}
