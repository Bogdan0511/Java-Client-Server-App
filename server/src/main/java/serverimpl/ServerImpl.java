package serverimpl;

import basketball.Game;
import basketball.GameTicket;
import basketball.interfaces.GameRepository;
import basketball.interfaces.GameTicketRepository;
import basketball.interfaces.TeamRepository;
import interfaces.IObserver;
import interfaces.IService;
import user.User;
import user.interfaces.UserRepository;

import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerImpl implements IService {

    private UserRepository userRepository;
    private TeamRepository teamRepository;
    private GameRepository gameRepository;
    private GameTicketRepository gameTicketRepository;

    private Map<UUID,IObserver> loggedClients;

    private final int defaultThreadsNo=5;

    public ServerImpl(UserRepository userRepository, TeamRepository teamRepository, GameRepository gameRepository, GameTicketRepository gameTicketRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
        this.gameTicketRepository = gameTicketRepository;

        loggedClients = new ConcurrentHashMap<>();
    }

    private User findUserByEmail(String userEmail) throws Exception {
        User user = ((List<User>) userRepository.findAll()).
                                                    stream().
                                                        filter(u -> u.getUserEmail().equals(userEmail)).
                                                            findFirst().
                                                                orElse(null);

        if(user == null)
            throw new Exception("Could not find an user with this email");

        return user;
    }

    @Override
    public synchronized void login(User userToLogin, IObserver client) throws Exception{
        User foundUser = findUserByEmail(userToLogin.getUserEmail());

        if(!userToLogin.getPassword().equals(foundUser.getPassword()))
            throw new Exception("Wrong email/password");

        System.out.println(loggedClients.keySet());
        System.out.println(foundUser);

        if(loggedClients.containsKey(foundUser.getUserID()))
            throw new Exception("User already logged in");


        loggedClients.put(foundUser.getUserID(),client);
        notifyUserLoggedIn(userToLogin);
    }

    private void notifyUserLoggedIn(User user) throws Exception{
        Iterable<User> users = userRepository.findAll();
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(User u: users){
            if (loggedClients.containsKey(u.getUserID())) {
                IObserver client = loggedClients.get(u.getUserID());
                System.out.println(client);
                if (!u.getUserEmail().equals(user.getUserEmail())) {
                    executor.execute(() -> {
                        try {
                            System.out.println("Notifying [" + u.getUserEmail() + "] user [" + user.getUserEmail() + "] logged in ...");
                            client.userLoggedIn(user);
                        } catch (Exception ex) {
                            System.out.println("Error notyfing user that an user has logged in" + ex);
                        }
                    });
                }
            }
        }
    }

    @Override
    public synchronized void logout(User user, IObserver client) throws Exception {
        User foundUser = findUserByEmail(user.getUserEmail());
        IObserver localClient =loggedClients.remove(foundUser.getUserID());
        if(localClient == null)
            throw new Exception("User " + user.getUserEmail() + " is not logged in");
        notifyUserLoggedOut(user);
    }

    private void notifyUserLoggedOut(User user){
        Iterable<User> users = userRepository.findAll();
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(User u: users){
            if (loggedClients.containsKey(u.getUserID())) {
                IObserver client = loggedClients.get(u.getUserID());
                System.out.println(client);
                if (!u.getUserEmail().equals(user.getUserEmail())) {
                    executor.execute(() -> {
                        try {
                            System.out.println("Notifying [" + u.getUserEmail() + "] user [" + user.getUserEmail() + "] logged out ...");
                            client.userLoggedOut(user);
                        } catch (Exception ex) {
                            System.out.println("Error notyfing user that an user has logged out" + ex);
                        }
                    });
                }
            }
        }
    }

    @Override
    public synchronized Iterable<Game> findAllGames() {
        System.out.println("Finding all games");
        return gameRepository.findAll();
    }

    @Override
    public synchronized void buyTicket(GameTicket ticket) throws Exception {
        Game game = gameRepository.findOne(ticket.getGame().getGameID());
        if(game.getAvailableSeats() < ticket.getNumberOfSeats())
            throw new Exception("Not enough seats available for this game");

        game.setAvailableSeats(game.getAvailableSeats() - ticket.getNumberOfSeats());
        gameRepository.update(game,game.getGameID());
        gameTicketRepository.add(ticket);
        notifyBoughtTicket(ticket);
    }

    private void notifyBoughtTicket(GameTicket gameTicket){
        Iterable<User> users = userRepository.findAll();
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(User u: users){
            IObserver client = loggedClients.get(u.getUserID());
            if(client != null ){
                executor.execute(()->{
                    try{
                        System.out.println("Notifying [" + u.getUserEmail() + "] that a ticket has been bought at game " + gameTicket.getGame() );
                        client.userBoughtTicket(gameTicket);
                    }catch (Exception ex){
                        System.out.println("Error notyfing users that a ticket has been bought" + ex);
                    }
                });
            }
        }
    }
}
