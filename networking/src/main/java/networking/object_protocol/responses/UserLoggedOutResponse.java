package networking.object_protocol.responses;

import networking.dto.UserDTO;
import networking.object_protocol.responses.interfaces.UpdateResponse;

public class UserLoggedOutResponse implements UpdateResponse {
    private UserDTO user;

    public UserDTO getUser() {
        return user;
    }

    public UserLoggedOutResponse(UserDTO user) {
        this.user = user;
    }
}
