package networking.dto;

import java.io.Serializable;
import java.util.UUID;

public class UserDTO implements Serializable {
    private UUID id;
    private String userEmail;
    private String userName;
    private String password;

    public UUID getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public UserDTO(UUID id, String userEmail, String userName, String password) {
        this.id = id;
        this.userEmail = userEmail;
        this.userName = userName;
        this.password = password;
    }
}
