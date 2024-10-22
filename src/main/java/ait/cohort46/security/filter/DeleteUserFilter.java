package ait.cohort46.security.filter;

import ait.cohort46.accounting.model.Role;
import ait.cohort46.security.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(40)
public class DeleteUserFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndpoint(request.getMethod(), request.getServletPath())) {
            User user = (User) request.getUserPrincipal();
            String[] parts = request.getServletPath().split("/");
            String owner = parts[parts.length - 1];
            if (!(user.getRoles().contains(Role.ADMINISTRATOR.name()) || user.getName().equalsIgnoreCase(owner))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return HttpMethod.DELETE.matches(method) && path.matches("/account/user/\\w+");
//                || HttpMethod.DELETE.matches(method) && path.matches("/forum/post/\\w+");
                // || (HttpMethod.PATCH.matches(method) && path.matches("/forum/post/\\w+"));
    }
}
