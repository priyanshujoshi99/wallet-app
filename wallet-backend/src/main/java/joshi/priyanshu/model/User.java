package joshi.priyanshu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String _id;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPassword;
    private boolean isActive;
    private double balance;
}
