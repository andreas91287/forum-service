package ait.cohort46.forum.dao;

import ait.cohort46.forum.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface PostRepository extends MongoRepository<Post, String> {
    Stream<Post> findByAuthorIgnoreCase(String user);

    Stream<Post> findByTagsInIgnoreCase(List<String> tags);

    Stream<Post> findByPeriodBetween(LocalDate dateFrom, LocalDate dateTo);
}
