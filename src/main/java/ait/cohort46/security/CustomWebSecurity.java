package ait.cohort46.security;

import ait.cohort46.forum.dao.PostRepository;
import ait.cohort46.forum.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomWebSecurity {
    private final PostRepository postRepository;

    public boolean checkPostAuthor(String postId, String username) {
        Post post = postRepository.findById(postId).orElse(null);
        return post != null && post.getAuthor().equals(username);
    }
}
