package networking.dto;

import basketball.Game;
import basketball.GameTicket;
import basketball.Team;
import user.User;

import java.sql.Timestamp;
import java.util.UUID;

public class UtilsDTO {

    public static Game[] getFromDTO(GameDTO[] gameDTOS){
        Game[] games = new Game[gameDTOS.length];
        for (int i = 0; i<gameDTOS.length;++i)
            games[i] = UtilsDTO.getFromDTO(gameDTOS[i]);

        return games;
    }

    public static GameDTO[] getDTO(Game[] games){
        GameDTO[] gameDTOS = new GameDTO[games.length];
        for(int i = 0; i<gameDTOS.length; ++i){
            gameDTOS[i]= UtilsDTO.getDTO(games[i]);
        }
        return gameDTOS;
    }
    public static GameTicket getFromDTO(GameTicketDTO gameTicketDTO){
        UUID id = gameTicketDTO.getId();
        Game game = UtilsDTO.getFromDTO(gameTicketDTO.getGame());
        String username = gameTicketDTO.getUsername();
        int numberOfSeats = gameTicketDTO.getNumberOfSeats();

        return new GameTicket(id,numberOfSeats,username,game);
    }

    public static GameTicketDTO getDTO(GameTicket gameTicket){
        UUID id = gameTicket.getGameTicketID();
        GameDTO game = UtilsDTO.getDTO(gameTicket.getGame());
        String username = gameTicket.getUserName();
        int numberOfSeats = gameTicket.getNumberOfSeats();

        return new GameTicketDTO(id,numberOfSeats,username,game);
    }
    public static Game getFromDTO(GameDTO gameDTO){
        UUID id = gameDTO.getId();
        Team homeTeam = UtilsDTO.getFromDTO(gameDTO.getHomeTeam());
        Team awayTeam = UtilsDTO.getFromDTO(gameDTO.getAwayTeam());
        Timestamp date = gameDTO.getDate();
        float price = gameDTO.getPrice();
        int numberOfSeats = gameDTO.getNumberOfSeatsAvailable();

        return new Game(id,homeTeam,awayTeam,date,numberOfSeats,price);
    }

    public static GameDTO getDTO(Game game){
        UUID id = game.getGameID();
        TeamDTO homeTeam = UtilsDTO.getDTO(game.getHomeTeam());
        TeamDTO awayTeam = UtilsDTO.getDTO(game.getAwayTeam());
        Timestamp date = game.getDate();
        float price = game.getTicketPrice();
        int numberOfSeats = game.getAvailableSeats();

        return new GameDTO(id,homeTeam,awayTeam,numberOfSeats,price,date);
    }
    public static Team getFromDTO(TeamDTO teamDTO){
        UUID id = teamDTO.getId();
        String username = teamDTO.getName();

        return new Team(id,username);
    }

    public static TeamDTO getDTO(Team team){
        UUID id = team.getTeamID();
        String username = team.getTeamName();

        return new TeamDTO(id,username);
    }
    public static User getFromDTO(UserDTO userDTO){
        UUID id = userDTO.getId();
        String email = userDTO.getUserEmail();
        String username = userDTO.getUserName();
        String password = userDTO.getPassword();

        return new User(id,email,username,password);
    }

    public static UserDTO getDTO(User user){
        UUID id = user.getUserID();
        String email = user.getUserEmail();
        String username = user.getUserName();
        String password = user.getPassword();
        return new UserDTO(id,email,username,password);
    }
}
