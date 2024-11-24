import javax.swing.*;
import java.awt.*;
import java.io.*;

class PlayerScore {
    private final String username;
    private final int score;
    private final long time;

    public PlayerScore(String username, int score, long time) {
        this.username = username;
        this.score = score;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public long getTime() {
        return time;
    }
}

public class Scoreboard {
    private static final String TEXT_FILE_NAME = "scoreboard.txt"; // Path to the scoreboard file

    // Method to append new player details to the file
    public void addScore(String username, int score, long totalTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEXT_FILE_NAME, true))) {
            String timeDisplay = (totalTime / 60) + " min " + (totalTime % 60) + "s";
            String line = username + " - Score: " + score + " - Time: " + timeDisplay;
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clears the scoreboard file
    public void clearScoreboard() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEXT_FILE_NAME))) {
            // Overwrites the file with an empty content, effectively clearing it.
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JPanel getScoreboardPanel(Runnable restartCallback) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Top Scores");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        // Read and display all scores from the text file
        try (BufferedReader reader = new BufferedReader(new FileReader(TEXT_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JLabel scoreLabel = new JLabel(line);
                scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(scoreLabel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Button to close the scoreboard and restart the game
        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(e -> restartCallback.run()); // Calls the restart logic passed as a callback

        // Exit button to close the application
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // Clear button to remove all scores from the scoreboard
        JButton clearButton = new JButton("Clear Scores");
        clearButton.addActionListener(e -> {
            clearScoreboard(); // Clears the scoreboard file
            panel.removeAll(); // Remove all components from the panel
            panel.revalidate(); // Revalidate the panel to reflect changes
            panel.repaint(); // Repaint the panel to update the UI
            JLabel clearedLabel = new JLabel("Scores Cleared");
            clearedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(clearedLabel); // Show a message saying the scoreboard has been cleared
        });

        buttonPanel.add(continueButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(clearButton);
        panel.add(buttonPanel);

        panel.setPreferredSize(new Dimension(800, 600));
        return panel;
    }

    // Handle User Login and add their score to the file
    public void handleUserLogin(String username, int score, long time) {
        addScore(username, score, time); // Append score of logged-in user to the file
    }


}
