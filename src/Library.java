import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Library implements Serializable {
    private HashMap<String, Book> allBooks; // tänker att ISBN eller Titel är ID
    private HashMap<String, Book> allAvailableBooks; // skulle också kunna vara en ArrayList?
    private HashMap<String, Book> allBorrowedBooks;
    private HashMap<String, Borrower> allBorrowers;
    private HashMap<String, Librarian> allLibrerians;
    private static Library library = null;

    private Library() {
        allBooks = new HashMap<>();
        allAvailableBooks = new HashMap<String, Book>();
        allBorrowedBooks = new HashMap<String, Book>();
        allBorrowers = new HashMap<String, Borrower>();
        allLibrerians = new HashMap<String, Librarian>();
    }

    public void addBook(Book book) {
        allBooks.put(book.getTitle(), book);
    }

    //here we are going to iterate through each of the book inside our list of books
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


    public static Library getLibrary() {
        if (library == null) {
            library = new Library();
        }
        return library;
    }

    public void findBookByTitle() {
        System.out.println("Enter the title of the book");
        String userSearchPhrase = Helpers.readUserString();
        userSearchPhrase = userSearchPhrase.trim();
        userSearchPhrase = userSearchPhrase.replaceAll("^[\\.]+", "");
        System.out.println(userSearchPhrase);
        if (!userSearchPhrase.isEmpty()) {

            try {
                Pattern pattern = Pattern.compile(userSearchPhrase, Pattern.CASE_INSENSITIVE);
                allBooks.forEach((s, book) -> {
                    Matcher matcher = pattern.matcher(book.getTitle());
                    if (matcher.find()) {
                        System.out.println(book.getTitle());
                    }
                });
            } catch (Exception e) {

                System.out.println("\nERROR: You can only input characters and numbers");
            }
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