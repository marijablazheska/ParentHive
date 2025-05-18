package ParentHiveApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    private LocalDate date;
    private Integer postLevel;
//    @ElementCollection
//    private List<String> category = new ArrayList<>();

    public Post(){

    }

    public Post(String title, String content, String category, User user) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
        this.date = LocalDateTime.now().toLocalDate();
        if(content.contains("cochrane.org") || content.contains("embs.org") || content.contains("pubmed.ncbi.nlm.nih.gov")) {
            if(user.getProfessional()) {
                this.postLevel = 2;
            } else {
                this.postLevel = 1;
            }
        } else {
            this.postLevel = 0;
        }
    }

    public void incrementUpVote() {
        upvote++;
    }
    public void incrementDownVote() {
        downvote++;
    }
    public void incrementRepost() {
        repost++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUpvote() {
        return upvote;
    }

    public void setUpvote(Integer upvote) {
        this.upvote = upvote;
    }

    public Integer getDownvote() {
        return downvote;
    }

    public void setDownvote(Integer downvote) {
        this.downvote = downvote;
    }

    public Integer getRepost() {
        return repost;
    }

    public void setRepost(Integer repost) {
        this.repost = repost;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
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


