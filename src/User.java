import java.io.Serializable;

public class User implements Serializable {
    private boolean isLibrarian;
    private String username;
    private String password;
    private String name;

    public User() {};

    public User(boolean isLibrerian, String name) {
        this.name = name;
        this.isLibrarian = isLibrerian;
    }

    public boolean isLibrarian() {
        return isLibrarian;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {  // här behöver vi kolla att username är unikt
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String
    toString() {
        return "Name: " + name + "\n" +
                "is Librarian: " + isLibrarian + "\n" +
                "Username: " + username + "\n" +
                "Password: " + password + "\n";
    }
}
