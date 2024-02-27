package networking.object_protocol.responses;

import networking.object_protocol.responses.interfaces.Response;

public class ErrorResponse implements Response {
    private String message;

    public String getMessage() {
        return message;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }
}
