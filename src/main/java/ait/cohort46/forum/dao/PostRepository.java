package ait.cohort46.forum.dao;

import ait.cohort46.forum.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {

}
