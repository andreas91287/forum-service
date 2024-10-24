package ait.cohort46.security;

import ait.cohort46.accounting.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CustomWebSecurity customWebSecurity;
    private final ExpiredPasswordFilter expiredPasswordFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable()); // csrf - cross site request forgery
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        http.addFilterBefore(expiredPasswordFilter, AuthorizationFilter.class);
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/account/register", "/forum/posts/**")
                    .permitAll()
                .requestMatchers("/account/user/{login}/role/{role}")
                    .hasRole(Role.ADMINISTRATOR.name())
                .requestMatchers(HttpMethod.PATCH,"/account/user/{login}", "/forum/post/{id}/comment/{login}")
                    .access(new WebExpressionAuthorizationManager("#login == authentication.name"))
                .requestMatchers(HttpMethod.DELETE,"/account/user/{login}")
                    .access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMINISTRATOR')"))
                .requestMatchers(HttpMethod.POST,"/forum/post/{author}")
                    .access(new WebExpressionAuthorizationManager("#author == authentication.name"))
                .requestMatchers(HttpMethod.PATCH,"/forum/post/{id}")
                    .access(((authentication, context) ->
                            new AuthorizationDecision(customWebSecurity.checkPostAuthor(
                                    context.getVariables().get("id"), authentication.get().getName()))))
                .requestMatchers(HttpMethod.DELETE,"/forum/post/{id}")
                    .access(((authentication, context) -> {
                        boolean checkAuthor = customWebSecurity.checkPostAuthor(context.getVariables().get("id"), authentication.get().getName());
                        boolean checkModerator = context.getRequest().isUserInRole(Role.MODERATOR.name());
                        return new AuthorizationDecision(checkAuthor || checkModerator);
                    }))
                .anyRequest()
                    .authenticated());
        return http.build();
    }
}
