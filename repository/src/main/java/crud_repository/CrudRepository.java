package crud_repository;


import entity.Entity;

public interface CrudRepository<ID, E extends Entity<ID>> {
    void add(E entity);
    void delete(ID entityID);
    void update(E newEntity, ID entityID);
    Iterable<E> findAll();
    E findOne(ID entityID);
}
