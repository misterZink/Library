public class Librarian extends User {
    private String name;

    public Librarian(String name) {
        this.name = name;
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
