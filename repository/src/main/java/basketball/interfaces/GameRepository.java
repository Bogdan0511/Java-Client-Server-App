package basketball.interfaces;

import basketball.Game;
import crud_repository.CrudRepository;


import java.util.UUID;

public interface GameRepository extends CrudRepository<UUID, Game> {

}
