import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GamePlay extends JPanel implements ActionListener, KeyListener {
    private final String username;
    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 600;
    private Timer timer;
    private boolean gameOver = false;
    private boolean isBallLaunched = false;
    private int score;
    private Rectangle paddle, ball;
    private long startTime;
    private int ballVelocityX = 5;
    private int ballVelocityY = -5;
    private boolean win = false;
    private MapGenerator mapGenerator;
    private final Scoreboard scoreboard;

    public GamePlay(String username, Scoreboard scoreboard) {
        this.username = username;
        this.scoreboard = scoreboard;
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();
        initGame();
    }

    private void initGame() {
        paddle = new Rectangle((SCREEN_WIDTH - 100) / 2, SCREEN_HEIGHT - 50, 100, 20);
        ball = new Rectangle(paddle.x + paddle.width / 2 - 10, paddle.y - 20, 20, 20);
        score = 0;
        startTime = System.currentTimeMillis();
        gameOver = false;
        isBallLaunched = false;

        mapGenerator = new MapGenerator();
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(paddle.x, paddle.y, paddle.width, paddle.height);
        g.setColor(Color.WHITE);
        g.fillOval(ball.x, ball.y, ball.width, ball.height);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 20, 30);

        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        if (elapsedTime >= 60) {
            long minutes = elapsedTime / 60;
            long seconds = elapsedTime % 60;
            g.drawString("Time: " + minutes + " min " + seconds + "s", SCREEN_WIDTH - 150, 30);
        } else {
            g.drawString("Time: " + elapsedTime + "s", SCREEN_WIDTH - 120, 30);
        }

        mapGenerator.draw(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", SCREEN_WIDTH / 2 - 75, SCREEN_HEIGHT / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press Enter to Restart", SCREEN_WIDTH / 2 - 100, SCREEN_HEIGHT / 2 + 40);
            showEndScreen();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (isBallLaunched) {
                moveBall();
                checkBalloonCollision();
            } else {
                ball.x = paddle.x + paddle.width / 2 - ball.width / 2;
                ball.y = paddle.y - ball.height;
            }
        }
        repaint();
    }

    private void moveBall() {
        ball.x += ballVelocityX;
        ball.y += ballVelocityY;

        if (ball.intersects(paddle)) {
            ballVelocityY = -ballVelocityY;
            ball.y = paddle.y - ball.height;
        }

        if (ball.x <= 0 || ball.x + ball.width >= SCREEN_WIDTH) {
            ballVelocityX = -ballVelocityX;
        }

        if (ball.y <= 0) {
            ballVelocityY = -ballVelocityY;
        }

        if (ball.y + ball.height > SCREEN_HEIGHT) {
            gameOver = true;
            timer.stop();
        }
    }

    private void checkBalloonCollision() {
        for (int i = 0; i < mapGenerator.getBalloons().size(); i++) {
            Rectangle balloonRect = mapGenerator.getBalloons().get(i).getRect();
            if (ball.intersects(balloonRect)) {
                ballVelocityY = -ballVelocityY;
                mapGenerator.removeBalloon(i);
                score += 2;
                repaint();
                break;
            }
        }

        if (mapGenerator.getBalloons().isEmpty() && !win) {
            win = true;
            timer.stop();

            Timer delayTimer = new Timer(300, e -> showEndScreen());
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }
    private void showEndScreen() {
        long totalTime = (System.currentTimeMillis() - startTime) / 1000;
        scoreboard.addScore(username, score, totalTime);  // Add the score to the scoreboard

        String message = (win ? "You Won!" : "Game Over")
                + "\nUsername: " + username
                + "\nScore: " + score
                + "\nTime: " + totalTime + "s";

        // Create a non-modal JOptionPane
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog dialog = optionPane.createDialog(this, "Game Over");
        dialog.setModal(false);

        // Add a custom button to view the scoreboard
        JButton viewScoreboardButton = new JButton("View Scoreboard");
        viewScoreboardButton.addActionListener(e -> {
            dialog.dispose();
            showScoreboard();  // Show the scoreboard after the dialog is closed
        });
        optionPane.setOptions(new Object[]{viewScoreboardButton});

        dialog.setVisible(true);
    }

    private void showScoreboard() {
        JFrame scoreboardFrame = new JFrame("Scoreboard");
        scoreboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        scoreboardFrame.setSize(400, 300);

        // Pass a callback to restart the game
        scoreboardFrame.add(scoreboard.getScoreboardPanel(() -> {
            scoreboardFrame.dispose();  // Close the scoreboard window
            initGame();                 // Reinitialize the game
            timer.start();              // Restart the game timer
        }));

        scoreboardFrame.setLocationRelativeTo(null);
        scoreboardFrame.setVisible(true);
    }



    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT && paddle.x > 0) {
            paddle.x -= 10;
        }
        if (keyCode == KeyEvent.VK_RIGHT && paddle.x + paddle.width < SCREEN_WIDTH) {
            paddle.x += 10;
        }

        if (!isBallLaunched && keyCode == KeyEvent.VK_SPACE) {
            isBallLaunched = true;
        }

        if (gameOver && keyCode == KeyEvent.VK_ENTER) {
            initGame();
            timer.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}