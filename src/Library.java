import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Library implements Serializable {
    private HashMap<String, Book> allBooks;
    private HashMap<String, Book> allAvailableBooks;
    private HashMap<String, Book> allBorrowedBooks;
    private HashMap<String, Borrower> allBorrowers;
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
                library = (Library) FileUtil.readObjectFromFile("LibraryFile.ser");
            } else {
                library.initiateLibrarianAndBorrowers();
                library.initiateLibraryBooks();
                library.initiateAvailableBooks();
            }
        }
        return library;
    }

    private void initiateLibrarianAndBorrowers() {
        Librarian librarian = new Librarian("Admin");
        librarian.setUsername("librarian");
        librarian.setPassword("lib");

        Borrower borrower1 = new Borrower("Robin", 123456);
        borrower1.setUsername("user1");
        borrower1.setPassword("Password1!");

        Borrower borrower2 = new Borrower("Ziggi", 654321);
        borrower2.setUsername("user2");
        borrower2.setPassword("Password2!");

        Borrower borrower3 = new Borrower("Kungen", 741852);
        borrower3.setUsername("user3");
        borrower3.setPassword("Password3!");

        allLibrarians.put(librarian.getUsername(), librarian);

        allBorrowers.put(borrower1.getUsername(), borrower1);
        allBorrowers.put(borrower2.getUsername(), borrower2);
        allBorrowers.put(borrower3.getUsername(), borrower3);

        // This method only to create a tempbook for presentation to show what happens if a book is due to return
        tempBookForPresentation(borrower1);

    }

    // This method is only created to demonstrate what happens if a book is due to return
    private void tempBookForPresentation(Borrower borrower) {
        Book tempBook = new Book("Head First Java",
                new Author("Kathy", "Sierra")
                , "9780596009205"
                , "An interactive guide to the fundamentals of the Java programming language utilizes icons, cartoons, " +
                "and numerous other visual aids to introduce the features and functions of Java and to teach the principles " +
                "of designing and writing Java programs."
                , false);

        tempBook.setAvailable(false);
        tempBook.setReturnDate(LocalDate.now());
        tempBook.setMyBorrower(borrower);
        tempBook.setTimesBorrowed(1);
        allBooks.put(tempBook.getTitle(), tempBook);
        allAvailableBooks.remove(tempBook.getTitle());
        allBorrowedBooks.put(tempBook.getTitle(), tempBook);
        borrower.addToMyBorrowedBooks(tempBook);
    }

    private void initiateLibraryBooks() {
        List<String> booksFromFile = FileUtil.readTextFromFile("src/libraryBooks.txt");
        List<String[]> booksSplitIntoArrays = FileUtil.splitList(booksFromFile);
        for (String[] bookArray : booksSplitIntoArrays) {
            allBooks.put(bookArray[0],
                    new Book(bookArray[0],
                            new Author(bookArray[1], bookArray[2]),
                            bookArray[3], bookArray[4], bookArray[5].equals("true")));
        }
    }

    private void initiateAvailableBooks() {
        for (Book book : allBooks.values()) {
            if (book.isAvailable()) {
                allAvailableBooks.put(book.getTitle(), book);
            }
        }
    }

    public void addBook() {
        System.out.println("Enter 0 at any point to cancel and return to main menu.");
        List<String> bookInfo = new ArrayList<>();
        for (String s : addBookDialog()) {
            System.out.println(s);
            String input = Helpers.readUserString();
            if (input.equals("0")) return;
            bookInfo.add(input);
        }
        allBooks.put(bookInfo.get(0), new Book(bookInfo.get(0), new Author(bookInfo.get(1),
                bookInfo.get(2)), bookInfo.get(3), bookInfo.get(4), bookInfo.get(5).equals("2")));
        System.out.println(bookInfo.get(0) + " has been successfully added to the library!");
    }

    private List<String> addBookDialog() {
        List<String> dialog = new ArrayList<>();
        dialog.add("Enter title: ");
        dialog.add("Enter author's first name: ");
        dialog.add("Enter author's last name: ");
        dialog.add("Enter ISBN: ");
        dialog.add("Enter Description: ");
        dialog.add("Enter 1 to set borrow time to 4 weeks.\n" +
                "Enter 2 to set borrow time to 2 weeks, for popular books.");
        return dialog;
    }

    // Need to write the whole title of the book and spelled correctly to be able to remove a book.
    // This is so that you cant write only a small word like "ring" because otherwise it would remove all books that had ring inside title.
    public void removeBookByTitle() {
        System.out.println("Enter title of the book you want to remove:");
        System.out.println("(or enter 0 to cancel and return to main menu.)");
        String bookTitle = Helpers.readUserString();
        if (bookTitle.equals("0")) return;

        if (allBooks.containsKey(bookTitle)) {
            if (allBooks.get(bookTitle).isAvailable()) {
                allBooks.get(bookTitle).getAuthor().removeFromList(allBooks.get(bookTitle));
                allBooks.remove(bookTitle);
                allAvailableBooks.remove(bookTitle);

                System.out.println(bookTitle + " has been removed from the library.");
            } else {
                Helpers.printWarning(bookTitle + " is currently borrowed and cannot be removed from library.");
            }
        } else {
            System.out.println("Book was not found in library.");
        }
    }

    public void printHashMap(HashMap<Integer, Book> numberedHashMap, String version, boolean isLibrarian) {
        numberedHashMap.forEach((k, v) -> {
            Helpers.printInMenuColors(k.toString() + ".");
            System.out.print(v.toString(version, isLibrarian));
        });
    }

    public void printHashMap(HashMap<Integer, Book> numberedHashMap) {
        numberedHashMap.forEach((k, v) -> {
            Helpers.printInMenuColors(k.toString() + ".");
            System.out.print(v.toString());
        });
    }

    public HashMap<Integer, Book> showBooks(HashMap<String, Book> hashMap, String version, boolean isLibrarian) {
        HashMap<Integer, Book> numberedHashMap = Helpers.createNumberedHashMap(hashMap);
        if (numberedHashMap.size() == 0) System.out.println("There are no " + version + " books at the moment.");
        printHashMap(numberedHashMap, version, isLibrarian);
        return numberedHashMap;
    }

    public HashMap<Integer, Book> showPopularBooks(HashMap<String, Book> hashMap) {
        HashMap<String, Book> tempHashMap = new HashMap<>();
        hashMap.forEach((s, book) -> {
            if (book.isPopular()) {
                tempHashMap.put(book.getTitle(), book);
            }
        });
        return showBooks(tempHashMap, "popular", false);
    }

    public <T> void showAllUsersInList(HashMap<?, T> userList) {
        if (userList != null && userList.size() > 0) {
            userList.forEach((s, u) -> System.out.println(u + "\n"));
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
                printHashMap(numberedHashMap);
            } else {
                Helpers.printWarning("No author is found.");
            }
        } else {
            Helpers.printWarning("No author is found.");
        }
        return numberedHashMap;
    }

    // Does the same as findBookByAuthor() but in a different way and searches books by title, just to try both ways.
    public HashMap<Integer, Book> findBookByTitleOrISBN() {
        System.out.println("Enter the title or ISBN of the book:");
        List<Book> foundBooks = new ArrayList<>();
        String userSearchPhrase = searchPhraseInput();
        HashMap<Integer, Book> numberedHashMap = new HashMap<>();

        if (!userSearchPhrase.isEmpty()) {
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
                printHashMap(numberedHashMap);
            } else {
                Helpers.printWarning("Title or ISBN not found.");
            }
        } else {
            Helpers.printWarning("Title or ISBN not found.");
        }
        return numberedHashMap;
    }

    public HashMap<Integer, Book> sortBooks(String sortBy) {
        HashMap<Integer, Book> numberedHashMap;
        List<Book> sorted = new ArrayList<>();
        switch (sortBy) {
            case "title" -> sorted = allBooks.values().stream()
                    .sorted(Comparator.comparing(Book::getTitle))
                    .collect(Collectors.toList());
            case "author" -> sorted = allBooks.values().stream()
                    .sorted(Comparator.comparing(b -> b.getAuthor().getLastName()))
                    .collect(Collectors.toList());
            case "times borrowed" -> sorted = allBooks.values().stream()
                    .sorted(Comparator.comparingInt(Book::getTimesBorrowed))
                    .collect(Collectors.toList());
        }
        numberedHashMap = Helpers.createNumberedHashMapFromList(sorted);
        if (sortBy.equals("times borrowed")) {
            printNumberedList(sorted);
        } else {
            printHashMap(numberedHashMap);
        }
        return numberedHashMap;
    }

    private void printNumberedList(List<Book> bookList) {
        int counter = 1;
        for (int i = bookList.size() - 1; i >= 0; i--) {
            Helpers.printInMenuColors(counter + ".");
            System.out.print(bookList.get(i).toString("", true));
            counter ++;
        }
    }

    public void findBorrowerByNameOrLibraryCardNo() {
        System.out.println("Enter name or library card number of the borrower:");
        Borrower foundBorrower = returnBorrowerIfFound(searchPhraseInput());

        if (foundBorrower != null) {
            printBorrowerInfo(foundBorrower);
        } else {
            System.out.println("No such user found.");
        }
    }

    private void printBorrowerInfo(Borrower borrower) {
        System.out.println("\nName: " + borrower.getName() +
                "\nLibrary card no: " + borrower.getLibraryCardNumber());
        if (borrower.myBorrowedBooks.size() > 0) {
            System.out.println("Borrowed books:");
            borrower.showMyBorrowedBooks(true);
        } else {
            System.out.println(borrower.getName() + " has no borrowed books.");
        }
    }

    private Borrower returnBorrowerIfFound(String userSearchPhrase) {
        Borrower foundBorrower = null;
        if (!userSearchPhrase.isEmpty()) {
            Pattern pattern = Pattern.compile(userSearchPhrase, Pattern.CASE_INSENSITIVE);
            for (Borrower borrower : allBorrowers.values()) {
                Matcher matcher = pattern.matcher(borrower.getName().toLowerCase());
                Matcher matcher2 = pattern.matcher(String.valueOf(borrower.getLibraryCardNumber()));

                if (matcher.find() || matcher2.find()) {
                    foundBorrower = borrower;
                    break;
                }
            }
        }
        return foundBorrower;
    }

    public void addLibrarianToLibrary(Librarian librarian) {
        allLibrarians.put(librarian.getName(), librarian);
    }

    public void addBorrowerToLibrary(Borrower borrower) {
        allBorrowers.put(borrower.getUsername(), borrower);
    }

    public HashMap<String, Borrower> getAllBorrowers() {
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

    // Needs to be chained like this so it can be effectively final,
    // otherwise if we do this in multiple steps, we would have to make a temp String to use in our lambda
    private String searchPhraseInput() {
        return Helpers.readUserString().toLowerCase().replaceAll("^[\\W]+", "");
    }

    public void borrowBook(int keyOfBook, HashMap<Integer, Book> numberedHashMap, Borrower currentBorrower) {
        if (keyOfBook > 0) {
            Book book = numberedHashMap.get(keyOfBook);
            if (book.isAvailable()) {
                book.borrowMe(currentBorrower);
                allAvailableBooks.remove(book.getTitle());
                allBorrowedBooks.put(book.getTitle(), book);
                currentBorrower.addToMyBorrowedBooks(book);
                System.out.println(book.getTitle() + " is now yours until " + book.getReturnDate());
            } else {
                Helpers.printWarning("The book is already borrowed");
            }
        }
    }

    public int readWhatBookToBorrow(int max) {
        Helpers.printInMenuColors("If you want to borrow a book, enter its number. "
                + "\nTo return to the main menu, enter 0.\n");
        return Helpers.readUserInt(-1, max);
    }

    public void removeBookFromAllBorrowedBooks(Book book) {
        allBorrowedBooks.remove(book.getTitle());
    }

    public void addBookToAllAvailableBooks(Book book) {
        allAvailableBooks.put(book.getTitle(), book);
    }
}