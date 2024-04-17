package ru.mirea.Ablautova.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.mirea.Ablautova.security.filter.JwtFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
       return httpSecurity
               .csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(
                       config -> config
                               .requestMatchers("/api/v1/auth/**").permitAll()
                               .requestMatchers("/api/v1/account/**").permitAll()
                               .anyRequest().authenticated()
               )
               .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .httpBasic(AbstractHttpConfigurer::disable)
               .formLogin(AbstractHttpConfigurer::disable)
               .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
               .build();

    }

}
