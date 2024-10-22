package ait.cohort46.security.filter;

import ait.cohort46.accounting.dao.UserAccountRepository;
import ait.cohort46.accounting.model.Role;
import ait.cohort46.accounting.model.UserAccount;
import ait.cohort46.security.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Order(10)
public class AuthentificationFilter implements Filter {
    private final UserAccountRepository repository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndpoint(request.getMethod(), request.getServletPath())) {
            // Credential - Berechtigung
            try {
                String[] credentials = getCredentials(request.getHeader("Authorization"));
                UserAccount userAccount = repository.findById(credentials[0]).orElseThrow(RuntimeException::new);
                if (!BCrypt.checkpw(credentials[1], userAccount.getPassword())) {
                    throw new RuntimeException();
                }
                Set<String> roles = userAccount.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.toSet());
                request = new WrappedRequest(request, userAccount.getLogin(), roles);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return !((HttpMethod.POST.matches(method) && path.matches("/account/register"))
                || (HttpMethod.GET.matches(method) && path.matches("/forum/post.+")));
    }

    private String[] getCredentials(String authorization) {
        String token = authorization.split(" ")[1];
        token = new String(Base64.getDecoder().decode(token));
        return token.split(":");
    }

    private class WrappedRequest extends HttpServletRequestWrapper {
        private String login;
        private Set<String> roles;

        public WrappedRequest(HttpServletRequest request, String login, Set<String> roles) {
            super(request);
            this.login = login;
            this.roles = roles;
        }

        @Override
        public Principal getUserPrincipal() {
            return new User(login, roles);
        }
    }
}
