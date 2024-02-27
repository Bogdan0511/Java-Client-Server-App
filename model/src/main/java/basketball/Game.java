package basketball;

import entity.Entity;


import java.sql.Timestamp;
import java.util.UUID;

public class Game extends Entity<UUID> {
    private Team homeTeam;
    private Team awayTeam;
    private Timestamp date;

    private int availableSeats;

    private Float ticketPrice;
    public Timestamp getGameTime() {
        return date;
    }

    public Float getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public String toString() {
        return  homeTeam + " vs " + awayTeam + " Date: " + date + "Price :" + ticketPrice;

    }

    public void setTicketPrice(Float ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Game(Team homeTeam, Team awayTeam, Timestamp gameTime, int availableSeats, Float ticketPrice) {
        super.setEntityID(UUID.randomUUID());
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = gameTime;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp gameTime) {
        this.date = gameTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Game(UUID givenUUID, Team homeTeam, Team awayTeam, Timestamp gameTime, int availableSeats, Float ticketPrice) {
        super.setEntityID(givenUUID);
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = gameTime;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
    }


    public UUID getGameID(){
        return super.getEntityID();
    }
    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }


}
