public class Main {

    // ziggis kommentar
    
    public static void main(String[] args) {
         Library.getLibrary();
         Book book = new Book("Fever", "John Lennon", "Destopian", "#1234");

        Author author = new Author("John", "Lennon");
        author.addToList(book);
         author.showList();
    }

}
