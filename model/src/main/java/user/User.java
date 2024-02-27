package user;



import entity.Entity;

import java.util.Objects;
import java.util.UUID;

public class User extends Entity<UUID> {

    private String userEmail;
    private String userName;
    private String password;


    public UUID getUserID(){
        return super.getEntityID();
    }
    public String getPassword() {
        return password;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userEmail.equals(user.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail);
    }

    public User(String userEmail, String userName, String password) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.password = password;
        super.setEntityID(UUID.randomUUID());
    }

    public User(String email,String password){
        this.userEmail = email;
        this.password = password;
    }
    public User(UUID id,String userEmail, String userName, String password) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.password = password;
        super.setEntityID(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
