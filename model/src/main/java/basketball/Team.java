package basketball;


import entity.Entity;

import java.util.UUID;

public class Team extends Entity<UUID> {

    public UUID getTeamID(){
        return super.getEntityID();
    }
    private String teamName;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }


    public Team(UUID givenUUID,String teamName) {
        this.teamName = teamName;
        super.setEntityID(givenUUID);
    }

    @Override
    public String toString() {
        return teamName;
    }

    public Team(String teamName) {
        this.teamName = teamName;
        super.setEntityID(UUID.randomUUID());
    }


}
