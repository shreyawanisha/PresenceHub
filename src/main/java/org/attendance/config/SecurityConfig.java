package org.attendance.config;

import org.attendance.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT-based = no session needed
                )

                .authorizeHttpRequests(auth -> auth
                        // Public frontend routes
                        .requestMatchers(
                                "/", "/home", "/login", "/register",
//                                "/student/**", "/faculty/**", "/admin/**", // JSP-based routes
                                "/WEB-INF/views/**",
                                "/images/**", "/css/**", "/js/**"
                        ).permitAll()

                        .requestMatchers("/reports/**").permitAll()

                        // Public API routes
                        .requestMatchers(
                                "/api/users/login",
                                "/api/users/register"
                        ).permitAll()

                        //  Protect backend APIs
                        .requestMatchers("/api/**").authenticated()

                        //  Fallback
                        .anyRequest().permitAll()
                )

                //  No form-based login
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())

                //  No logout redirect (handled via frontend logout logic)
                .logout(logout -> logout.disable())

                // Add your custom JWT filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}