package networking.dto;

import java.io.Serializable;
import java.util.UUID;

public class GameTicketDTO implements Serializable {
    private UUID id;
    private int numberOfSeats;
    private String username;

    private GameDTO game;

    public UUID getId() {
        return id;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public String getUsername() {
        return username;
    }

    public GameDTO getGame() {
        return game;
    }

    public GameTicketDTO(UUID id, int numberOfSeats, String username, GameDTO game) {
        this.id = id;
        this.numberOfSeats = numberOfSeats;
        this.username = username;
        this.game = game;
    }
}
