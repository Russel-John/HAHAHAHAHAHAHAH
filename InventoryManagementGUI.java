import javax.swing.*;
import javax.swing.table.DefaultTableModel; // Import the DefaultTableModel class
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

public class InventoryManagementGUI {
    private static ArrayList<Product> inventory = new ArrayList<>(); // Array-based List
    private static LinkedList<String> transactionHistory = new LinkedList<>(); // Linked List
    private static Queue<Product> pendingOrders = new LinkedList<>(); // Queue
    private static Stack<Product> undoStack = new Stack<>(); // Stack
    private static TreeMap<Integer, Product> bst = new TreeMap<>(); // Binary Search Tree (TreeMap)

    private static ArrayList<User> users = new ArrayList<>(); // User login data

    public static void main(String[] args) {
        // Preload an admin account for testing
        users.add(new User("admin", "admin123"));

        // Show the login/signup form first
        if (!loginSignupForm()) {
            System.exit(0); // Exit the application if login fails or is canceled.
        }

        // Start the inventory management system
        JFrame frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // === Tabbed Interface ===
        JTabbedPane tabbedPane = new JTabbedPane();

        // Inventory Tab
        JPanel inventoryPanel = createInventoryPanel();
        tabbedPane.addTab("Inventory", inventoryPanel);

        // Transactions Tab
        JPanel transactionsPanel = createTransactionsPanel();
        tabbedPane.addTab("Transactions", transactionsPanel);

        // History Tab
        JPanel historyPanel = createHistoryPanel();
        tabbedPane.addTab("History", historyPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    // === Login/Signup Form ===
    private static boolean loginSignupForm() {
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
                        return true;
                    }
                }
                JOptionPane.showMessageDialog(null, "Invalid username or password. Try again.");
            } else {
                int signupOption = JOptionPane.showConfirmDialog(null, "Would you like to sign up instead?", "Sign Up", JOptionPane.YES_NO_OPTION);
                if (signupOption == JOptionPane.YES_OPTION) {
                    showSignUpForm();
                } else {
                    return false; // User chose to cancel the login/signup process.
                }
            }
        }
    }

    private static void showSignUpForm() {
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

    // === Inventory Panel ===
    private static JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Name", "ID", "Quantity", "Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0); // Fix: DefaultTableModel imported
        JTable inventoryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Item");
        JButton updateButton = new JButton("Update Item");
        JButton deleteButton = new JButton("Delete Item");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter product name:");
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter product ID:"));
            int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity:"));
            double price = Double.parseDouble(JOptionPane.showInputDialog("Enter price per unit:"));

            Product product = new Product(name, id, quantity, price);
            inventory.add(product);
            tableModel.addRow(new Object[]{name, id, quantity, price});
        });

        return panel;
    }

    // === Transactions Panel ===
    private static JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Process Transactions");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        JButton sellButton = new JButton("Sell Item");
        panel.add(sellButton, BorderLayout.CENTER);

        sellButton.addActionListener(e -> {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter product ID to sell:"));
            int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity to sell:"));

            for (Product product : inventory) {
                if (product.getId() == id) {
                    if (product.getQuantity() >= quantity) {
                        product.setQuantity(product.getQuantity() - quantity);
                        transactionHistory.add("Sold " + quantity + " of " + product.getName());
                        JOptionPane.showMessageDialog(null, "Transaction successful!");
                        return;
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient stock.");
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Product not found.");
        });

        return panel;
    }

    // === History Panel ===
    private static JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh History");
        panel.add(refreshButton, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> {
            StringBuilder history = new StringBuilder();
            for (String transaction : transactionHistory) {
                history.append(transaction).append("\n");
            }
            historyArea.setText(history.toString());
        });

        return panel;
    }
}

// === Product Class ===
class Product {
    private String name;
    private int id;
    private int quantity;
    private double price;

    public Product(String name, int id, int quantity, double price) {
        this.name = name;
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}

// === User Class ===
class User {
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