import controllers.LoginViewController;
import controllers.TicketViewController;
import interfaces.IService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import networking.object_protocol.ServerObjectProxy;

import java.util.Properties;

public class StartClient extends Application {

    private Stage primaryStage;
    private final static int defaultPort=55555;
    private static String defaultServer="localhost";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProps = new Properties();
        try{
            clientProps.load(StartClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set");
            clientProps.list(System.out);
        }catch (Exception ex){
            System.out.println("Cannot find client.properties " + ex);
            return;
        }
        String serverIP = clientProps.getProperty("server.host",defaultServer);
        int serverPort =defaultPort;

        try{
            serverPort=Integer.parseInt(clientProps.getProperty("server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number "+ex.getMessage());
            System.out.println("Using default port: "+ defaultPort);
        }

        System.out.println("Using server IP " +serverIP);
        System.out.println("Using server port" + serverPort);

        IService server = new ServerObjectProxy(serverIP,serverPort);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login-view.fxml"));
        Parent root =loader.load();

        LoginViewController ctrl= loader.<LoginViewController>getController();
        ctrl.setServer(server);

        FXMLLoader tloader = new FXMLLoader(getClass().getClassLoader().getResource("ticket-office-view.fxml"));
        Parent troot  = tloader.load();

        TicketViewController ticketViewCtrl = tloader.<TicketViewController>getController();
        ticketViewCtrl.setServer(server);

        ctrl.setTicketOfficeCtrl(ticketViewCtrl);
        ctrl.setMainTicketOfficeParent(troot);

        primaryStage.setTitle("MPP Basket");
        primaryStage.setScene(new Scene(root,600,400));
        primaryStage.show();

    }
}
