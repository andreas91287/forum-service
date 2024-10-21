package ait.cohort46.forum.service;

import ait.cohort46.forum.dto.AddCommentDto;
import ait.cohort46.forum.dto.AddOrUpdatePostDto;
import ait.cohort46.forum.dto.PostDto;

import java.time.LocalDate;
import java.util.List;

public interface PostService {
    PostDto addPost(String user, AddOrUpdatePostDto addPostDto);
    PostDto findPostById(String postId);
    PostDto updatePost(String postId, AddOrUpdatePostDto addPostDto);
    PostDto deletePost(String postId);

    void addLike(String postId);
    PostDto addComment(String postId, String commenter, AddCommentDto addCommentDto);

    Iterable<PostDto> findPostsByAuthor(String user);
    Iterable<PostDto> findPostsByTags(List<String> tags);
    Iterable<PostDto> findPostsByPeriod(LocalDate dateFrom, LocalDate dateTo);
}
