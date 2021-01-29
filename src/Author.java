import java.util.ArrayList;
import java.util.List;

public class Author {

    private String firstName;
    private String lastName;

    List<Book> booksByAuthor = new ArrayList<>();

    public Author() {

    }


    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void addToList(Book book) {
        booksByAuthor.add(book);
    }

    public void showList() {
        for (Book book : booksByAuthor) {
            System.out.println(book);
        }
    }
}
