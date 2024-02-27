package networking.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

public class GameDTO implements Serializable {
    private UUID id;
    private TeamDTO homeTeam;
    private TeamDTO awayTeam;
    private int numberOfSeatsAvailable;
    private float price;
    private Timestamp date;

    public UUID getId() {
        return id;
    }

    public TeamDTO getHomeTeam() {
        return homeTeam;
    }

    public TeamDTO getAwayTeam() {
        return awayTeam;
    }

    public int getNumberOfSeatsAvailable() {
        return numberOfSeatsAvailable;
    }

    public float getPrice() {
        return price;
    }

    public Timestamp getDate() {
        return date;
    }

    public GameDTO(UUID id, TeamDTO homeTeam, TeamDTO awayTeam, int numberOfSeatsAvailable, float price, Timestamp date) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.numberOfSeatsAvailable = numberOfSeatsAvailable;
        this.price = price;
        this.date = date;
    }
}
