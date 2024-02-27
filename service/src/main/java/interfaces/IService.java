package interfaces;

import basketball.Game;
import basketball.GameTicket;
import interfaces.IObserver;
import user.User;

public interface IService{
    void login(User userToLogin, IObserver client) throws Exception;
    void logout(User user, IObserver client) throws Exception;
    Iterable<Game> findAllGames() throws Exception;
    void buyTicket(GameTicket ticket) throws Exception;
}
