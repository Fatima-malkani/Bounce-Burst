import javax.swing.*;
import java.awt.*;

class GameLauncher extends JFrame {
    public GameLauncher() {
        // Set up the main game window
        setTitle("Bounce & Burst");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a panel for the title and play button
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(173, 216, 230)); // Light blue background
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align panel components

        // Title label
        JLabel titleLabel = new JLabel("Bounce & Burst");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
        titleLabel.setForeground(new Color(60, 90, 180)); // Solid blue color for text
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align label

        // Play button
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
        playButton.setPreferredSize(new Dimension(150, 50)); // Button dimensions
        playButton.setBackground(Color.BLACK);
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align button

        playButton.addActionListener(e -> {
            JFrame loginFrame = new JFrame("Login");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(800, 600);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.add(new LoginScreen(this)); // Pass GameLauncher instance
            loginFrame.setVisible(true);
        });

        // Add components with spacing
        mainPanel.add(Box.createVerticalStrut(150)); // Space above title
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(40)); // Space between title and button
        mainPanel.add(playButton);

        // Add the main panel to the frame
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // This method will be called after successful login
    public void startGame(String username, String email) {
        try {
            System.out.println("Game starting for: " + username + " with email: " + email);

            // Create and display the GamePlay screen
            JFrame gameFrame = new JFrame("Bounce & Burst Game");
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setSize(600, 600);
            gameFrame.setLocationRelativeTo(null);
            gameFrame.add(new GamePlay(username, new Scoreboard()));
            gameFrame.setVisible(true);

            this.dispose(); // Close the launcher window
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error starting the game: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


}
