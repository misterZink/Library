import java.io.*;
import java.util.Scanner;

public class Main {
//since this class contains public static void main, all the attributes must be static.
    static String fileName = null;
    static Library lib = Library.getLibrary();
    static Scanner in = new Scanner(System.in);
    static Boolean running = true;

    /*public static void main(String[] args) {
         Library.getLibrary();
         Book book = new Book("Fever", "John Lennon", "Destopian", "#1234");

        Author author = new Author("John", "Lennon");
        author.addToList(book);
         author.showList();
    }*/
 //------------
    public static void main(String[] args) {
        while (running) {
            System.out.println("\nEnter 0 for load a library. " + "\nEnter 1 for save and quit " + "\nEnter 2 for list all books in library" + "\nEnter 3 for add book to library");
            int answer = in.nextInt();
            switch (answer) {
                case 0:
                    System.out.println("Enter file name to load");
                    loadScript(in.next());
                    break;
                case 1:
                    saveAndQuit();
                    break;
                case 2:
                    lib.getAllBooks();

                    break;

                case 3:
                    addBook();
                    break;
            }

        }
        System.exit(0);
    }
// add a book to our library
    private static void addBook() {
        String isbn;
        String title;
        String author;
        String description;
        System.out.println("\nEnter title: ");
        title = in.next();
        System.out.println("\nEnter Author: ");
        author = in.next();
        System.out.println("\nEnter ISBN: ");
        isbn = in.next();
        System.out.println("\nEnter Description: ");
        description = in.next();

        Book b = new Book(title, author, isbn,  description);
        lib.addBook(b);



    }

    private static void saveAndQuit() {
        System.out.println("Enter file name: ");
        fileName = in.next() + ".ser";
        running = false;
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fos);
            out.writeObject(lib);
            fos.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadScript(String name) {
        //FileInputStream is how you read a file
        FileInputStream fis = null;
        ObjectInputStream in = null;
        File file = new File(name + ".ser");
        if (file.exists()) {
            try {
                fis = new FileInputStream(file);
                in = new ObjectInputStream(fis);
                lib = (Library) in.readObject();
                fis.close();
                in.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("\nThe file does not exist!");
        }
    }

}
