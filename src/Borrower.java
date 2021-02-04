import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Borrower extends User implements Serializable {
    private int libraryCardNumber;
    List<Book> myBorrowedBooks = new ArrayList<>();

    public Borrower() {
        super(false, "default");
    }

    public Borrower(String name, int libraryCardNumber) {
        super(false, name);
        this.libraryCardNumber = libraryCardNumber;
    }

    public void addToMyBorrowedBooks(Book book){
        myBorrowedBooks.add(book);
    }

    public void showMyBorrowedBooks() {
        if (myBorrowedBooks.size() > 0) {
            for (Book book : myBorrowedBooks) {
                System.out.println("\n"
                        + book.getTitle() + " by "
                        + book.getAuthor().toString() + " is due back "
                        + book.getReturnDate());
            }
        }
        else {
            System.out.println("You have no borrowed books.");
        }
    }

    public int getLibraryCardNumber() {
        return libraryCardNumber;
    }
}
