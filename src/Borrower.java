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

    public void showMyBorrowedBooks(boolean isLibrarian) {
        if (myBorrowedBooks.size() > 0) {
            int printListnumber = 1;
            for (Book book : myBorrowedBooks) {
                Helpers.printInMenuColors(printListnumber++ + ".");
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
            if (!isLibrarian) {
                readWhatBookToReturn();
            }
        } else {
            System.out.println("You have no borrowed books.");
        }
    }

    private void readWhatBookToReturn() {
        Helpers.printInMenuColors("If you want to return a book, enter its number." +
                "\nTo return to the main menu, enter 0\n");
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
        book.returnMe();
        System.out.println("You haven now returned the book: " + ConsoleColor.GREEN + book.getTitle() + ConsoleColor.RESET);
    }

    public void setLibraryCardNumber(int libraryCardNumber) {
        this.libraryCardNumber = libraryCardNumber;
    }

    public int getLibraryCardNumber() {
        return libraryCardNumber;
    }
}
