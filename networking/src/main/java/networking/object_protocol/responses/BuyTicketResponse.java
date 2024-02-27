package networking.object_protocol.responses;

import networking.dto.GameTicketDTO;
import networking.object_protocol.responses.interfaces.UpdateResponse;

public class BuyTicketResponse implements UpdateResponse {
    private GameTicketDTO gameTicketDTO;

    public GameTicketDTO getGameTicketDTO() {
        return gameTicketDTO;
    }

    public BuyTicketResponse(GameTicketDTO gameTicketDTO) {
        this.gameTicketDTO = gameTicketDTO;
    }
}
