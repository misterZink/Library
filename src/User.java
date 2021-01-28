public class User {
    private boolean isLibrerian;
    private String username;
    private String password;

    public User() {};

    public User(boolean isLibrerian) {
        this.isLibrerian = isLibrerian;
    }

    public boolean isLibrerian() {
        return isLibrerian;
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

    @Override
    public String
    toString() {
        return "User{" +
                "isLibrerian=" + isLibrerian +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
