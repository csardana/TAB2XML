package models.measure.attributes;

public class Key {
    public int fifths;

    // Default Constructor:
    public Key() {
		super();
	}

	public Key(int fifths) {
        this.fifths = fifths;
    }

    public int getFifths() {
        return fifths;
    }

    public void setFifths(int fifths) {
        this.fifths = fifths;
    }
}
