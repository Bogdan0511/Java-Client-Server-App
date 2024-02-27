package basketball.database;

import basketball.Team;
import basketball.interfaces.TeamRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TeamDBRepository implements TeamRepository {

    private final JdbcUtils jdbcUtils;
    private final String ADD_TEAM_QUERRY = "INSERT INTO team VALUES(?, ?)";
    private final String DELETE_TEAM_QUERRY = "DELETE FROM team WHERE id = ? ";
    private final String UPDATE_TEAM_QUERRY = "UPDATE team set name = ? WHERE id = ?";
    private final String FIND_ALL_TEAMS_QUERRY = "SELECT * from team";
    private final String FIND_ONE_TEAM_QUERRY ="SELECT  * from team WHERE id = ?";

    private final Logger log = LogManager.getLogger();
    public TeamDBRepository(Properties props) {
        this.jdbcUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Team entity) {
        log.traceEntry("Saving team entity {}",entity);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(ADD_TEAM_QUERRY)){
            preparedStatement.setObject(1,entity.getTeamID());
            preparedStatement.setString(2,entity.getTeamName());

            preparedStatement.executeUpdate();
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error ADD team to Db" + sqlException.getMessage());
        }
        log.traceExit();
    }

    @Override
    public void delete(UUID entityID) {
        log.traceEntry("Deleting team entity with id {}",entityID);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(DELETE_TEAM_QUERRY)){
            preparedStatement.setObject(1,entityID);
            preparedStatement.executeUpdate();

        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error team Game from DB" + sqlException.getMessage());
        }
        log.traceExit();
    }

    @Override
    public void update(Team newEntity, UUID entityID) {
        log.traceEntry("Updating name of team entity with id {} with the new name {}",entityID,newEntity.getTeamName());
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement(UPDATE_TEAM_QUERRY)){
            preparedStatement.setObject(1,newEntity.getTeamName());
            preparedStatement.setObject(2,entityID);
            preparedStatement.executeUpdate();

        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error Update team  int DB" + sqlException.getMessage());
        }
        log.traceExit();
    }

    @Override
    public Iterable<Team> findAll() {
        log.traceEntry("Selecting all the teams at : {}", LocalDateTime.now());
        Connection con = jdbcUtils.getConnection();
        List<Team> teamList = new ArrayList<>();
        try(PreparedStatement preparedStatement = con.prepareStatement(FIND_ALL_TEAMS_QUERRY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()){
                    UUID id = (UUID) resultSet.getObject("id");
                    String name = resultSet.getString("name");
                    teamList.add(new Team(id,name));

                }
            }
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error find all teams");
        }
        log.traceExit(teamList);
        return teamList;
    }

    @Override
    public Team findOne(UUID entityID) {
        log.traceEntry("Selecting a team with id {} at date : {}", entityID, LocalDateTime.now());
        Connection con = jdbcUtils.getConnection();
        Team foundTeam = null;
        try(PreparedStatement preparedStatement = con.prepareStatement(FIND_ONE_TEAM_QUERRY)) {
            preparedStatement.setObject(1, entityID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()){
                    UUID id = (UUID) resultSet.getObject("id");
                    String name = resultSet.getString("name");
                    foundTeam = new Team(id,name);
                }
            }
        }catch (SQLException sqlException){
            log.error(sqlException);
            System.out.println("Error find one team");
        }
        log.traceExit(foundTeam);
        return foundTeam;
    }
}
