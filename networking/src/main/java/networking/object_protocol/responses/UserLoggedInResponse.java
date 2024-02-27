package networking.object_protocol.responses;

import networking.dto.UserDTO;
import networking.object_protocol.responses.interfaces.UpdateResponse;

public class UserLoggedInResponse implements UpdateResponse {
    private UserDTO user;

    public UserDTO getUser() {
        return user;
    }

    public UserLoggedInResponse(UserDTO user) {
        this.user = user;
    }
}
