import java.util.HashMap;
import java.util.Scanner;

public class Program {
    private HashMap<String, User> users = new HashMap<>();   //testformat för att spara alla Users

    private void initiateTestUsers() {
        int counter = 1;

        while (counter < 10) {
            users.put("user" + counter, new User());
            users.get("user" + counter).setPassword("password" + counter);
            counter ++;
        }
    }

    public void start() {
        initiateTestUsers(); // skapar testanvändare

        User currentUser = null;
        Scanner scan = new Scanner(System.in);
        String usernameInput;
        String passwordInput;
        boolean wasFound = false;
        int loginAttempts = 0;

        do {
            System.out.println("Username: ");       // Läser in username
            usernameInput = scan.nextLine();
            if (users.containsKey(usernameInput)) {
                currentUser = users.get(usernameInput);
            }
            loginAttempts ++;

            if (currentUser == null && loginAttempts >= 10) {
                System.out.println("Det verkar som att du glömt ditt användarnamn. Kontakta kundtjänst.");
                System.exit(0); // fult avslut, vill avsluta snyggare här
            }
            else if (currentUser == null) System.out.println("Användarnamnet finns inte, försök igen.");
        } while (currentUser == null);

        loginAttempts = 0;

        do {
            System.out.println("Password:");        // läser in password
            passwordInput = scan.nextLine();

            if (currentUser.getPassword().equals(passwordInput)) { // matchar mot currentUser's password
                wasFound = true;
            }
            loginAttempts++;
            if (!wasFound && loginAttempts >= 10) {
                System.out.println("Du verkar ha glömt ditt lösenord, kontakta kundtjänst");
                System.exit(0); // fult avslut, vill avsluta snyggare här
            }
            else if (!wasFound) System.out.println("Fel lösenord, försök igen.");
        } while (!wasFound);

        System.out.println("Välkommen till biblioteket");
    }
}
