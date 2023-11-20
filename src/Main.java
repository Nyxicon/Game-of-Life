import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame implements ActionListener {

    private UserInterface ui;
    private GamePanel gamePanel;
    private GameOfLife game;
    private Timer timer;

    public Main() {
        this.setTitle("Game Of Life");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.game = new GameOfLife(250, 150);
        timer = new Timer(100, this);

        this.gamePanel = new GamePanel(this);
        this.add(gamePanel);

        ui = new UserInterface(this);
        this.setJMenuBar(ui);

        this.setVisible(true);
        this.pack();
    }

    public void resize() {
        gamePanel.setPreferredSize(new Dimension(game.getCellAmountX() * gamePanel.cellSize, game.getCellAmountY() * gamePanel.cellSize));
        gamePanel.invalidate();
        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.nextGeneration();

        ui.getInfoJMenu().update();
    }

    public Timer getTimer() {
        return timer;
    }

    public GameOfLife getGame() {
        return game;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public UserInterface getUi() {
        return ui;
    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}