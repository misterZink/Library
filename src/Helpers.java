import java.util.*;

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

    public static <T> HashMap<Integer, T> createNumberedHashMap(HashMap<String, T> sourceHashMap) {
        List<T> sourceList = new ArrayList<>(sourceHashMap.values());
        HashMap<Integer, T> numberedHashMap = new HashMap<>();
        Integer counter = 1;
        for (T t: sourceList) {
            numberedHashMap.put(counter, t);
            counter ++;
        }
        return numberedHashMap;
    }

    public static <T> HashMap<Integer, T> createNumberedHashMapFromList(List<T> sourceList) {
        HashMap<Integer, T> numberedHashMap = new HashMap<>();
        Integer counter = 1;
        for (T t: sourceList) {
            numberedHashMap.put(counter, t);
            counter ++;
        }
        return numberedHashMap;
    }
}
