package ParentHiveApp.model;

import jakarta.persistence.*;
import lombok.Data;

//class Reply{
//    Id,
//    String Body,
//    UserId
//}
@Data
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Integer upvote = 0;
    private Integer downvote = 0;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;


    public Reply(){

    }

    public Reply(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }
}
