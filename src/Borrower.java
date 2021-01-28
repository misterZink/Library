import java.util.ArrayList;
import java.util.List;

public class Borrower extends User {

    private String name;
    private int libraryCardNumber;
    List<Book> borrowedBooks = new ArrayList<>();

    public Borrower() {
    }

    public Borrower(String name, int libraryCardNumber) {
        this.name = name;
        this.libraryCardNumber = libraryCardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
