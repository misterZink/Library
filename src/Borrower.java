import java.util.ArrayList;
import java.util.List;

public class Borrower extends User {
    private int libraryCardNumber;
    List<Book> borrowedBooks = new ArrayList<>();

    public Borrower() {
        super(false, "default");
    }

    public Borrower(String name, int libraryCardNumber) {
        super(false, name);
        this.libraryCardNumber = libraryCardNumber;
    }

    public int getLibraryCardNumber() {
        return libraryCardNumber;
    }
}
