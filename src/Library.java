import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public static Library getLibrary() {
        if (library == null) {
            library = new Library();
            if (Files.exists(Paths.get("LibraryFile.ser"))) {

                // If file exist then read from file
                library = (Library) FileUtil.readObjectFromFile("LibraryFile.ser");
            } else {

                // Otherwise add some books to the library
                library.initBooks();
            }
        }
        return library;
    }


    // This method is called if the file LibraryFile does not exists. So that the library always have some books.
    private void initBooks() {
        Author jrTolkien = new Author("J. R. R.", "Tolkien");
        allBooks.put("The lord of the rings - The fellowship of the ring", new Book("The lord of the rings - The fellowship of the ring", jrTolkien, "9172632186", "The first book of the trilogy The lord of the rings."));
        allBooks.put("The lord of the rings - The two towers", new Book("The lord of the rings - The two towers", jrTolkien, "9789172632196", "The second book of the trilogy The lord of the rings."));
        allBooks.put("The lord of the rings - The return of the king", new Book("The lord of the rings - The return of the king", jrTolkien, "9789119129710", "The third book of the trilogy The lord of the rings."));
        allBooks.put("The Hobbit: Or There and Back Again", new Book("The Hobbit: Or There and Back Again", jrTolkien, "9789113084893", "The hobbit is a book about Bilbo and how he got the ring."));
        allBooks.put("The Great Gatsby", new Book("The Great Gatsby", new Author("F Scott", "Fitzgerald"), "9781847496140", "Tells a story about one man's pursuit of the American dream."));
        allBooks.put("Invisible Man", new Book("Invisible Man", new Author("Ralph", "Ellison"), "9780241970560", "Invisible Man is the story of a young black man from the South who does not fully understand racism in the world."));
        allBooks.put("Anna Karenina", new Book("Anna Karenina", new Author("Lev", "Tolstoj"), "9789113079943", "This book is about a russian lady."));
        allBooks.put("Hamlet", new Book("Hamlet", new Author("William", "Shakespeare"), "9780007902347", "To be, or not to be, that is the question."));
        allBooks.put("Moby Dick", new Book("Moby Dick", new Author("Herman", "Melville"), "9780199535729", "Moby dick is a famous book."));
        allBooks.put("To Kill A Mockingbird", new Book("To Kill A Mockingbird", new Author("Harper", "Lee"), "9780099419785", "To Kill a Mockingbird is a novel by the American author Harper Lee. It was published in 1960 and, instantly successful in the United States."));

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

    public <T> void showBooks(HashMap<String, T> hashMap) {
        HashMap<Integer, T> numberedHashMap = Helpers.createNumberedHashMap(hashMap);
        numberedHashMap.forEach((k, v) -> System.out.println("\n" + k + ". " + v.toString()));
    }

    public void findBookByAuthor() {
        System.out.println("Enter the author of the book:");

        // Reads user input, make all characters to lower case and then removes all special characters, including dots and spaces in beginning of the String
        // Needs to be chained like this so it can be effectively final, otherwise if we do this in multiple steps, we would have to make a temp String to use in our lambda
        String userSearchPhrase = Helpers.readUserString().toLowerCase().replaceAll("^[\\W]+", "");

        if (!userSearchPhrase.isEmpty()) {                                                      // If the string is not empty after it has been trimmed, then the code under will run
            List<Book> foundBooks = allBooks.values().stream()
                    .filter(stringBookEntry -> stringBookEntry.getAuthor().toString().toLowerCase().contains(userSearchPhrase))
                    .collect(Collectors.toList());
            if (foundBooks.size() > 0) {
                HashMap<Integer, Book> foundBooksWithNumbers = Helpers.createNumberedHashMapFromList(foundBooks);
                System.out.println("Books by " + foundBooks.get(0).getAuthor().toString());
                foundBooksWithNumbers.forEach((k, v) -> System.out.println(k + ". " + v.getTitle()));
            } else {
                System.out.println("No author is found.");
            }
        } else {
            System.out.println("No author is found.");
        }
    }

    // Does the same as findBookByAuthor() but in a different way and searches books by title, just to try both ways.
    // Johan said that i could keep both methods
    public void findBookByTitleOrISBN() {
        System.out.println("Enter the title or ISBN of the book:");
        String userSearchPhrase = Helpers.readUserString().replaceAll("^[\\W]+", "");
        List<Book> foundBooks = new ArrayList<>();

        if (!userSearchPhrase.isEmpty()) { // If the string is not empty after it has been trimmed, then the code under will run
            Pattern pattern = Pattern.compile(userSearchPhrase, Pattern.CASE_INSENSITIVE);
            allBooks.forEach((s, book) -> {
                Matcher matcher = pattern.matcher(book.getTitle());
                Matcher matcher2 = pattern.matcher(book.getIsbn());
                if (matcher.find() || matcher2.find()) {
                    foundBooks.add(book);
                }
            });
            if (foundBooks.size() > 0) {
                HashMap<Integer, Book> foundBooksWithNumbers = Helpers.createNumberedHashMapFromList(foundBooks);
                System.out.println("\nBooks that match your search:");
                foundBooksWithNumbers.forEach((k, v) -> System.out.println(k + ". " + v.toString()));
            } else {
                System.out.println("Title or ISBN not found.");
            }
        } else {
            System.out.println("Title or ISBN not found.");
        }
    }

    public void sortBooks(String sortingOn) {
        switch (sortingOn) {
            case "title" -> allBooks.entrySet().stream()
                    .sorted(Comparator.comparing(b -> b.getValue().getTitle()))
                    .forEach(System.out::println);
            case "author" -> allBooks.entrySet().stream()
                    .sorted(Comparator.comparing(b -> b.getValue().getAuthor().getLastName()))
                    .forEach(System.out::println);
        }
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

    public HashMap<String, Book> getAllBooks() {
        return allBooks;
    }

    public HashMap<String, Book> getAllAvailableBooks() {
        return allAvailableBooks;
    }

    public HashMap<String, Book> getAllBorrowedBooks() {
        return allBorrowedBooks;
    }
}