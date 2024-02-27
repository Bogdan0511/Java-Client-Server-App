package user.database;

import basketball.database.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.User;
import user.interfaces.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class UserDBRepository implements UserRepository {
    private JdbcUtils jdbcUtils;

    private final String DELETE_USER_QUERRY = "DELETE FROM users WHERE id = ? ";
    private final String UPDATE_USER_QUERRY = "UPDATE users SET email = ? , username = ? , password = ? WHERE id = ?";
    private final String FIND_ALL_USER_QUERRY = "SELECT * FROM users";
    private final String FIND_ONE_USER_QUERRY ="SELECT * FROM users WHERE id = ? ";

    private final static Logger log = LogManager.getLogger();
    public UserDBRepository(Properties props) {

        this.jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public void add(User entity) {
        log.traceEntry("Saving User entity {}",entity);
        Connection con = jdbcUtils.getConnection();
        String ADD_USER_QUERRY = "INSERT INTO users VALUES(?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = con.prepareStatement(ADD_USER_QUERRY)){
            preparedStatement.setObject(1,entity.getUserID());
            preparedStatement.setString(2,entity.getUserEmail());
            preparedStatement.setString(3,entity.getUserName());
            preparedStatement.setString(4,entity.getPassword());

            preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error ADD user to Db" + sqlException.getMessage());
        }
        log.traceExit();
    }

    @Override
    public void delete(UUID entityID) {
        log.traceEntry("Deleting user entity with id {}",entityID);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(DELETE_USER_QUERRY)){
            preparedStatement.setObject(1,entityID);
            preparedStatement.executeUpdate();

        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error delete user from DB" + sqlException.getMessage());
        }
        log.traceExit();
    }

    @Override
    public void update(User newEntity, UUID entityID) {
        log.traceEntry("Updating user entity with id {} new user email {}, username {}",entityID,newEntity.getUserEmail(),newEntity.getUserName());
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(UPDATE_USER_QUERRY)){
            preparedStatement.setString(1,newEntity.getUserEmail());
            preparedStatement.setString(2,newEntity.getUserName());
            preparedStatement.setString(3,newEntity.getPassword());
            preparedStatement.setObject(4,newEntity.getUserID());
            preparedStatement.executeUpdate();

        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error Update user  int DB" + sqlException.getMessage());
        }
        log.traceExit();
    }

    @Override
    public Iterable<User> findAll() {
        log.traceEntry("Selecting all the users at : {}", LocalDateTime.now());
        Connection con = jdbcUtils.getConnection();
        List<User> userList = new ArrayList<>();
        try(PreparedStatement preparedStatement = con.prepareStatement(FIND_ALL_USER_QUERRY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()){
                    UUID id = (UUID) resultSet.getObject("iduser");
                    String userEmail = resultSet.getString("email");
                    String userName = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    userList.add(new User(id,userEmail,userName,password));
                }
            }
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error find all users");
        }
        log.traceExit(userList);
        return userList;
    }

    public UserDBRepository(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public User findOne(UUID entityID) {
        log.traceEntry("Selecting a user with id {} at date : {}", entityID, LocalDateTime.now());
        Connection con = jdbcUtils.getConnection();
        User foundUser = null;
        try(PreparedStatement preparedStatement = con.prepareStatement(FIND_ONE_USER_QUERRY)) {
            preparedStatement.setObject(1, entityID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()){
                    UUID id = (UUID) resultSet.getObject("iduser");
                    String userName = resultSet.getString("username");
                    String userEmail = resultSet.getString("email");
                    String password = resultSet.getString("password");

                    foundUser = new User(id,userName,userEmail,password);
                }
            }
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error find one user");
        }
        log.traceExit(foundUser);
        return foundUser;
    }
}
