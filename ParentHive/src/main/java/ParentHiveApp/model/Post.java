package ParentHiveApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//Class Post{
//Id,
//UserId, TODO
//String Title,
//String Body,
//Integer upvote,
//Integer downvote,
//Integer repost,
//List Class<Reply>,
//List<String> Category
//}

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // TODO UserId
    // #############
    private String title;
    private String content;
    private Integer upvote = 0;
    private Integer downvote = 0;
    private Integer repost = 0;
    @OneToMany(mappedBy = "Post", fetch = FetchType.EAGER)
    private List<Reply> replies = new ArrayList<>();
    private String category;
//    @ElementCollection
//    private List<String> category = new ArrayList<>();

    public Post() {

    }

    public Post(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}


