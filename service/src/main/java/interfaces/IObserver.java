package interfaces;

import basketball.GameTicket;
import user.User;

public interface IObserver {
    void userLoggedIn(User userLoggedIn);
    void userLoggedOut(User userLoggedOut);
    void userBoughtTicket(GameTicket gameTicket);
}
