package ru.poldjoke.demo.jokebot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.poldjoke.demo.jokebot.model.UserAuthority;

@Slf4j
@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/registration", "/login").permitAll()
                                .mvcMatchers(HttpMethod.POST, "/jokes").hasAuthority(UserAuthority.PLACE_ORDERS.getAuthority())
                                .mvcMatchers(HttpMethod.GET, "/jokes/{id}").hasAuthority(UserAuthority.MANAGE_ORDERS.getAuthority())
                                .mvcMatchers(HttpMethod.POST, "/items").hasAuthority(UserAuthority.MANAGE_ORDERS.getAuthority())
                                .anyRequest().hasAuthority(UserAuthority.FULL.getAuthority()))
                .formLogin(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
