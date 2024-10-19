package ait.cohort46.forum.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = {"user", "dateCreated"})
@NoArgsConstructor
public class Comment {
    @Setter
    private String user;
    @Setter
    private String message;
    private LocalDateTime dateCreated = LocalDateTime.now();
    private long likes;

    public Comment(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public void addLike() {
        likes++;
    }
}
