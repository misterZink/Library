import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Library implements Serializable {
    private static HashMap<String, Book> allBooks;
    private HashMap<String, Book> allAvailableBooks;
    private HashMap<String, Book> allBorrowedBooks;
    private HashMap<Integer, Borrower> allBorrowers; // lånekortsnummer är key
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
                initiateLibraryBooks();
            }
        }
        return library;
    }

    private static void initiateLibraryBooks(){
        List<String> booksFromFile = FileUtil.readTextFromFile("src/libraryBooks.txt");
        List<String[]> booksSplitIntoArrays = FileUtil.splitList(booksFromFile);
        for (String[] bookArray : booksSplitIntoArrays) {
            allBooks.put(bookArray[0], 
                    new Book(bookArray[0],
                            new Author(bookArray[1], bookArray[2]),
                    bookArray[3], bookArray[4]));
        }
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

    public <T> HashMap<Integer, T> showBooks(HashMap<String, T> hashMap) {
        HashMap<Integer, T> numberedHashMap = Helpers.createNumberedHashMap(hashMap);
        numberedHashMap.forEach((k, v) -> System.out.println("\n" + k + ". " + v.toString()));
        return numberedHashMap;
    }

    public <T> void showAllUsersInList(HashMap<?, T> userList) {
        if (userList != null && userList.size() > 0) {
            userList.forEach((s, u) -> System.out.println(u));
        }
    }

    public HashMap<Integer, Book> findBookByAuthor() {
        System.out.println("Enter the author of the book:");
        String userSearchPhrase = searchPhraseInput();
        HashMap<Integer, Book> numberedHashMap = new HashMap<>();

        if (!userSearchPhrase.isEmpty()) {                                                      // If the string is not empty after it has been trimmed, then the code under will run
            List<Book> foundBooks = allBooks.values().stream()
                    .filter(stringBookEntry -> stringBookEntry.getAuthor().toString().toLowerCase().contains(userSearchPhrase))
                    .collect(Collectors.toList());
            if (foundBooks.size() > 0) {
                numberedHashMap = Helpers.createNumberedHashMapFromList(foundBooks);
                System.out.println("Books by " + foundBooks.get(0).getAuthor().toString());
                numberedHashMap.forEach((k, v) -> System.out.println(k + ". " + v.getTitle()));
            } else {
                System.out.println("No author is found.");
            }
        } else {
            System.out.println("No author is found.");
        }
        return numberedHashMap;
    }

    // Does the same as findBookByAuthor() but in a different way and searches books by title, just to try both ways.
    // Johan said that i could keep both methods
    public HashMap<Integer, Book> findBookByTitleOrISBN() {
        System.out.println("Enter the title or ISBN of the book:");
        List<Book> foundBooks = new ArrayList<>();
        String userSearchPhrase = searchPhraseInput();
        HashMap<Integer, Book> numberedHashMap = new HashMap<>();

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
                numberedHashMap = Helpers.createNumberedHashMapFromList(foundBooks);
                System.out.println("\nBooks that match your search:");
                numberedHashMap.forEach((k, v) -> System.out.println(k + ". " + v.toString()));
            } else {
                System.out.println("Title or ISBN not found.");
            }
        } else {
            System.out.println("Title or ISBN not found.");
        }
        return numberedHashMap;
    }

    public HashMap<Integer, Book> sortBooks(String sortBy) {
        HashMap<Integer, Book> numberedHashMap = new HashMap<>();
        switch (sortBy) {
            case "title" -> {
                List<Book> test = allBooks.values().stream()
                        .sorted(Comparator.comparing(Book::getTitle))
                        .collect(Collectors.toList());
                numberedHashMap = Helpers.createNumberedHashMapFromList(test);
                numberedHashMap.forEach((k, v) -> System.out.println(k + ". " + v.toString()));
            }
            case "author" -> {
                List<Book> test3 = allBooks.values().stream()
                        .sorted(Comparator.comparing(b -> b.getAuthor().getLastName()))
                        .collect(Collectors.toList());
                numberedHashMap = Helpers.createNumberedHashMapFromList(test3);
                numberedHashMap.forEach((k, v) -> System.out.println(k + ". " + v.toString()));
            }
        }
        return numberedHashMap;
    }

    public void findBorrowerByName() {
        System.out.println("Enter the name of the borrower:");
        String userSearchPhrase = searchPhraseInput();

        if (!userSearchPhrase.isEmpty()) {
            allBorrowers.entrySet().stream()
                    .filter(BorrowerEntry -> BorrowerEntry.getValue().getName().toLowerCase().contains(userSearchPhrase))
                    .forEach(BorrowerEntry -> {
                        System.out.println("\nUser: " + BorrowerEntry.getValue().getName());
                        if (BorrowerEntry.getValue().myBorrowedBooks.size() > 0) {
                            System.out.println("Borrowed books:");
                            BorrowerEntry.getValue().showMyBorrowedBooks();
                        }
                    });
        } else {
            System.out.println("No user found with that name.");
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
    // Reads user input, make all characters to lower case and then removes all special characters, including dots and spaces in beginning of the String
    // Needs to be chained like this so it can be effectively final, otherwise if we do this in multiple steps, we would have to make a temp String to use in our lambda

    public String searchPhraseInput() {
        return Helpers.readUserString().toLowerCase().replaceAll("^[\\W]+", "");
    }

    public void borrowBook(int keyOfBook, HashMap<Integer, Book> numberedHashMap, Borrower currentBorrower){
        if (keyOfBook > 0) {
            Book book = numberedHashMap.get(keyOfBook);
            book.setAvailable(false);
            book.setReturnDate();
            book.setMyBorrower(currentBorrower);
            allAvailableBooks.remove(book.getTitle());
            allBorrowedBooks.put(book.getTitle(), book);
            currentBorrower.addToMyBorrowedBooks(book);
            System.out.println(book.getTitle() + " is now yours until " + book.getReturnDate());
        }
    }
    public int readWhatBookToBorrow(int max) {
        System.out.println("If you want to borrow a book, enter its number. " +
                "\nTo return to the main menu, enter 0");
        return Helpers.readUserInt(-1, max);
    }
}