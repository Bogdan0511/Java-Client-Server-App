package networking.object_protocol.responses;

import networking.dto.GameDTO;
import networking.object_protocol.responses.interfaces.Response;

public class GetGamesResponse implements Response {
    private GameDTO[] games;

    public GameDTO[] getGames() {
        return games;
    }

    public GetGamesResponse(GameDTO[] games) {
        this.games = games;
    }
}
