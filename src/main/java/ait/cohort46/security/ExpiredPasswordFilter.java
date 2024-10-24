package ait.cohort46.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class ExpiredPasswordFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (checkEndPoint(request.getMethod(), request.getServletPath())
                && authentication != null && authentication.getPrincipal() instanceof UserProfile) {
            UserProfile userProfile = (UserProfile) authentication.getPrincipal();
            if (!userProfile.isPasswordNotExpired()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndPoint(String method, String path) {
        return !(HttpMethod.PATCH.matches(method) || path.matches("/account/password"));
    }
}
