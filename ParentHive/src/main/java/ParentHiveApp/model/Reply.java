package ParentHiveApp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @Lob
    @Column(length = 10000)
    private String content;
    private Integer upvote = 0;
    private Integer downvote = 0;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    private Integer post_level = 0;
    private LocalDate date;


    public Reply(){

    }

    public Reply(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.date = LocalDateTime.now().toLocalDate();

        if(content.contains("cochrane.org") || content.contains("embs.org") || content.contains("pubmed.ncbi.nlm.nih.gov")) {
            if(user.getProfessional()) {
                this.post_level = 2;
            } else {
                this.post_level = 1;
            }
        } else {
            this.post_level = 0;
        }
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Integer getPost_level() {
        return post_level;
    }

    public void setPost_level(Integer post_level) {
        this.post_level = post_level;
    }
}
