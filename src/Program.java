import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Program {
    private HashMap<String, User> users = new HashMap<>();   //testformat för att spara alla Users
    private final Scanner scan = new Scanner(System.in);
    private final Library library = Library.getLibrary();
    private User currentUser = null;

    public void start() {
        initiateTestUsers(); // skapar testanvändare
        readUsername();
        readPassword();
        System.out.println("Welcome to the library!");

        // skapar en bok här som test
        library.addBook(new Book("Hej", "Katten Katten", "34554OISG", "En bok om hej"));

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
            userInput = readUserInt();
            librarianMenuSwitch(userInput);
        } while (userInput != 9);
    }

    private void runBorrowerMenu() {
        int userInput = 0;
        do {
            borrowerMenuChoices();
            userInput = readUserInt();
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

    private void librarianMenuSwitch(int choice){
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
            usernameInput = readUserString();
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
            passwordInput = readUserString();

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

    private int readUserInt() {
        int userInput;
        do {
            System.out.println("Enter your choice: ");
            while (!scan.hasNextInt()) {
                System.out.println("Try again.");
                scan.next();
            }
            userInput = scan.nextInt();

        } while (userInput <= 0);
        return userInput;
    }

    private String readUserString() {
        String userInput = null;
        do {
            try {
                userInput = scan.nextLine();
            } catch (Exception e) {
                System.out.println("Try again");
            }
        } while (Objects.requireNonNull(userInput).isEmpty());
        return userInput;
    }
}
