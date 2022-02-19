package models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = Identification.class)
public class Identification {
    Creator creator;

    public Identification(Creator creator) {
        this.creator = creator;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }
}
