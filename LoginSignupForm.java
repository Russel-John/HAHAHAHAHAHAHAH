import javax.swing.*;
import java.util.ArrayList;

public class LoginSignupForm {
    // Array-based List: Used to store user credentials
    private static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {
        // Preload an admin account for testing
        users.add(new User("admin", "admin123"));

        // Show login form as the entry point
        showLoginForm();
    }

    // Show the login form
    private static void showLoginForm() {
        while (true) {
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();

            Object[] fields = {
                    "Username:", usernameField,
                    "Password:", passwordField
            };

            int option = JOptionPane.showConfirmDialog(null, fields, "Login", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check if the user exists in the Array-based List
                for (User user : users) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        JOptionPane.showMessageDialog(null, "Login successful! Welcome, " + username + "!");
                        InventoryManagementGUI.main(null); // Launch Inventory Management System
                        return;
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            } else {
                System.exit(0); // Exit if the user cancels
            }
        }
    }

    // Show the sign-up form
    public static void showSignUpForm() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] fields = {
                "Username:", usernameField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Sign Up", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Check if username is already taken
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    JOptionPane.showMessageDialog(null, "Username already taken. Please choose another.");
                    return;
                }
            }

            // Add a new user to the Array-based List
            users.add(new User(username, password));
            JOptionPane.showMessageDialog(null, "Sign-up successful! You can now log in.");
        }
    }

    // User class for storing username and password
    static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}