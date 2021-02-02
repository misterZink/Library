import java.io.Serializable;
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
        allAvailableBooks = new HashMap<String, Book>();
        allBorrowedBooks = new HashMap<String, Book>();
        allBorrowers = new HashMap<Integer, Borrower>();
        allLibrarians = new HashMap<String, Librarian>();
    }

    public void addBook(Book book) {
        allBooks.put(book.getTitle(), book);
    }

    // Den här metoden tar inte bort böcker?
    public void removeBook(Book book) {
        allBooks.put(book.getTitle(), book);
    }

    public void getAllBooks() {
        for (Book book : allBooks.values()
        ) {
            System.out.println(book);
        }
    }

    public void getAllAvailableBooks(){
        System.out.println("ALL AVAILABLE BOOKS:");
        for (Book book : allAvailableBooks.values()){
            System.out.println("\n"
                    + book.getTitle() + " by "
                    + book.getAuthor() + ", described as \""
                    + book.getBookDescription() + "\"");

        }
    }

    public void getAllBorrowedBooks(){
        System.out.println("ALL BORROWED BOOKS:");
        for (Book book : allBorrowedBooks.values()){
            System.out.println("\n"
                    + book.getTitle() + " by "
                    + book.getAuthor() + " is borrowed by "
                    + book.getMyBorrower().getLibraryCardNumber() + " and is due back "
                    + book.getReturnDate());
        }
    }

    public static Library getLibrary() {
        if (library == null) {
            library = new Library();
        }
        return library;
    }

    public void findBookByAuthor() {
        System.out.println("Enter the author of the book");

        // Reads user input, make all characters to lower case and then removes all special characters, including dots and spaces in beginning of the String
        // Needs to be chained like this so it can be effectively final, otherwise if we do this in multiple steps, we would have to make a temp String to use in our lambda
        String userSearchPhrase = Helpers.readUserString().toLowerCase().replaceAll("^[\\W]+", "");

        if (!userSearchPhrase.isEmpty()) {                                                      // If the string is not empty after it has been trimmed, then the code under will run
            System.out.println("");
            allBooks.entrySet().stream()
                    .filter(stringBookEntry -> stringBookEntry.getValue().getAuthor().getFullName().toLowerCase().contains(userSearchPhrase))
                    .forEach(stringBookEntry -> System.out.println("BOOK: " + stringBookEntry.getValue().getTitle() +
                            " AUTHOR: " + stringBookEntry.getValue().getAuthor().getFullName()));
        } else {
            System.out.println("No author is found");
        }
    }

    // Does the same as findBookByAuthor() but in a different way and searches books by title, just to try both ways.
    // Johan said that i could keep both methods
    public void findBookByTitle() {
        System.out.println("Enter the title of the book");
        String userSearchPhrase = Helpers.readUserString().replaceAll("^[\\W]+", "");

        if (!userSearchPhrase.isEmpty()) {                                                      // If the string is not empty after it has been trimmed, then the code under will run

            Pattern pattern = Pattern.compile(userSearchPhrase, Pattern.CASE_INSENSITIVE);
            allBooks.forEach((s, book) -> {
                Matcher matcher = pattern.matcher(book.getTitle());
                if (matcher.find()) {
                    System.out.println("BOOK: " + book.getTitle());
                }
            });

        } else {
            System.out.println("You need to input a title");
        }
    }

    public void sortBooksByTitle() {
        allBooks.entrySet().stream()
                .sorted((b1, b2) -> b1.getValue().getTitle()
                        .compareTo(b2.getValue().getTitle()))
                .forEach(System.out::println);
    }

    public void sortBooksByAuthor() {
        allBooks.entrySet().stream()
                .sorted((b1, b2) -> b1.getValue().getAuthor().getLastName()
                        .compareTo(b2.getValue().getAuthor().getLastName()))
                .forEach(System.out::println);
    }

    public void addLibrarianToLibrary(Librarian librarian){
        allLibrarians.put(librarian.getName(), librarian);
    }

    public void addBorrowerToLibrary(Borrower borrower){
        allBorrowers.put(borrower.getLibraryCardNumber(), borrower);
    }

    public HashMap<Integer, Borrower> getAllBorrowers() {
        return allBorrowers;
    }

    public HashMap<String, Librarian> getAllLibrarians() {
        return allLibrarians;
    }
}

// add a book to our library
   /* private static void addBook() {
        String isbn;
        String title;
        String author;
        String description;
        System.out.println("\nEnter title: ");
        title = in.next();
        System.out.println("\nEnter Author: ");
        author = in.next();
        System.out.println("\nEnter ISBN: ");
        isbn = in.next();
        System.out.println("\nEnter Description: ");
        description = in.next();

        Book b = new Book(title, author, isbn, description);
        lib.addBook(b);
    }

//remove a book from our library
private static void removeBook(){
    String title;
    System.out.println("\nEnter  books title to remove it: ");
    title = in.next();
    Book b = new Book(title);
    lib.removeBook(b);
}*/