import java.util.ArrayList;
import java.util.List;

public class Borrower {

    /*
    Skapad av Robin Heidari, 2021-01-27
    
     */

    private String name;
    private int libraryCardNumber;
    List<Book> borrowedBooks = new ArrayList<>();

    public Borrower() {
    }

    public Borrower(String name, int libraryCardNumber) {
        this.name = name;
        this.libraryCardNumber = libraryCardNumber;
    }
}
