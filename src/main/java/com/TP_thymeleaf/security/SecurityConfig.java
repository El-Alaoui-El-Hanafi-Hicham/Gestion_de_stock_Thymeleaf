package com.TP_thymeleaf.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/products").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers("/categories").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers("/formProduct", "/saveProduct", "/editProduct").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/formCategory", "/saveCategory", "/editCategory").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/products", true)
                        .failureUrl("/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf().disable() // Enable for production
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login?invalid-session=true")
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Print credentials to console for verification
        System.out.println("\n=== ACTIVE CREDENTIALS ===");
        System.out.println("USERNAME: 'admin' | PASSWORD: 'admin123' | ROLES: ROLE_ADMIN, ROLE_USER");
        System.out.println("USERNAME: 'user'  | PASSWORD: 'password' | ROLES: ROLE_USER\n");

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin123")
                .roles("ADMIN", "USER")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
