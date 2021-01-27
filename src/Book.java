import java.util.Date;

public class Book {
    private String title;
    private String author;
    private String bookDescription;
    private boolean isAvailable;
    private String isbn;
    private Date returnDate;

    public Book() {
    }

    public Book(String title, String author, String bookDescription, String isbn) {
        this.title = title;
        this.author = author;
        this.bookDescription = bookDescription;
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", bookDescription='" + bookDescription + '\'' +
                ", isAvailable=" + isAvailable +
                ", isbn='" + isbn + '\'' +
                ", returnDate=" + returnDate +
                '}';
    }
}
