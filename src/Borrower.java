import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

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

    public void addToMyBorrowedBooks(Book book) {
        myBorrowedBooks.add(book);
    }

    @Override
    public String toString() {
        return "Name: " + getName() + "\n" +
                "LibraryCardNumber: " + getLibraryCardNumber() + "\n" +
                "Username: " + getUsername() + "\n" +
                "Books borrowed: " + myBorrowedBooks.size();
    }

    public void showMyBorrowedBooks(boolean showBooksForLibrarian) {
        if (myBorrowedBooks.size() > 0) {
            int printListnumber = 1;
            for (Book book : myBorrowedBooks) {
                System.out.print(ConsoleColor.MAGENTA_BOLD_BRIGHT + "\n" + printListnumber++ + ". " + ConsoleColor.RESET);
                if (!LocalDate.now().isBefore(book.getReturnDate())) {
                    System.out.print(ConsoleColor.RED_BOLD);
                }
                System.out.println(book.getTitle()
                        + " by "
                        + book.getAuthor().toString() + " is due back "
                        + book.getReturnDate()
                        + " ( "
                        + DAYS.between(LocalDate.now(), book.getReturnDate())     // Returns how many days are between now and the return date
                        + " days left )" + ConsoleColor.RESET);
            }
            if (!showBooksForLibrarian) {
                readWhatBookToReturn();
            }
        } else {
            System.out.println("You have no borrowed books.");
        }
    }

    private void readWhatBookToReturn() {
        System.out.println("\n" + ConsoleColor.BLACK_BACKGROUND_BRIGHT + "" + ConsoleColor.MAGENTA_BOLD_BRIGHT +
                "If you want to return a book, enter its number. " +
                "\nTo return to the main menu, enter 0");
        int returnBookInt = Helpers.readUserInt(-1, myBorrowedBooks.size() + 1) - 1;

        if (returnBookInt == -1) {
            return;
        } else {
            returnBook(myBorrowedBooks.get(returnBookInt), returnBookInt);
        }
    }

    private void returnBook(Book book, int index) {
        Library library = Library.getLibrary();

        library.removeBookFromAllBorrowedBooks(book);
        library.addBookToAllAvailableBooks(book);
        myBorrowedBooks.remove(index);
        book.setMyBorrower(null);
        book.setReturnDate(null);
        book.setAvailable(true);
        System.out.println("You haven now returned the book: " + ConsoleColor.GREEN + book.getTitle() + ConsoleColor.RESET);
    }

    public void setLibraryCardNumber(int libraryCardNumber) {
        this.libraryCardNumber = libraryCardNumber;
    }

    public int getLibraryCardNumber() {
        return libraryCardNumber;
    }
}
