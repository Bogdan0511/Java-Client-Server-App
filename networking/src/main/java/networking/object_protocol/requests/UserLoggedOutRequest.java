package networking.object_protocol.requests;

import networking.dto.UserDTO;
import networking.object_protocol.requests.interfaces.Request;

public class UserLoggedOutRequest implements Request {
    private UserDTO user;

    public UserDTO getUser() {
        return user;
    }

    public UserLoggedOutRequest(UserDTO user) {
        this.user = user;
    }
}
