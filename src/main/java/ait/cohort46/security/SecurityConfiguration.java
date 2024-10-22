package ait.cohort46.security;

import ait.cohort46.accounting.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable()); // csrf - cross site request forgery
        http.authorizeHttpRequests(authorize -> authorize

                .requestMatchers("/account/register", "/forum/posts/**")
                .permitAll()

                // TODO change role permit only for admin
                .requestMatchers("account/user/*/role/*")
                .hasRole(Role.ADMINISTRATOR.name())

                //
                .requestMatchers(HttpMethod.PATCH, "/account/password", "account/user/*", "/forum/post/*")
                .hasRole(Role.USER.name())

                .anyRequest()
                .authenticated());
        return http.build();
    }
}
