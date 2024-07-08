
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private class Tile {

        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // Food
    Tile food;
    Random random;

    // Game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;
    boolean restartPending = false;

    int boardWidth;
    int boardHeight;
    int tileSize = 20;

    int delay = 100;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);

        addKeyListener(this);
        setFocusable(true);

        resetGame();
        gameLoop = new Timer(delay, this);
        gameLoop.start();
    }

    public void resetGame() {
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();
        food = new Tile(10, 10);

        random = new Random();
        placeFood();
        velocityX = 0;
        velocityY = 0;
        gameOver = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid
        // for (int i = 0; i < boardWidth / tileSize; i++) {
        //     g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
        //     g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        // }

        // Food
        g.setColor(Color.RED);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // SnakeHead
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // SnakeBody
        for (Tile snakePart : snakeBody) {
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        // Score
        if (gameOver) {
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 20));

            String gameOverText = "Game Over";
            String scoreText = "Score: " + snakeBody.size();
            String restartText = "Press any key twice to restart";

            int gameOverTextWidth = g.getFontMetrics().stringWidth(gameOverText);
            int scoreTextWidth = g.getFontMetrics().stringWidth(scoreText);
            int restartTextWidth = g.getFontMetrics().stringWidth(restartText);

            int centerX = boardWidth / 2;
            int centerY = boardHeight / 2;

            // Draw each line of text
            g.drawString(gameOverText, centerX - gameOverTextWidth / 2, centerY - tileSize);
            g.drawString(scoreText, centerX - scoreTextWidth / 2, centerY);
            g.drawString(restartText, centerX - restartTextWidth / 2, centerY + tileSize);
        } else {
            g.setColor(Color.yellow);
            g.drawString("Score: " + snakeBody.size(), tileSize, tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();

            delay = delay-5;
            gameLoop.setDelay(delay);
        }

        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize
                || snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            if (restartPending) {
                resetGame();
                gameLoop.start();
                repaint();
                restartPending = false; // Reset the flag after restarting the game
            } else {
                restartPending = true; // Set the flag on the first key press
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
                if (velocityY != 1) {
                    velocityX = 0;
                    velocityY = -1;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
                if (velocityY != -1) {
                    velocityX = 0;
                    velocityY = 1;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                if (velocityX != 1) {
                    velocityX = -1;
                    velocityY = 0;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                if (velocityX != -1) {
                    velocityX = 1;
                    velocityY = 0;
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
