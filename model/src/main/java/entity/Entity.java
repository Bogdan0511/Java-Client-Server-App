package entity;

import java.io.Serializable;

public class Entity<ID> implements Serializable {
    private ID entityID;

    protected void setEntityID(final ID _entityID){
        this.entityID = _entityID;
    }

    protected ID getEntityID(){
        return this.entityID;
    }

}
