package ait.cohort46.forum.service;

import ait.cohort46.forum.dao.PostRepository;
import ait.cohort46.forum.dto.AddCommentDto;
import ait.cohort46.forum.dto.AddPostDto;
import ait.cohort46.forum.dto.PostDto;
import ait.cohort46.forum.dto.excception.PostNotFoundException;
import ait.cohort46.forum.model.Comment;
import ait.cohort46.forum.model.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostDto addPost(String user, AddPostDto addPostDto) {
        Post post = modelMapper.map(addPostDto, Post.class);
        post.setAuthor(user);
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto findPostById(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto updatePost(String postId, AddPostDto addPostDto) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        String title = addPostDto.getTitle();
        if (title != null) {
            post.setTitle(title);
        }
        if (addPostDto.getContent() != null) {
            post.setContent(addPostDto.getContent());
        }
        Set<String> tags = addPostDto.getTags();
        if (tags != null) {
            // tags.forEach(tag -> post.addTag(tag));
            tags.forEach(post::addTag);
        }
        postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto deletePost(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        // postRepository.deleteById(postId);
        postRepository.delete(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public void addLike(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.addLike();
        postRepository.save(post);
    }

    @Override
    public PostDto addComment(String postId, String commenter, AddCommentDto addCommentDto) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Comment comment = new Comment(commenter, addCommentDto.getMessage());
        post.addComment(comment);
        postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public Iterable<PostDto> findPostsByAuthor(String user) {
        return null;
    }

    @Override
    public Iterable<PostDto> findPostsByTags(List<String> tags) {
        return null;
    }

    @Override
    public Iterable<PostDto> findPostsByPeriod(LocalDate dateFrom, LocalDate dateTo) {
        return null;
    }
}
