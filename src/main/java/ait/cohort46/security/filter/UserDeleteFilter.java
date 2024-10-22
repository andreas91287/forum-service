package ait.cohort46.security.filter;

import ait.cohort46.accounting.dao.UserAccountRepository;
import ait.cohort46.accounting.model.Role;
import ait.cohort46.accounting.model.UserAccount;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(40)
public class UserDeleteFilter implements Filter {
    private final UserAccountRepository repository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if (checkEndpoint(request.getMethod(), request.getServletPath())) {
            UserAccount user = repository.findById(request.getUserPrincipal().getName()).get();
            String[] arr = request.getServletPath().split("/");
            String owner = arr[arr.length - 1];
            if (user.getRoles().contains(Role.ADMINISTRATOR) || user.getLogin().equalsIgnoreCase(owner)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return HttpMethod.DELETE.matches(method) && path.matches("/account/user/\\w+");
    }
}
