package ait.cohort46.security.filter;

import ait.cohort46.forum.dao.PostRepository;
import ait.cohort46.forum.model.Post;
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
@Order(50)
public class UpdatePostFilter implements Filter {
    private final PostRepository repository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndpoint(request.getMethod(), request.getServletPath())) {
            String principal = request.getUserPrincipal().getName();
            String[] parts = request.getServletPath().split("/");
            String postId = parts[parts.length - 1];
            Post post = repository.findById(postId).orElse(null);
            if (post == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (!principal.equals(post.getAuthor())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN); // 404
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return (HttpMethod.PATCH.matches(method) && path.matches("/forum/post/\\w+"));
    }
}
