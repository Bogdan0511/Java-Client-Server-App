import basketball.database.GameDBRepository;
import basketball.database.GameTicketDBRepository;
import basketball.database.TeamDBRepository;
import basketball.interfaces.GameRepository;
import basketball.interfaces.GameTicketRepository;
import basketball.interfaces.TeamRepository;
import interfaces.IService;
import networking.utils.AbstractServer;
import server.SerialServer;
import serverimpl.ServerImpl;
import user.database.UserDBRepository;
import user.interfaces.UserRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class StartServer {

    private static final int defaultPort = 55555;

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(StartServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Database properties set.");;
            props.list(System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserRepository userRepository = new UserDBRepository(props);
        TeamRepository teamRepository = new TeamDBRepository(props);
        GameRepository gameRepository = new GameDBRepository(teamRepository,props);
        GameTicketRepository gameTicketRepository = new GameTicketDBRepository(gameRepository,props);

        int serverPort = defaultPort;
        try{
            serverPort = Integer.parseInt(props.getProperty("server.port"));
        }catch(Exception ex){
            System.err.println("Wrong  Port Number"+ex.getMessage());
            System.err.println("Using default port "+defaultPort);
        }

        System.out.println("Starting server on port: "+ serverPort);

        IService serverImpl = new ServerImpl(userRepository,teamRepository,gameRepository,gameTicketRepository);
        AbstractServer server = new SerialServer(defaultPort,serverImpl);

        try {
            server.start();
        }catch (Exception ex){
            System.err.println("Error starting the server" + ex.getMessage());
        }

    }
}
