import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @Override
    public String toString() {
        return "Name: " + getName() + "\n" +
                "LibraryCardNumber: " + getLibraryCardNumber() + "\n" +
                "Username: " + getUsername() + "\n" +
                "Password: " + getPassword() + "\n" +  "Books borrowed: " + myBorrowedBooks.size();
    }

    public void showMyBorrowedBooks() {
        if (myBorrowedBooks.size() > 0) {
            for (Book book : myBorrowedBooks) {
                System.out.println("\n"
                        + book.getTitle() + " by "
                        + book.getAuthor().toString() + " is due back "
                        + book.getReturnDate()
                        + " ( "
                        + ChronoUnit.DAYS.between(LocalDate.now(), book.getReturnDate())     // Returns how many days are between now and the return date
                        + " days left )");
            }
        }
        else {
            System.out.println("You have no borrowed books.");
        }
    }

    public void setLibraryCardNumber(int libraryCardNumber) {
        this.libraryCardNumber = libraryCardNumber;
    }

    public int getLibraryCardNumber() {
        return libraryCardNumber;
    }
}
