package rest;

/**
 * Created by gantz on 27.02.16.
 */
public class Id {
    long id;

    public Id() {
        id = 0;
    }

    public Id(long id) {

        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
