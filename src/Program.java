import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program {
    private HashMap<String, User> allUsers = new HashMap<>();   //testformat för att spara alla Users
    private Library library = Library.getLibrary();
    private User currentUser;
    Borrower currentBorrower;

    public void start() {
        initiateTestUsers();

        boolean incorrectUsername = !readUsername();
        if (incorrectUsername)  {
            System.out.println("You seem to have forgotten your username or password, please contact customer service.");
            return;
        }

        boolean incorrectPassword = !readPassword();
        if (incorrectPassword) {
            System.out.println("You seem to have forgotten your username or password, please contact customer service.");
            return;
        }

        System.out.println("Welcome to the library!");

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
        int userInput;
        do {
            borrowerMenuChoices();
            userInput = Helpers.readUserInt(0, 10);
            borrowerMenuSwitch(userInput);
        } while (userInput != 9);
    }

    private void librarianMenuChoices() {
        System.out.println("\n1. List all borrowed books"
                + "\n2. Add new book to library"
                + "\n3. Remove book from library"
                + "\n4. Add new user"
                + "\n5. List all borrowers"
                + "\n6. List all librarians"
                + "\n7. Find borrower by name"
                + "\n9. Exit program"
        );
    }

    private void borrowerMenuChoices() {
        System.out.println("\n1. List all books"
                + "\n2. Sort all books alphabetically by title"
                + "\n3. Sort all books alphabetically by author"
                + "\n4. List available books"
                + "\n5. List my borrowed books"
                + "\n6. Search for book by title or ISBN"
                + "\n7. Search for book by author"
                + "\n9. Exit program"
        );
    }

    private void librarianMenuSwitch(int choice) {
        switch (choice) {
            case 1 -> library.showBooks(library.getAllBorrowedBooks());
            case 2 -> library.addBookWithDialog();
            case 3 -> library.removeBookWithDialog();
            case 4 -> addNewUser();
            case 5 -> library.showAllUsersInList(library.getAllBorrowers());
            case 6 -> library.showAllUsersInList(library.getAllLibrarians());
            case 7 -> library.findBorrowerByName();
            case 9 -> FileUtil.writeObjectToFile("LibraryFile.ser", library);
            default -> System.out.println("Your choice does not exist, try again.");
        }
    }

    private void borrowerMenuSwitch(int choice) {
        switch (choice) {
            case 1 -> callBorrowBook(library.showBooks(library.getAllBooks()));
            case 2 -> callBorrowBook(library.sortBooks("title"));
            case 3 -> callBorrowBook(library.sortBooks("author"));
            case 4 -> callBorrowBook(library.showBooks(library.getAllAvailableBooks()));
            case 5 -> currentBorrower.showMyBorrowedBooks();
            case 6 -> callBorrowBook(library.findBookByTitleOrISBN());
            case 7 -> callBorrowBook(library.findBookByAuthor());
            case 9 -> FileUtil.writeObjectToFile("LibraryFile.ser", library);
            default -> System.out.println("Your choice does not exist, try again.");
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

    private void initiateTestUsers() {
        int counter = 1;
        while (counter < 10) {
            User a = new Borrower();
            ;
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
