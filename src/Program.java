import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Program {
    private HashMap<String, User> allUsers = new HashMap<>();   //testformat för att spara alla Users
    private Library library = Library.getLibrary();
    private User currentUser;
    Borrower currentBorrower;

    public void start() {
        initiateTestUsers();

        boolean incorrectUsername = !readUsername();
        if (incorrectUsername) {
            System.out.println(ConsoleColor.RED_BOLD +
                    "You seem to have forgotten your username or password, " +
                    "please contact customer service."
                    + ConsoleColor.RESET);
            return;
        }

        boolean incorrectPassword = !readPassword();
        if (incorrectPassword) {
            System.out.println(ConsoleColor.RED_BOLD +
                    "You seem to have forgotten your username or password, " +
                    "please contact customer service."
                    + ConsoleColor.RESET);
            return;
        }

        printMenuTitle("Welcome to the library, " + currentUser.getName() + "!");

        if (currentUser.isLibrarian()) {
            runLibrarianMenu();
        } else {
            currentBorrower = (Borrower) currentUser;
            runBorrowerMenu();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void runLibrarianMenu() {
        int userInput;
        do {
            librarianMenuChoices();
            userInput = Helpers.readUserInt(0, 10);
            librarianMenuSwitch(userInput);
        } while (userInput != 9);
    }

    private void runBorrowerMenu() {
        checkBooksToReturn(currentBorrower);
        int userInput;
        do {
            borrowerMenuChoices();
            userInput = Helpers.readUserInt(0, 10);
            borrowerMenuSwitch(userInput);
        } while (userInput != 9);
    }

    private void librarianMenuChoices() {
        System.out.println("\n"
                + ConsoleColor.BLACK_BACKGROUND_BRIGHT + "" + ConsoleColor.MAGENTA_BOLD_BRIGHT
                + "1. List all borrowed books"
                + "\n2. Add new book to library"
                + "\n3. Remove book from library"
                + "\n4. Add new user"
                + "\n5. List all borrowers"
                + "\n6. List all librarians"
                + "\n7. Find borrower by name"
                + "\n9. Exit program\n"
                + ConsoleColor.RESET
        );
    }

    private void borrowerMenuChoices() {
        System.out.println("\n"
                + ConsoleColor.BLACK_BACKGROUND_BRIGHT + "" + ConsoleColor.MAGENTA_BOLD_BRIGHT
                + "1. List all books"
                + "\n2. Sort all books alphabetically by title"
                + "\n3. Sort all books alphabetically by author"
                + "\n4. List available books"
                + "\n5. List my borrowed books"
                + "\n6. Search for book by title or ISBN"
                + "\n7. Search for book by author"
                + "\n9. Exit program\n"
                + ConsoleColor.RESET
        );
    }

    private void librarianMenuSwitch(int choice) {
        switch (choice) {
            case 1 -> {
                printMenuTitle("All borrowed books");
                library.showBooks(library.getAllBorrowedBooks(), " borrowed ");
            }
            case 2 -> {
                printMenuTitle("Add book to library");
                library.addBook();
            }
            case 3 -> {
                printMenuTitle("Remove book from library");
                library.removeBook();
            }
            case 4 -> {
                printMenuTitle("Add new user to system");
                addNewUser();
            }
            case 5 -> {
                printMenuTitle("Show all users");
                library.showAllUsersInList(library.getAllBorrowers());
            }
            case 6 -> {
                printMenuTitle("Show all librarians");
                library.showAllUsersInList(library.getAllLibrarians());
            }
            case 7 -> {
                printMenuTitle("Find borrower by name");
                library.findBorrowerByName();
            }
            case 9 -> FileUtil.writeObjectToFile("LibraryFile.ser", library);
            default -> System.out.println("Your choice does not exist, try again.");
        }
    }

    private void borrowerMenuSwitch(int choice) {
        switch (choice) {
            case 1 -> {
                printMenuTitle("All books");
                callBorrowBook(library.showBooks(library.getAllBooks(), " "));
            }
            case 2 -> {
                printMenuTitle("All books sorted by author");
                callBorrowBook(library.sortBooks("title"));
            }
            case 3 -> {
                printMenuTitle("All books sorted by title");
                callBorrowBook(library.sortBooks("author"));
            }
            case 4 -> {
                printMenuTitle("All available books");
                callBorrowBook(library.showBooks(library.getAllAvailableBooks(), " available "));
            }
            case 5 -> {
                printMenuTitle("My borrowed books");
                currentBorrower.showMyBorrowedBooks();
            }
            case 6 -> {
                printMenuTitle("Find book by title or isbn");
                callBorrowBook(library.findBookByTitleOrISBN());
            }
            case 7 -> {
                printMenuTitle("Find book by author");
                callBorrowBook(library.findBookByAuthor());
            }
            case 9 -> FileUtil.writeObjectToFile("LibraryFile.ser", library);
            default -> System.out.println(
                    ConsoleColor.RED_BOLD + "Your choice does not exist, try again." + ConsoleColor.RESET);
        }
    }

    private void printMenuTitle(String title) {
        System.out.println(ConsoleColor.GREEN_BOLD_BRIGHT
                + "\t\t\t\t\t\t\t\t\t"
                + title.toUpperCase()
                + ConsoleColor.RESET);
    }

    private void callBorrowBook(HashMap<Integer, Book> numberedHashMap) {
        if (numberedHashMap.size() > 0) {
            library.borrowBook(library.readWhatBookToBorrow(
                    numberedHashMap.size() + 1),
                    numberedHashMap,
                    currentBorrower);
        }
    }

    private void checkBooksToReturn(Borrower borrower) {
        List<Book> tempBookList = borrower.myBorrowedBooks.stream()
                .filter(book -> !LocalDate.now().isBefore(book.getReturnDate()))
                .collect(Collectors.toList());
        if (tempBookList.size() > 0) {
            System.out.println(ConsoleColor.RED_BOLD + "\n\nYou have books to return:\n");
            tempBookList.forEach(book -> System.out.println(book.getTitle() + ", RETURN DATE: " + book.getReturnDate()));
            System.out.println(ConsoleColor.RESET);
        }
    }

    private void initiateTestUsers() {
        int counter = 1;
        while (counter < 10) {
            User a = new Borrower();
            allUsers.put("user" + counter, a);
            a.setUsername("user" + counter);
            allUsers.get("user" + counter).setPassword("password" + counter);
            counter++;
        }
        Librarian librarian = new Librarian("Test"); // skapar bibliotekarie
        librarian.setUsername("librarian");
        librarian.setPassword("lib");
        allUsers.put(librarian.getUsername(), librarian);
    }

    private boolean readUsername() {
        String usernameInput;
        int loginAttempts = 0;
        do {
            System.out.println("Username:");
            usernameInput = Helpers.readUserString();
            if (allUsers.containsKey(usernameInput)) {
                currentUser = allUsers.get(usernameInput);
            }
            loginAttempts++;

            if (currentUser == null && loginAttempts >= 10) {
                return false;
            } else if (currentUser == null) System.out.println("Username does not exist, please try again.");
        } while (currentUser == null);
        return true;
    }

    private boolean readPassword() {
        String passwordInput;
        int loginAttempts = 0;
        boolean wasFound = false;
        do {
            System.out.println("Password:");
            passwordInput = Helpers.readUserString();

            if (currentUser.getPassword().equals(passwordInput)) {
                wasFound = true;
            }
            loginAttempts++;
            if (!wasFound && loginAttempts >= 10) {
                return false;
            } else if (!wasFound) System.out.println("Wrong password, please try again.");
        } while (!wasFound);
        return true;
    }

    public void addNewUser() {
        System.out.println("Would you like to create a new \n1. Librarian \n2. Borrower");
        int userInput = 0;
        while (userInput != 1 && userInput != 2) {
            userInput = Helpers.readUserInt(0, 3);
        }
        System.out.println("Enter name of new user:");
        String nameOfNewUser = Helpers.readUserString();
        User newUser = userFactory(userInput);

        newUser.setName(nameOfNewUser);
        createUsername(newUser);
        createPassword(newUser);

        if (userInput == 1) {
            library.addLibrarianToLibrary((Librarian) newUser);
        } else {
            ((Borrower) newUser).setLibraryCardNumber(newLibraryCardNo());
            library.addBorrowerToLibrary((Borrower) newUser);
        }
        allUsers.put(newUser.getName(), newUser);
    }

    private User userFactory(int userInput) {
        return userInput == 1 ? new Librarian() : new Borrower();
    }

    private int newLibraryCardNo() {
        return ThreadLocalRandom.current().nextInt(10_000, 100_000);
    }

    private void createUsername(User user) {
        boolean isNotValid;
        String username;
        System.out.println("Enter username for new user:");
        do {
            username = Helpers.readUserString();
            String finalUsername = username;
            isNotValid = allUsers.values().stream()
                    .anyMatch(u -> u.getUsername().equals(finalUsername));
            if (isNotValid) System.out.println("Usename already exists, please try another:");
        } while (isNotValid);
        user.setUsername(username);
    }

    private void createPassword(User user) {
        boolean isValid;
        String password;
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&-+=()]).{6,}$");
        System.out.println("Enter password for new user\n" +
                "Minimum 6 characters long with 1 upper case, 1 lower case, 1 digit and 1 special character:");
        do {
            password = Helpers.readUserString();
            Matcher matcher = pattern.matcher(password);
            isValid = matcher.find();
            if (!isValid) System.out.println("Invalid password, try again:");
        } while (!isValid);
        System.out.println("That password works perfectly!");
        user.setPassword(password);
    }
}
