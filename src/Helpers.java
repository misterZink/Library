import java.util.Objects;
import java.util.Scanner;

public class Helpers {
    private static final Scanner scan = new Scanner(System.in);

    public static int readUserInt() {
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

    public static String readUserString() {
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
