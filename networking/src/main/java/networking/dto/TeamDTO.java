package networking.dto;

import java.io.Serializable;
import java.util.UUID;

public class TeamDTO implements Serializable {
    private UUID id;
    private String name;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TeamDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
