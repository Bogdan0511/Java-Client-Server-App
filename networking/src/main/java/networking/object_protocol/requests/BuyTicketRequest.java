package networking.object_protocol.requests;

import networking.dto.GameTicketDTO;
import networking.object_protocol.requests.interfaces.Request;

public class BuyTicketRequest implements Request {
    private GameTicketDTO gameTicketDTO;

    public GameTicketDTO getGameTicketDTO() {
        return gameTicketDTO;
    }

    public BuyTicketRequest(GameTicketDTO gameTicketDTO) {
        this.gameTicketDTO = gameTicketDTO;
    }
}
