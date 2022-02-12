package models;

public class Identification {
    Creator creator;
    
    // Default Constructor:
    public Identification() {
		super();
	}

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
