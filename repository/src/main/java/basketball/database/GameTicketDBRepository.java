package basketball.database;

import basketball.Game;
import basketball.GameTicket;
import basketball.interfaces.GameRepository;
import basketball.interfaces.GameTicketRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameTicketDBRepository implements GameTicketRepository {

    private JdbcUtils jdbcUtils;

    private GameRepository gameRepository;
    private final String ADD_GAMETICKET_QUERRY = "INSERT INTO gameTicket VALUES(?, ?, ?, ?)";
    private final String DELETE_GAMETICKET_QUERRY = "DELETE FROM gameTicket WHERE id = ? ";
    private final String FIND_ALL_GAMETICKETS_QUERRY = "SELECT * FROM gameTicket ";

    private final String FIND_ONE_GAMETICKET_QUEERY ="SELECT * FROM gameTicket WHERE  gameTicket.id = ?";

    private final static Logger log = LogManager.getLogger();
    @Override
    public void add(GameTicket entity) {
        log.traceEntry("Saving gameTicket entity {}",entity);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(ADD_GAMETICKET_QUERRY)){
            preparedStatement.setObject(1,entity.getGameTicketID());
            preparedStatement.setInt(2,entity.getNumberOfSeats());
            preparedStatement.setString(3,entity.getUserName());
            preparedStatement.setObject(4,entity.getGame().getGameID());

            preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error ADD GameTicket to Db" + sqlException.getMessage());
        }
        log.traceExit();
    }

    @Override
    public void delete(UUID entityID) {
        log.traceEntry("Deleting gameticket entity with id {}",entityID);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(DELETE_GAMETICKET_QUERRY)){
            preparedStatement.setObject(1,entityID);
            preparedStatement.executeUpdate();

        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error Delete Gameticket from DB" + sqlException.getMessage());
        }
        log.traceExit();
    }

    @Override
    public void update(GameTicket newEntity, UUID entityID) {
        throw new RuntimeException("Not implemented exception");
    }

    @Override
    public Iterable<GameTicket> findAll() {
        log.traceEntry("Selecting all the gametickets at : {}", LocalDateTime.now());
        Connection con = jdbcUtils.getConnection();
        List<GameTicket> gameTicketList = new ArrayList<>();
        try(PreparedStatement preparedStatement = con.prepareStatement(FIND_ALL_GAMETICKETS_QUERRY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()){
                    UUID id = (UUID) resultSet.getObject("id");
                    UUID gameID = (UUID) resultSet.getObject("gameID");
                    int numberOfSeats = resultSet.getInt("numberOfSeats");
                    String userName = resultSet.getString("userName");

                    gameTicketList.add(new GameTicket(id,numberOfSeats,userName,gameRepository.findOne(gameID)));

                }
            }
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error find all game");
        }
        log.traceExit(gameTicketList);
        return gameTicketList;
    }

    public GameTicketDBRepository(GameRepository gameRepository, Properties props) {
        this.jdbcUtils = new JdbcUtils(props);
        this.gameRepository = gameRepository;
    }

    @Override
    public GameTicket findOne(UUID entityID) {
        log.traceEntry("Finding one gameticket with id {} at : {}", entityID,LocalDateTime.now());
        Connection con = jdbcUtils.getConnection();
        GameTicket foundGameTicket = null;
        try(PreparedStatement preparedStatement = con.prepareStatement(FIND_ONE_GAMETICKET_QUEERY)) {
            preparedStatement.setObject(1, entityID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()){
                    UUID id = (UUID) resultSet.getObject("id");
                    UUID gameID = (UUID) resultSet.getObject("gameID");
                    int numberOfSeats = resultSet.getInt("numberOfSeats");
                    String userName = resultSet.getString("userName");

                    foundGameTicket = new GameTicket(id,numberOfSeats,userName,gameRepository.findOne(gameID));

                }
            }
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error find one gameticket");
        }
        log.traceExit(foundGameTicket);
        return foundGameTicket;
    }
}
