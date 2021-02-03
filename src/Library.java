import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Library implements Serializable {
    private HashMap<String, Book> allBooks; // tänker att ISBN eller Titel är ID
    private HashMap<String, Book> allAvailableBooks; // skulle också kunna vara en ArrayList?
    private HashMap<String, Book> allBorrowedBooks;
    private HashMap<Integer, Borrower> allBorrowers; // nu är key lånekortsnummer, kan behöva ändras
    private HashMap<String, Librarian> allLibrarians;
    private static Library library = null;

    private Library() {
        allBooks = new HashMap<>();
        allAvailableBooks = new HashMap<>();
        allBorrowedBooks = new HashMap<>();
        allBorrowers = new HashMap<>();
        allLibrarians = new HashMap<>();
    }

    // for testing purposes only
    public void addBookDirty(Book book) { //For testing purposes only
        allBooks.put(book.getTitle(), book);
    }

    public void addBookWithDialog() {
        System.out.println("ADD A BOOK TO THE LIBRARY");

        System.out.println("Enter title: ");
        String title = Helpers.readUserString();
        System.out.println("Enter author's first name: ");
        String authorFName = Helpers.readUserString();
        System.out.println("Enter author's last name");
        String authorLName = Helpers.readUserString();
        System.out.println("Enter ISBN: ");
        String isbn = Helpers.readUserString();
        System.out.println("Enter Description: ");
        String description = Helpers.readUserString();

        Book newBook = new Book(title, new Author(authorFName, authorLName), isbn, description);
        allBooks.put(newBook.getTitle(), newBook);
    }

    public void removeBookWithDialog() {
        System.out.println("REMOVE A BOOK FROM THE LIBRARY");
        System.out.println("Enter title of book you want to remove:");
        String bookTitle = Helpers.readUserString();
        if (allBooks.containsKey(bookTitle)) {
            if (allBooks.get(bookTitle).isAvailable()) {
                allBooks.get(bookTitle).getAuthor().removeFromList(allBooks.get(bookTitle));
                allBooks.remove(bookTitle);
                allAvailableBooks.remove(bookTitle);

                System.out.println(bookTitle + " has been removed from the library.");
            } else {
                System.out.println(bookTitle + " is currently borrowed and cannot be removed from library.");
            }
        } else {
            System.out.println("Book was not found in library.");
        }
    }

    public void getAllBooks() {
        for (Book book : allBooks.values()
        ) {
            System.out.println(book);
        }
    }

    public void getAllAvailableBooks() {
        System.out.println("ALL AVAILABLE BOOKS:");
        for (Book book : allAvailableBooks.values()) {
            System.out.println("\n"
                    + book.getTitle() + " by "
                    + book.getAuthor().toString() + ", described as \""
                    + book.getBookDescription() + "\"");

        }
    }

    public void getAllBorrowedBooks() {
        System.out.println("ALL BORROWED BOOKS:");
        for (Book book : allBorrowedBooks.values()) {
            System.out.println("\n"
                    + book.getTitle() + " by "
                    + book.getAuthor().toString() + " is borrowed by "
                    + book.getMyBorrower().getLibraryCardNumber() + " and is due back "
                    + book.getReturnDate());
        }
    }

    public static Library getLibrary() {
        if (library == null) {
            library = new Library();
            if (Files.exists(Paths.get("LibraryFile.ser"))) {
                library = (Library) FileUtil.readObjectFromFile("LibraryFile.ser");
            }
        }
        return library;
    }

    public void findBookByAuthor() {
        System.out.println("Enter the author of the book:");

        // Reads user input, make all characters to lower case and then removes all special characters, including dots and spaces in beginning of the String
        // Needs to be chained like this so it can be effectively final, otherwise if we do this in multiple steps, we would have to make a temp String to use in our lambda
        String userSearchPhrase = Helpers.readUserString().toLowerCase().replaceAll("^[\\W]+", "");

        if (!userSearchPhrase.isEmpty()) {                                                      // If the string is not empty after it has been trimmed, then the code under will run
            allBooks.entrySet().stream()
                    .filter(stringBookEntry -> stringBookEntry.getValue().getAuthor().toString().toLowerCase().contains(userSearchPhrase))
                    .forEach(stringBookEntry -> System.out.println("BOOK: " + stringBookEntry.getValue().getTitle() +
                            " AUTHOR: " + stringBookEntry.getValue().getAuthor().toString()));
        } else {
            System.out.println("No author is found.");
        }
    }

    // Does the same as findBookByAuthor() but in a different way and searches books by title, just to try both ways.
    // Johan said that i could keep both methods
    public void findBookByTitleOrISBN() {
        System.out.println("Enter the title or ISBN of the book:");
        String userSearchPhrase = Helpers.readUserString().replaceAll("^[\\W]+", "");

        if (!userSearchPhrase.isEmpty()) {                                                      // If the string is not empty after it has been trimmed, then the code under will run

            Pattern pattern = Pattern.compile(userSearchPhrase, Pattern.CASE_INSENSITIVE);
            allBooks.forEach((s, book) -> {
                Matcher matcher = pattern.matcher(book.getTitle());
                Matcher matcher2 = pattern.matcher(book.getIsbn());
                if (matcher.find() || matcher2.find()) {
                    System.out.println("BOOK: " + book.getTitle());
                }
            });

        } else {
            System.out.println("Title or ISBN not found.");
        }
    }

    public void sortBooksByTitle() {
        allBooks.entrySet().stream()
                .sorted(Comparator.comparing(b -> b.getValue().getTitle()))
                .forEach(System.out::println);
    }

    public void sortBooksByAuthor() {
        allBooks.entrySet().stream()
                .sorted(Comparator.comparing(b -> b.getValue().getAuthor().getLastName()))
                .forEach(System.out::println);
    }

    public void addLibrarianToLibrary(Librarian librarian) {
        allLibrarians.put(librarian.getName(), librarian);
    }

    public void addBorrowerToLibrary(Borrower borrower) {
        allBorrowers.put(borrower.getLibraryCardNumber(), borrower);
    }

    public HashMap<Integer, Borrower> getAllBorrowers() {
        return allBorrowers;
    }

    public HashMap<String, Librarian> getAllLibrarians() {
        return allLibrarians;
    }
}