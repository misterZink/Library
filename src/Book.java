import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {
    private String title;
    private Author author;
    private String bookDescription;
    private String isbn;
    private boolean isAvailable;
    private LocalDate returnDate;
    private Borrower myBorrower = null;

    public Book() {
    }

    public Book(String title, Author author, String isbn, String bookDescription) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.bookDescription = bookDescription;
        this.isAvailable = true;
    }

    public Book(String title) {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
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


    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Borrower getMyBorrower() {
        return myBorrower;
    }

    public void setMyBorrower(Borrower myBorrower) {
        this.myBorrower = myBorrower;
    }

    @Override
    public String toString() {
        return
                "Title: " + title + "\n" +
                "Author: " + author.toString() + "\n" +
                "Description: " + bookDescription + "\n" +
                "ISBN: " + isbn + "\n" +
                "Is available: " + isAvailable + "\n" +
                "Return date: " + returnDate + "\n";
    }


}

