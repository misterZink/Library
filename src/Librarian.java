import java.io.Serializable;

public class Librarian extends User implements Serializable {

    public Librarian() {
        super(true, "default");
    }

    public Librarian(String name) {
        super(true, name);
    };
}
