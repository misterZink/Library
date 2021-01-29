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

    //added our method addBook
    public void addBook(Book book) {
        allBooks.put(book.getTitle(), book);
    }
    //implement toString method
    //here we are going to iterate through each of the book inside our list of books

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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the title of the book");
        String userSearchPhrase = scanner.nextLine();
        if (!userSearchPhrase.equals(".") && !userSearchPhrase.isEmpty() ) {
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
        }

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
    }*/