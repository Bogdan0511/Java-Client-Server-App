package basketball.interfaces;

import basketball.GameTicket;
import crud_repository.CrudRepository;

import java.util.UUID;

public interface GameTicketRepository extends CrudRepository<UUID, GameTicket> {

}

