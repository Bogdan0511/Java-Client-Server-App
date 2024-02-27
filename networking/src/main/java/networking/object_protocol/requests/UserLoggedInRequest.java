package networking.object_protocol.requests;

import networking.dto.UserDTO;
import networking.object_protocol.requests.interfaces.Request;

public class UserLoggedInRequest implements Request {
    private UserDTO user;
    public UserLoggedInRequest(UserDTO user) { this.user = user;}

    public UserDTO getUser() {
        return user;
    }
}
