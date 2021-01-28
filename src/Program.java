import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Program {
    private List<User> users = new ArrayList<>();   //testformat för att spara alla Users

    private void initiateTestUsers() {
        users.add(new User());                  //tomma Users för att testa
        users.add(new User());
        users.add(new User());
        users.add(new User());
        users.add(new User());
        users.add(new User());

        int counter = 1;                        //sätter username och password för alla users
        for (User u : users) {
            u.setUsername("user" + counter);
            u.setPassword("password" + counter);
            counter++;
        }
    }

    public void start() {
        initiateTestUsers();

        User currentUser = null;
        Scanner scan = new Scanner(System.in);
        String usernameInput = "";
        String passwordInput = "";
        boolean wasFound = false;
        int loginAttempts = 0;

        do {
            System.out.println("Username: ");       // Läser in username
            usernameInput = scan.nextLine();
            for (User user : users) {               // Kollar input mot alla usernames
                if (user.getUsername().equals(usernameInput)) {
                    currentUser = user;             // Sätter currentUser ifall username finns
                }
            }
            loginAttempts ++;
            if (currentUser == null && loginAttempts >= 10) {
                System.out.println("Det verkar som att du glömt ditt användarnamn. Kontakta kundtjänst.");
                //sen: kör metod som avslutar programmet.?
            }
            else if (currentUser == null) System.out.println("Användarnamnet finns inte, försök igen.");

        } while (currentUser == null && loginAttempts < 10);

        do {
            System.out.println("Password:");        // läser in password
            passwordInput = scan.nextLine();
            if (currentUser.getPassword().equals(passwordInput)) { // matchar mot currentUser's password
                wasFound = true;
            }
            if (!wasFound) System.out.println("Fel lösenord, försök igen.");
        } while (!wasFound);

        System.out.println("Välkommen till biblioteket, " + currentUser.getUsername());
    }

    public static void main(String[] args) {
        Program program = new Program();
        program.start();
    }

}
