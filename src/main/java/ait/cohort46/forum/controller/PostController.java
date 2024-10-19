package ait.cohort46.forum.controller;

import ait.cohort46.forum.dto.AddCommentDto;
import ait.cohort46.forum.dto.AddPostDto;
import ait.cohort46.forum.dto.PostDto;
import ait.cohort46.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forum")
public class PostController {
    private final PostService postService;

    @PostMapping("/post/{user}")
    public PostDto addPost(@PathVariable String user, @RequestBody AddPostDto addPostDto) {
        return postService.addPost(user, addPostDto);
    }

    @GetMapping("/post/{postId}")
    public PostDto findPostById(@PathVariable String postId) {
        return postService.findPostById(postId);
    }

    @PatchMapping("/post/{postId}")
    public PostDto updatePost(@PathVariable String postId, @RequestBody AddPostDto addPostDto) {
        return postService.updatePost(postId, addPostDto);
    }

    @DeleteMapping("/post/{postId}")
    public PostDto deletePost(@PathVariable String postId) {
        return postService.deletePost(postId);
    }

    @PatchMapping("/post/{postId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable String postId) {
        postService.addLike(postId);
    }

    @PatchMapping("/post/{postId}/comment/{commenter}")
    public PostDto addComment(@PathVariable String postId, @PathVariable String commenter, @RequestBody AddCommentDto addCommentDto) {
        return postService.addComment(postId, commenter, addCommentDto);
    }

    @GetMapping("/posts/author/{user}")
    public Iterable<PostDto> findPostsByAuthor(@PathVariable String user) {
        return postService.findPostsByAuthor(user);
    }

    @GetMapping("/posts/tags")
    public Iterable<PostDto> findPostsByTags(@RequestParam List<String> tags) {
        return postService.findPostsByTags(tags);
    }

    @GetMapping("/posts/period")
    public Iterable<PostDto> findPostsByPeriod(@RequestParam LocalDate dateFrom, @RequestParam LocalDate dateTo) {
        return postService.findPostsByPeriod(dateFrom, dateTo);
    }
}
