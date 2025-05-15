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
    @ManyToOne
    @JoinColumn(name = "user_id") //
    private User user;
    private String title;
    private String content;
    private Integer upvote = 0;
    private Integer downvote = 0;
    private Integer repost = 0;
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}


