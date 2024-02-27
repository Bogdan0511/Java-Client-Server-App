package controllers;

import interfaces.IService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import user.User;

import java.awt.*;
import java.net.UnknownServiceException;

public class LoginViewController {

    private IService server;
    private TicketViewController ticketOfficeCtrl;
    private User loggedUser;
    Parent mainTicketOfficeParent;

    public void setServer(IService server) {
        this.server = server;
    }

    public void setTicketOfficeCtrl(TicketViewController ticketOfficeCtrl) {
        this.ticketOfficeCtrl = ticketOfficeCtrl;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setMainTicketOfficeParent(Parent mainTicketOfficeParent) {
        this.mainTicketOfficeParent = mainTicketOfficeParent;
    }


    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtFieldEmail;

    @FXML
    private PasswordField txtFieldPassword;

    @FXML
    void loginButtonPressed(MouseEvent event) {
        String email = txtFieldEmail.getText();
        String password = txtFieldPassword.getText();
        loggedUser  = new User(email,password);

        try{
            if(email.isEmpty() || password.isEmpty()) {
                throw new Exception("Please complete all fields");
            }
            server.login(loggedUser,ticketOfficeCtrl);

            Stage stage = new Stage();
            stage.setTitle("Chat window for " + loggedUser.getUserEmail());
            stage.setScene(new Scene(mainTicketOfficeParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    ticketOfficeCtrl.logOutButtonClicked(null);
                    System.exit(0);
                }
            });

            stage.show();
            ticketOfficeCtrl.setLoggedUser(loggedUser);
            ticketOfficeCtrl.setGames();
            ticketOfficeCtrl.setServer(server);
            ((Node)(event.getSource())).getScene().getWindow().hide();

        }catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MPP chat");
            alert.setHeaderText("Authentication failure");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void registerButtonPressed(MouseEvent event) {

    }
}
