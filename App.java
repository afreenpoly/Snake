import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {
        
        int boardWidth=600;
        int boardHeight=600;

        JFrame frame= new JFrame();
        frame.setVisible(true);
        frame.setSize(600,600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame=new SnakeGame(boardWidth,boardHeight);
        frame.add(snakeGame);
        frame.pack(); //corrects the size of frame
        snakeGame.requestFocus();
    }

}
