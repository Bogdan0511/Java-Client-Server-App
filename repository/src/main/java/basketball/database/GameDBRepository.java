package basketball.database;


import basketball.Game;
import basketball.Team;
import basketball.interfaces.GameRepository;

import java.time.LocalDateTime;
import java.util.*;

import java.sql.*;

import basketball.interfaces.TeamRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameDBRepository implements GameRepository {

    private TeamRepository teamRepository;
    private final String ADD_GAME_QUERRY = "INSERT INTO game VALUES(?, ?, ?, ?, ?, ?)";
    private final String DELETE_GAME_QUERRY = "DELETE FROM game WHERE id = ? ";
    private final String UPDATE_GAME_QUERRY = "UPDATE game SET price = ? , \"availableSeatsNumber\" = ? , \"gameTime\" = ? WHERE id = ?";
    private final String FIND_ALL_GAMES_QUERRY = "SELECT * FROM game ORDER BY \"availableSeatsNumber\" DESC";
    private final String FIND_ONE_GAME_QUEERY ="SELECT * FROM game WHERE game.id = ?";

    private JdbcUtils jdbcUtils;
    private final static Logger log = LogManager.getLogger();


    public GameDBRepository(Properties properties) {
        log.info("Creating GameDBRepository with properties : {} ", properties);
        this.jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Game entity) {
        log.traceEntry("Saving game entity {}",entity);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(ADD_GAME_QUERRY)){
            preparedStatement.setObject(1,entity.getGameID());
            preparedStatement.setObject(2,entity.getHomeTeam().getTeamID());
            preparedStatement.setObject(3,entity.getAwayTeam().getTeamID());
            preparedStatement.setTimestamp(4,entity.getGameTime());
            preparedStatement.setInt(5,entity.getAvailableSeats());
            preparedStatement.setFloat(6,entity.getTicketPrice());

            preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error ADD Game to Db" + sqlException.getMessage());
        }
        log.traceExit();
    }

    @Override
    public void delete(UUID entityID) {
        log.traceEntry("Deleting game entity with id {}",entityID);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(DELETE_GAME_QUERRY)){
            preparedStatement.setObject(1,entityID);
            preparedStatement.executeUpdate();

        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error Delete Game from DB" + sqlException.getMessage());
        }
        log.traceExit();
    }

    @Override
    public void update(Game newEntity, UUID entityID) {
        log.traceEntry("Updating date of game entity with id {} with the new date {} and new number of seats {}",entityID,newEntity.getGameTime(), newEntity.getAvailableSeats());
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(UPDATE_GAME_QUERRY)){
            preparedStatement.setFloat(1,newEntity.getTicketPrice());
            preparedStatement.setInt(2,newEntity.getAvailableSeats());
            preparedStatement.setObject(3,newEntity.getGameTime());
            preparedStatement.setObject(4,entityID);
            preparedStatement.executeUpdate();

        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error Update Game int DB" + sqlException.getMessage());
        }
        log.traceExit();
    }

    @Override
    public Iterable<Game> findAll() {
        log.traceEntry("Selecting all the games at : {}", LocalDateTime.now());
        Connection con = jdbcUtils.getConnection();
        List<Game> gameList = new ArrayList<>();
        try(PreparedStatement preparedStatement = con.prepareStatement(FIND_ALL_GAMES_QUERRY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()){
                    UUID id = (UUID) resultSet.getObject("id");
                    UUID homeTeamID = (UUID) resultSet.getObject("homeTeamID");
                    UUID awayTeamID = (UUID) resultSet.getObject("awayTeamID");
                    Timestamp gameTime = resultSet.getTimestamp("gameTime");
                    int availableSeatsNumber = resultSet.getInt("availableSeatsNumber");
                    float price = resultSet.getFloat("price");
                    Team homeTeam = teamRepository.findOne(homeTeamID);
                    Team awayTeam = teamRepository.findOne(awayTeamID);

                    gameList.add(new Game(id,homeTeam,awayTeam,gameTime,availableSeatsNumber,price));

                }
            }
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error find all game");
        }
        log.traceExit(gameList);
        return gameList;
    }

    public GameDBRepository(TeamRepository teamRepository, Properties props) {
        this.teamRepository = teamRepository;
        this.jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public Game findOne(UUID entityID) {
        log.traceEntry("Selecting a game with id {} at date : {}", entityID, LocalDateTime.now());
        Connection con = jdbcUtils.getConnection();
        Game foundGame = null;
        try(PreparedStatement preparedStatement = con.prepareStatement(FIND_ONE_GAME_QUEERY)) {
            preparedStatement.setObject(1, entityID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()){


                    UUID id = (UUID) resultSet.getObject("id");
                    UUID homeTeamID = (UUID) resultSet.getObject("homeTeamID");
                    UUID awayTeamID = (UUID) resultSet.getObject("awayTeamID");
                    Timestamp gameTime = resultSet.getTimestamp("gameTime");
                    int availableSeatsNumber = resultSet.getInt("availableSeatsNumber");

                    float price = resultSet.getFloat("price");
                    Team homeTeam = teamRepository.findOne(homeTeamID);
                    Team awayTeam = teamRepository.findOne(awayTeamID);

                   foundGame = new Game(id,homeTeam,awayTeam,gameTime,availableSeatsNumber,price);

                }
            }
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error find one game");
        }
        log.traceExit(foundGame);
        return foundGame;
    }
}
