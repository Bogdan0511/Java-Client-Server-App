package user.interfaces;

import crud_repository.CrudRepository;
import user.User;

import java.util.UUID;

public interface UserRepository extends CrudRepository<UUID, User> {

    @Override
    default void add(User entity) {

    }

    @Override
    default void delete(UUID entityID) {

    }

    @Override
    default void update(User newEntity, UUID entityID) {

    }

    @Override
    default Iterable<User> findAll() {
        return null;
    }

    @Override
    default User findOne(UUID entityID) {
        return null;
    }
}
