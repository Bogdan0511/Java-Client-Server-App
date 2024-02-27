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
import user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.List;

public class ClientObjectWorker implements Runnable,IObserver {
    private IService server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public ClientObjectWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void userLoggedIn(User userLoggedIn) {
        UserDTO userDTO = UtilsDTO.getDTO(userLoggedIn);
        System.out.println("User logged in " + userLoggedIn);
        try{
            sendResponse(new UserLoggedInResponse(userDTO));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void userLoggedOut(User userLoggedOut) {
        UserDTO userDTO = UtilsDTO.getDTO(userLoggedOut);
        System.out.println("User logged out " + userLoggedOut);
        try{
            sendResponse(new UserLoggedOutResponse(userDTO));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void userBoughtTicket(GameTicket gameTicket) {
        GameTicketDTO gameTicketDTO = UtilsDTO.getDTO(gameTicket);
        System.out.println("Bought ticket " + gameTicket);
        try{
            sendResponse(new BuyTicketResponse(gameTicketDTO));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public Response handleRequest(Request request){
        Response response = null;
        if(request instanceof UserLoggedInRequest){
            System.out.println("Login request ...");
            UserLoggedInRequest userLoggedInRequest = (UserLoggedInRequest) request;
            UserDTO userDTO = userLoggedInRequest.getUser();
            User user = UtilsDTO.getFromDTO(userDTO);

            try{
                server.login(user,this);
                return new OkResponse();
            }catch (Exception ex){
                connected=false;
                return new ErrorResponse(ex.getMessage());
            }
        }

        if(request instanceof UserLoggedOutRequest){
            System.out.println("Logout request ...");
            UserLoggedOutRequest userLoggedOutRequest = (UserLoggedOutRequest) request;
            UserDTO userDTO = userLoggedOutRequest.getUser();
            User user = UtilsDTO.getFromDTO(userDTO);
            try{
                server.logout(user,this);
                return new OkResponse();
            }catch (Exception ex){
                connected=false;
                return new ErrorResponse(ex.getMessage());
            }
        }

        if(request instanceof GetGamesRequest){
            System.out.println("Get Games request ...");
            GetGamesRequest getGamesRequest = (GetGamesRequest) request;
            try{
                List<Game> listGames = (List<Game>) server.findAllGames();
                Game[] games = listGames.toArray(Game[]::new);
                GameDTO[] gameDTOS =UtilsDTO.getDTO(games);

                return new GetGamesResponse(gameDTOS);
            }catch (Exception ex){
                return new ErrorResponse(ex.getMessage());
            }
        }

        if(request instanceof BuyTicketRequest){
            System.out.println("Buy ticket request ... ");
            BuyTicketRequest buyTicketRequest = (BuyTicketRequest) request;
            GameTicketDTO gameTicketDTO = buyTicketRequest.getGameTicketDTO();
            GameTicket gameTicket = UtilsDTO.getFromDTO(gameTicketDTO);
            try{
                server.buyTicket(gameTicket);
                return new OkResponse();
            }catch (Exception ex){
                return new ErrorResponse(ex.getMessage());
            }
        }
        return response;
    }
    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse((Response) response);
                }
            } catch (IOException e) {
                //e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }
}
