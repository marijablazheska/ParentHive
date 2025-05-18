package ParentHiveApp.model;

//class User{
//    Id,
//    String Username,
//    List<Post> Posts,
//    List<Post> SavedPosts,
//    List<Post> LikedPosts,
//
//
//}

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Boolean professional = false;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Post> posts;
    @ElementCollection
    private List<Long> SavedPosts; // Posle baraj po id
    @ElementCollection
    private List<Long> upVotedPosts; // Posle baraj po id
    @ElementCollection
    private List<Long> downVotedPosts; // Posle baraj po id
    @ElementCollection
    private List<Long> repostedPosts; // Posle baraj po id
    @ElementCollection
    private List<Long> upVotedReplies; // Posle baraj po id
    @ElementCollection
    private List<Long> downVotedReplies; // Posle baraj po id
    private String role = "ROLE_USER";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Long> getSavedPosts() {
        return SavedPosts;
    }

    public void setSavedPosts(List<Long> savedPosts) {
        SavedPosts = savedPosts;
    }

    public List<Long> getUpVotedPosts() {
        return upVotedPosts;
    }

    public void setUpVotedPosts(List<Long> upVotedPosts) {
        this.upVotedPosts = upVotedPosts;
    }

    public List<Long> getDownVotedPosts() {
        return downVotedPosts;
    }

    public void setDownVotedPosts(List<Long> downVotedPosts) {
        this.downVotedPosts = downVotedPosts;
    }

    public List<Long> getRepostedPosts() {
        return repostedPosts;
    }

    public void setRepostedPosts(List<Long> repostedPosts) {
        this.repostedPosts = repostedPosts;
    }

    public List<Long> getUpVotedReplies() {
        return upVotedReplies;
    }

    public void setUpVotedReplies(List<Long> upVotedReplies) {
        this.upVotedReplies = upVotedReplies;
    }

    public List<Long> getDownVotedReplies() {
        return downVotedReplies;
    }

    public void setDownVotedReplies(List<Long> downVotedReplies) {
        this.downVotedReplies = downVotedReplies;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getProfessional() { return professional; }

    public void setProfessional(Boolean professional) { this.professional = professional; }
}
