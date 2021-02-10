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
        allUsers = loadUsers();

        boolean incorrectUsername = !readUsername();
        if (incorrectUsername) {
            Helpers.printWarning("You seem to have forgotten your username or password, " +
                    "please contact customer service.");
            return;
        }

        boolean incorrectPassword = !readPassword();
        if (incorrectPassword) {
            Helpers.printWarning("You seem to have forgotten your username or password, " +
                    "please contact customer service.");
            return;
        }

        Helpers.printMenuTitle("Welcome to the library, " + currentUser.getName() + "!");

        if (currentUser.isLibrarian()) {
            runLibrarianMenu();
        } else {
            currentBorrower = (Borrower) currentUser;
            runBorrowerMenu();
        }
        Helpers.printMenuTitle("Thank you for visiting the library!");
    }

    private HashMap<String, User> loadUsers() {
        HashMap<String, User> tempUserHashMap = new HashMap<>();
        library.getAllLibrarians().entrySet()
                .forEach(librarian -> tempUserHashMap.put(
                        librarian.getValue().getUsername(), librarian.getValue()));

        library.getAllBorrowers().entrySet().stream()
                .forEach(
                        borrower -> tempUserHashMap.put(borrower.getValue().getUsername(), borrower.getValue())
                );
        return tempUserHashMap;
    }

    private void runLibrarianMenu() {
        int userInput;
        do {
            librarianMenuChoices();
            userInput = Helpers.readUserInt(-1, 11);
            librarianMenuSwitch(userInput);
        } while (userInput != 0);
    }

    private void runBorrowerMenu() {
        checkBooksToReturn(currentBorrower);
        int userInput;
        do {
            borrowerMenuChoices();
            userInput = Helpers.readUserInt(-1, 8);
            borrowerMenuSwitch(userInput);
        } while (userInput != 0);
    }

    private void librarianMenuChoices() {
        Helpers.printInMenuColors("1. List all borrowed books"
                + "\n2. List all books"
                + "\n3. List all available books"
                + "\n4. Add new book to library"
                + "\n5. Remove book from library"
                + "\n6. Add new user"
                + "\n7. List all borrowers"
                + "\n8. List all librarians"
                + "\n9. Find borrower by name"
                + "\n10. Sort books by times borrowed"
                + "\n0. Exit program\n"
        );
    }

    private void borrowerMenuChoices() {
        Helpers.printInMenuColors("1. List all books"
                + "\n2. Sort all books alphabetically by title"
                + "\n3. Sort all books alphabetically by author"
                + "\n4. List available books"
                + "\n5. List my borrowed books"
                + "\n6. Search for book by title or ISBN"
                + "\n7. Search for book by author"
                + "\n0. Exit program\n"
        );
    }

    private void librarianMenuSwitch(int choice) {
        switch (choice) {
            case 1 -> {
                Helpers.printMenuTitle("All borrowed books");
                library.showBooks(library.getAllBorrowedBooks(), "borrowed", true);
            }
            case 2 -> {
                Helpers.printMenuTitle("All books");
                library.showBooks(library.getAllBooks(), "", true);
            }
            case 3 -> {
                Helpers.printMenuTitle("All available books");
                library.showBooks(library.getAllAvailableBooks(), "available", true);
            }
            case 4 -> {
                Helpers.printMenuTitle("Add book to library");
                library.addBook();
            }
            case 5 -> {
                Helpers.printMenuTitle("Remove book from library");
                library.removeBook();
            }
            case 6 -> {
                Helpers.printMenuTitle("Add new user to system");
                addNewUser();
            }
            case 7 -> {
                Helpers.printMenuTitle("All borrowers");
                library.showAllUsersInList(library.getAllBorrowers());
            }
            case 8 -> {
                Helpers.printMenuTitle("All librarians");
                library.showAllUsersInList(library.getAllLibrarians());
            }
            case 9 -> {
                Helpers.printMenuTitle("Find borrower by name");
                library.findBorrowerByNameOrLibraryCardNo();
            }
            case 10 -> {
                Helpers.printMenuTitle("List most borrowed books");
                library.sortBooks("times borrowed");
            }
            case 0 -> FileUtil.writeObjectToFile("LibraryFile.ser", library);
            default -> Helpers.printWarning("Your choice does not exist, try again.");
        }
    }

    private void borrowerMenuSwitch(int choice) {
        switch (choice) {
            case 1 -> {
                Helpers.printMenuTitle("All books");
                callBorrowBook(library.showBooks(library.getAllBooks(), "", false));
            }
            case 2 -> {
                Helpers.printMenuTitle("All books sorted by title");
                callBorrowBook(library.sortBooks("title"));
            }
            case 3 -> {
                Helpers.printMenuTitle("All books sorted by author");
                callBorrowBook(library.sortBooks("author"));
            }
            case 4 -> {
                Helpers.printMenuTitle("All available books");
                callBorrowBook(library.showBooks(library.getAllAvailableBooks(), "available", false));
            }
            case 5 -> {
                Helpers.printMenuTitle("My borrowed books");
                currentBorrower.showMyBorrowedBooks(false);
            }
            case 6 -> {
                Helpers.printMenuTitle("Find book by title or isbn");
                callBorrowBook(library.findBookByTitleOrISBN());
            }
            case 7 -> {
                Helpers.printMenuTitle("Find book by author");
                callBorrowBook(library.findBookByAuthor());
            }
            case 0 -> FileUtil.writeObjectToFile("LibraryFile.ser", library);
            default -> System.out.println(
                    ConsoleColor.RED_BOLD + "Your choice does not exist, try again." + ConsoleColor.RESET);
        }
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
            Helpers.printWarning("\n\nYou have books to return:\n");
            tempBookList.forEach(book -> Helpers.printWarning(book.getTitle() + ", RETURN DATE: " + book.getReturnDate()));
        }
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
            } else if (currentUser == null) Helpers.printWarning("Username does not exist, please try again.");
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
            } else if (!wasFound) Helpers.printWarning("Wrong password, please try again.");
        } while (!wasFound);
        return true;
    }

    public void addNewUser() {
        System.out.println("Enter 0 to cancel and return to main menu.");
        System.out.println("Would you like to create a new \n1. Librarian \n2. Borrower");
        int userInput = Helpers.readUserInt(-1, 3);
        if (userInput == 0) return;

        System.out.println("Enter name of new user:");
        String nameOfNewUser = Helpers.readUserString();
        if (nameOfNewUser.equals("0")) return;

        User newUser = userFactory(userInput);
        newUser.setName(nameOfNewUser);
        if (!createUsername(newUser)) return;
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

    private boolean createUsername(User user) {
        boolean isNotValid = true;
        boolean wasCreated = false;
        String username;
        System.out.println("Enter username for new user:");
        do {
            username = Helpers.readUserString();
            if (username.equals("0")) {
                return wasCreated;
            }

            String finalUsername = username;
            isNotValid = allUsers.values().stream()
                    .anyMatch(u -> u.getUsername().equals(finalUsername));
            if (isNotValid) Helpers.printWarning("Username already exists, please try another:");

        } while (isNotValid);
        wasCreated = true;
        user.setUsername(username);
        return wasCreated;
    }

    private void createPassword(User user) {
        boolean isValid;
        String password;
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&-+=()]).{6,}$");
        System.out.println("Enter password for new user\n" +
                "Minimum 6 characters long with 1 upper case, 1 lower case, 1 digit and 1 special character:");
        do {
            password = Helpers.readUserString();
            if (password.equals("0")) return;
            Matcher matcher = pattern.matcher(password);
            isValid = matcher.find();
            if (!isValid) Helpers.printWarning("Invalid password, try again:");
        } while (!isValid);
        System.out.println("That password works perfectly!");
        user.setPassword(password);
    }
}
