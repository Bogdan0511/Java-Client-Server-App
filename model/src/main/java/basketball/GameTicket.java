package basketball;

import entity.Entity;

import java.util.UUID;

public class GameTicket extends Entity<UUID> {
    private int numberOfSeats;
    private String userName;
    private Game game;

    public UUID getGameTicketID(){
        return super.getEntityID();
    }
    public GameTicket(int numberOfSeats, String userName, Game game) {
        this.numberOfSeats = numberOfSeats;
        this.game = game;
        this.userName = userName;
        super.setEntityID(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return game.toString();
    }

    public GameTicket(UUID id, int numberOfSeats, String userName, Game game) {
        this.numberOfSeats = numberOfSeats;
        this.game = game;
        this.userName = userName;
        super.setEntityID(id);
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
