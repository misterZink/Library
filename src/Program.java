import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program {
    private HashMap<String, User> users = new HashMap<>();   //testformat för att spara alla Users
    private final Library library = Library.getLibrary();
    private User currentUser = null;

    public void start() {
        initiateTestUsers(); // skapar testanvändare
        readUsername();
        readPassword();
        System.out.println("Welcome to the library!");

        // skapar en bok här som test
        library.addBook(new Book("Hej", new Author("Katten", "Jansson"), "34554OISG", "En bok om hej"));

        if (currentUser.isLibrarian()) {
            runLibrarianMenu();
        } else {
            runBorrowerMenu();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void runLibrarianMenu() {
        int userInput = 0;
        do {
            librarianMenuChoices();
            userInput = Helpers.readUserInt();
            librarianMenuSwitch(userInput);
        } while (userInput != 9);
    }

    private void runBorrowerMenu() {
        int userInput = 0;
        do {
            borrowerMenuChoices();
            userInput = Helpers.readUserInt();
            borrowerMenuSwitch(userInput);
        } while (userInput != 9);
    }

    private void librarianMenuChoices() {
        System.out.println("\n1. List all books"
                + "\n2."
                + "\n3."
                + "\n4."
        );
    }

    private void borrowerMenuChoices() {
        System.out.println("\n1. List all books"
                + "\n2."
                + "\n3."
                + "\n4."
        );
    }

    private void librarianMenuSwitch(int choice) {
        switch (choice) {
            case 1 -> library.getAllBooks();
        }
    }

    private void borrowerMenuSwitch(int choice) {
        switch (choice) {
            case 1 -> library.getAllBooks();
        }
    }

    private void initiateTestUsers() {
        int counter = 1;
        while (counter < 10) {
            users.put("user" + counter, new User());
            users.get("user" + counter).setPassword("password" + counter);
            counter++;
        }
    }

    private void readUsername() {
        String usernameInput;
        int loginAttempts = 0;
        do {
            System.out.println("Username:");
            usernameInput = Helpers.readUserString();
            if (users.containsKey(usernameInput)) {
                currentUser = users.get(usernameInput);
            }
            loginAttempts++;

            if (currentUser == null && loginAttempts >= 10) {
                System.out.println("You seem to have forgotten you username, please contact customer service.");
                System.exit(0); // fult avslut, vill avsluta snyggare här
            } else if (currentUser == null) System.out.println("Username does not exist, please try again.");
        } while (currentUser == null);
    }

    private void readPassword() {
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
                System.out.println("You seem to have forgotten you password, please contact customer service.");
                System.exit(0); // fult avslut, vill avsluta snyggare här
            } else if (!wasFound) System.out.println("Wrong password, please try again.");
        } while (!wasFound);
    }

    public void addNewUser() {
        System.out.println("Would you like to create a new \n1. Librarian \n2. Borrower");
        int userInput = 0;
        while (userInput != 1 && userInput != 2) {
            userInput = Helpers.readUserInt();
        }
        System.out.println("Enter name of new user:");
        String nameOfNewUser = Helpers.readUserString();
        User newUser;
        if (userInput == 1) {
            newUser = new Librarian(nameOfNewUser);
            createUsername(newUser);
            createPassword(newUser);
            library.addLibrarianToLibrary((Librarian) newUser);
            users.put(newUser.getName(), newUser);
        } else {
            newUser = new Borrower(nameOfNewUser, newLibraryCardNo());
            createUsername(newUser);
            createPassword(newUser);
            library.addBorrowerToLibrary((Borrower) newUser);
            users.put(newUser.getName(), newUser);
        }
    }

    private int newLibraryCardNo() {
        return ThreadLocalRandom.current().nextInt(10_000, 100_000);
    }

    private void createUsername(User user) {
        boolean isValid;
        String username;
        System.out.println("Enter username for new user:");
        do {
            username = Helpers.readUserString();
            String finalUsername = username;
            isValid = users.values().stream()
                    .anyMatch(u -> u.getUsername().equals(finalUsername));
            if (!isValid) System.out.println("Usename already exists, please try another:");
        } while (!isValid);
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

    public static void main(String[] args) {
        Program program = new Program();
        program.addNewUser();
    }

}
