import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Helpers {
    private static final Scanner scan = new Scanner(System.in);

    public static int readUserInt(int min, int max) {
        int userInput;
        do {
            System.out.println("Enter your choice: ");
            while (!scan.hasNextInt()) {
                System.out.println("Try again.");
                scan.next();
            }
            userInput = scan.nextInt();

        } while (userInput <= min || userInput >= max);
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

    public static HashMap<Integer, Book> createNumberedHashMap(HashMap<String, Book> sourceHashMap) {
        List<Book> sourceList = new ArrayList<>(sourceHashMap.values());
        HashMap<Integer, Book> numberedHashMap = new HashMap<>();
        Integer counter = 1;
        for (Book book : sourceList) {
            numberedHashMap.put(counter, book);
            counter++;
        }
        return numberedHashMap;
    }

    public static HashMap<Integer, Book> createNumberedHashMapFromList(List<Book> sourceList) {
        HashMap<Integer, Book> numberedHashMap = new HashMap<>();
        Integer counter = 1;
        for (Book t : sourceList) {
            numberedHashMap.put(counter, t);
            counter++;
        }
        return numberedHashMap;
    }

    public static void printMenuTitle(String text) {
        System.out.println(ConsoleColor.MAGENTA_BOLD_BRIGHT
                + "\t\t\t\t\t\t\t\t\t"
                + text.toUpperCase()
                + ConsoleColor.RESET);
    }

    public static void printInMenuColors(String text) {
        System.out.println("\n"
                + ConsoleColor.BLACK_BACKGROUND_BRIGHT
                + ""
                + ConsoleColor.YELLOW_BOLD_BRIGHT
                + text
                + ConsoleColor.RESET
        );
    }

    public static void printWarning(String text) {
        System.out.println(ConsoleColor.RED_BOLD + text + ConsoleColor.RESET);
    }
}
