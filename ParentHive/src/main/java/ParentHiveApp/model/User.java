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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Post> posts;
    @ElementCollection
    private List<Long> SavedPosts; // Posle baraj po id
    @ElementCollection
    private List<Long> LikedPosts; // Posle baraj po id
    private String role = "ROLE_USER";
}
