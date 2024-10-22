package ait.cohort46.security.filter;

import ait.cohort46.accounting.dao.UserAccountRepository;
import ait.cohort46.accounting.model.Role;
import ait.cohort46.accounting.model.UserAccount;
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
@Order(60)
public class DeletePostFilter implements Filter {
    private final PostRepository postRepository;
    private final UserAccountRepository userAccountRepository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndpoint(request.getMethod(), request.getServletPath())) {
            UserAccount user = userAccountRepository.findById(request.getUserPrincipal().getName()).get();
            String[] parts = request.getServletPath().split("/");
            String postId = parts[parts.length - 1];
            Post post = postRepository.findById(postId).orElse(null);
            if (post == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (!(user.getLogin().equals(post.getAuthor()) || user.getRoles().contains(Role.MODERATOR))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return (HttpMethod.DELETE.matches(method) && path.matches("/forum/post/\\w+"));
    }
}
