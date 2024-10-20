package ait.cohort46.forum.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"user", "dateCreated"})
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
