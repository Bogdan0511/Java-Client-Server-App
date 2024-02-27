package controllers;

import basketball.Game;
import basketball.GameTicket;
import basketball.Team;
import interfaces.IObserver;
import interfaces.IService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import user.User;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

public class TicketViewController implements Initializable, IObserver {


    ObservableList<Game> observableListGames = FXCollections.observableArrayList();
    List<Game> gameList;

    @FXML
    private TableColumn<Game, Team> awayTeamColumn;

    @FXML
    private Button btnBuyTickets;

    @FXML
    private Button btnLogOut;

    @FXML
    private TableColumn<Game, Timestamp> dateColumn;

    @FXML
    private TableView<Game> gameTableView;

    @FXML
    private TableColumn<Game, Team> homeTeamColumn;

    @FXML
    private Label labelSelectedMatchDateInfo;

    @FXML
    private Label labelSelectedMatchTeamsInfo;

    @FXML
    private Label labelSelectedSeatsAndPrice;

    @FXML
    private TableColumn<Game, Integer> numberOfSeatsColumn;

    @FXML
    private TableColumn<Game, Float> priceColumn;

    @FXML
    private TextField textFieldCustomerName;

    @FXML
    private Label serverInfoLabel;

    public TicketViewController() {

    }

    public TicketViewController(IService server) {
        this.server = server;
    }

    @FXML
    private TextField textFieldNumberOfSeats;

    @FXML
    private TextField textFieldSearchMatchBar;

    @FXML
    void buyTicketsButtonClicked(MouseEvent event) {
        try{
            if(gameTableView.getSelectionModel().getSelectedItems().isEmpty())
                throw new Exception("No game was selected");

            if(textFieldNumberOfSeats.getText().isEmpty() && textFieldCustomerName.getText().isEmpty())
                throw new Exception("Please complete the fields for buying");

            Game game = gameTableView.getSelectionModel().getSelectedItems().get(0);
            GameTicket gameTicket = new GameTicket(Integer.parseInt(textFieldNumberOfSeats.getText()),textFieldCustomerName.getText(),game);
            server.buyTicket(gameTicket);

            Alert alertWindow = new Alert(Alert.AlertType.INFORMATION);
            alertWindow.setContentText("Ticket bought succesfully");
            alertWindow.show();

            labelSelectedSeatsAndPrice.setText("");
            labelSelectedMatchDateInfo.setText("");
            labelSelectedMatchTeamsInfo.setText("");
            gameTableView.getSelectionModel().clearSelection();
        }
        catch (Exception ex){
            Alert alertWindow = new Alert(Alert.AlertType.INFORMATION);
            alertWindow.setContentText(ex.getMessage());
            alertWindow.show();
        }
    }

    @FXML
    void logOutButtonClicked(MouseEvent event) {
        logOut();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    private void logOut(){
        try{
            server.logout(loggedUser,this);
        }catch (Exception ex){
            System.out.println("Logout error " + ex);
        }
    }
    @FXML
    public void gameTableItemSelectionChanged(MouseEvent event) {
        if(!gameTableView.getSelectionModel().getSelectedItems().isEmpty()){
            Game game = gameTableView.getSelectionModel().getSelectedItems().get(0);
            labelSelectedMatchTeamsInfo.setText(game.getHomeTeam().getTeamName() +" vs " + game.getAwayTeam().getTeamName());
            labelSelectedMatchDateInfo.setText(game.getDate().toString());
            labelSelectedSeatsAndPrice.setText("Seats left: " + game.getAvailableSeats() + " Price: " +game.getTicketPrice() +'$');
        }
    }
    private IService server;

    private User loggedUser;

    Parent mainTicketOfficeParent;

    public void setServer(IService server) {
        this.server = server;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setMainTicketOfficeParent(Parent mainTicketOfficeParent) {
        this.mainTicketOfficeParent = mainTicketOfficeParent;
    }

    private void displayGames(List<Game> gamesList){
        gameTableView.getItems().clear();
        for(Game g : gamesList){
            gameTableView.getItems().add(g);
        }
        if(gameTableView.getItems().size() > 0)
            gameTableView.getSelectionModel().select(0);
    }
    public void setGames(){
        try{
            gameList = (List<Game>) server.findAllGames();
            displayGames(gameList);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameList = new ArrayList<>();

        initializeTable();
        serverInfoLabel.setText("");

    }

    private void initializeTable(){
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        homeTeamColumn.setCellValueFactory(new PropertyValueFactory<>("homeTeam"));
        awayTeamColumn.setCellValueFactory(new PropertyValueFactory<>("awayTeam"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("ticketPrice"));
        numberOfSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
    }

    @Override
    public void userLoggedIn(User userLoggedIn) {
        Platform.runLater(()->{
            serverInfoLabel.setText("S-a conectat userul cu email " + userLoggedIn.getUserEmail());
        });
    }

    @Override
    public void userLoggedOut(User userLoggedOut) {
        Platform.runLater(()->{
            serverInfoLabel.setText("S-a deconectat userul cu email " + userLoggedOut.getUserEmail());
        });
    }

    @Override
    public void userBoughtTicket(GameTicket gameTicket) {
        Platform.runLater(()->{
            serverInfoLabel.setText("S-a cumparat un bilet la meciul " + gameTicket.toString());
            for(int i = 0; i<gameList.size();++i){
                Game game = gameList.get(i);
                if(game.getGameID().equals(gameTicket.getGame().getGameID())){
                    game.setAvailableSeats(game.getAvailableSeats() - gameTicket.getNumberOfSeats());
                    gameList.set(i,game);
                    break;
                }
            }
            displayGames(gameList);
        });
    }
}
