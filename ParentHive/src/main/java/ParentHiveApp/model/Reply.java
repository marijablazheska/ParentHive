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
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


    public Reply(){

    }

}
