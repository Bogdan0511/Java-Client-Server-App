package networking.object_protocol;

import basketball.Game;
import basketball.GameTicket;
import interfaces.IObserver;
import interfaces.IService;
import networking.dto.GameDTO;
import networking.dto.GameTicketDTO;
import networking.dto.UserDTO;
import networking.dto.UtilsDTO;
import networking.object_protocol.requests.BuyTicketRequest;
import networking.object_protocol.requests.GetGamesRequest;
import networking.object_protocol.requests.UserLoggedInRequest;
import networking.object_protocol.requests.UserLoggedOutRequest;
import networking.object_protocol.requests.interfaces.Request;
import networking.object_protocol.responses.*;
import networking.object_protocol.responses.interfaces.Response;
import networking.object_protocol.responses.interfaces.UpdateResponse;
import user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerObjectProxy implements IService {

    private String host;
    private int port;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> queueResponses;
    private volatile boolean finished;

    public ServerObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        queueResponses = new LinkedBlockingQueue<>();
    }

    private void handleUpdate(UpdateResponse update){
        if(update instanceof UserLoggedInResponse){
            UserLoggedInResponse userLoggedInResponse = (UserLoggedInResponse) update;
            UserDTO userDTO = userLoggedInResponse.getUser();
            User user = UtilsDTO.getFromDTO(userDTO);
            System.out.println("User logged in " + user);

            try{
                client.userLoggedIn(user);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        if(update instanceof UserLoggedOutResponse){
            UserLoggedOutResponse userLoggedOutResponse = (UserLoggedOutResponse) update;
            UserDTO userDTO = userLoggedOutResponse.getUser();
            User user = UtilsDTO.getFromDTO(userDTO);
            System.out.println("User logged out " + user);

            try{
                client.userLoggedOut(user);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        if(update instanceof BuyTicketResponse){
            BuyTicketResponse buyTicketResponse =(BuyTicketResponse) update;
            GameTicketDTO gameTicketDTO = buyTicketResponse.getGameTicketDTO();
            GameTicket gameTicket = UtilsDTO.getFromDTO(gameTicketDTO);
            try{
                client.userBoughtTicket(gameTicket);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void login(User userToLogin, IObserver client) throws Exception {
        initializeConnection();
        UserDTO userDTO = UtilsDTO.getDTO(userToLogin);
        sendRequest(new UserLoggedInRequest(userDTO));
        Response response =readResponse();
        if(response instanceof OkResponse){
            this.client = client;
        }
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new Exception(err.getMessage());
        }
    }

    @Override
    public void logout(User user, IObserver client) throws Exception {
        UserDTO udto= UtilsDTO.getDTO(user);
        sendRequest(new UserLoggedOutRequest(udto));
        Response response=readResponse();
        closeConnection();
        if (response instanceof ErrorResponse){
            ErrorResponse err =(ErrorResponse)response;
            throw new Exception(err.getMessage());
        }
    }

    @Override
    public Iterable<Game> findAllGames() throws Exception {
        sendRequest(new GetGamesRequest());
        Response response = readResponse();
        if(response instanceof ErrorResponse){
            ErrorResponse errorResponse = (ErrorResponse) response;
            throw new Exception(errorResponse.getMessage());
        }
        GetGamesResponse getGamesResponse = (GetGamesResponse) response;
        GameDTO[] gameDTOS = getGamesResponse.getGames();
        Game[] games = UtilsDTO.getFromDTO(gameDTOS);

        Iterable<Game> iterableGames = Arrays.asList(games);
        return iterableGames;

    }

    @Override
    public void buyTicket(GameTicket ticket) throws Exception {
        GameTicketDTO gameTicketDTO = UtilsDTO.getDTO(ticket);
        sendRequest(new BuyTicketRequest(gameTicketDTO));
        Response response =readResponse();
        if(response instanceof OkResponse){

        }

        if(response instanceof ErrorResponse){
            ErrorResponse errorResponse = (ErrorResponse)  response;
            throw new Exception(errorResponse.getMessage());
        }

    }

    private Response readResponse() {
        Response response=null;
        try{

            response=queueResponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws Exception {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (response instanceof UpdateResponse){
                        handleUpdate((UpdateResponse)response);
                    }else{
                        try {
                            queueResponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private void sendRequest(Request request)throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object "+e);
        }

    }
}
