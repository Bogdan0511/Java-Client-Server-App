package basketball.interfaces;



import basketball.Team;
import crud_repository.CrudRepository;

import java.util.UUID;

public interface TeamRepository extends CrudRepository<UUID, Team> {
}
