import javax.swing.*;
import java.awt.*;

class LoginScreen extends JPanel {
    private final JTextField usernameField;
    private final JTextField emailField;

    public LoginScreen(GameLauncher gameLauncher) {
        setLayout(new GridBagLayout()); // Use GridBagLayout for more control over component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Add padding around components

        // Set preferred size of the panel for a larger window
        setPreferredSize(new Dimension(800, 600));

        // Create title label
        JLabel titleLabel = new JLabel("Login Page");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font size for title
        titleLabel.setHorizontalAlignment(JLabel.CENTER); // Center the title

        // Create username and email labels
        JLabel usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField(20); // Limit the number of characters
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size for text field
        JLabel emailLabel = new JLabel("Email: ");
        emailField = new JTextField(20); // Limit the number of characters
        emailField.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size for text field

        // Create login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size for button

        // Positioning components in the GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Make title span across both columns
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.gridwidth = 1; // Reset gridwidth for other components
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST; // Align labels to the right
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Align text fields to the left
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(emailField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        // Action listener for the login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            if (!username.isEmpty() && !email.isEmpty()) {
                gameLauncher.startGame(username, email); // Call startGame on gameLauncher
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both username and email.");
            }
        });
    }
}
