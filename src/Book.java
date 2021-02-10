import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {
    private String title;
    private Author author;
    private String bookDescription;
    private String isbn;
    private int timesBorrowed;
    private boolean isAvailable;
    private boolean isPopular;
    private LocalDate returnDate;
    private Borrower myBorrower = null;

    public Book() {
    }

    public Book(String title, Author author, String isbn, String bookDescription, boolean isPopular) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.timesBorrowed = 0;
        this.bookDescription = bookDescription;
        this.isAvailable = true;
        this.isPopular = isPopular;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Author: " + author.toString() + "\n" +
                "Description: " + bookDescription + "\n" +
                "ISBN: " + isbn + "\n" +
                "Available: " + (isAvailable ? "Yes" : "No") + "\n" +
                (returnDate != null ? "Return date: " + returnDate + "\n" : "");
    }

    public String toString(String version, boolean isLibrarian) {
        String returnString = "Title: " + title + "\n" +
                "Author: " + author.toString() + "\n" +
                "Description: " + bookDescription + "\n" +
                "ISBN: " + isbn + "\n" +
                (isLibrarian ? "Times borrowed: " + timesBorrowed + "\n" : "");
        if (version.equals("borrowed")) {
            returnString += "Borrowed by: " + myBorrower.getLibraryCardNumber() + "\n" +
                    "Return date: " + returnDate + "\n";
        } else if (version.equals("")) {
            returnString += "Available: " + (isAvailable ? "Yes" : "No" +
                    "\nReturn date: " + returnDate) + "\n";
        }
        return returnString;
    }

    public void borrowMe(Borrower currentBorrower) {
        setAvailable(false);
        setReturnDate(LocalDate.now().plusDays(isPopular() ? 14 : 28));
        setMyBorrower(currentBorrower);
        timesBorrowed++;
    }

    public void returnMe() {
        setMyBorrower(null);
        setReturnDate(null);
        setAvailable(true);
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
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

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setMyBorrower(Borrower myBorrower) {
        this.myBorrower = myBorrower;
    }

    public boolean isPopular() {
        return isPopular;
    }

    // only used by temporary book for presentation
    public void setTimesBorrowed(int timesBorrowed) {
        this.timesBorrowed = timesBorrowed;
    }

    public int getTimesBorrowed(){
        return timesBorrowed;
    }
}

