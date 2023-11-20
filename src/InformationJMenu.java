import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class InformationJMenu extends JMenu {

    private GameOfLife game;

    private JMenuItem test, liv, rev, ded;

    public InformationJMenu(GameOfLife game) {
        this.game = game;
        setIcon(new ImageIcon(getClass().getResource("Icons/infoIcon.png")));
        this.setBackground(new Color(46,48,54));
        this.setForeground(new Color(255,255,255));
        this.setOpaque(true);
        LineBorder border1 = new LineBorder(new Color(26,27,30));
        EmptyBorder border2 = new EmptyBorder(2,10,3,10);
        Border newBorder = BorderFactory.createCompoundBorder(border1, border2);
        this.setBorder(newBorder);

        test = new JMenuItem("Information");
        this.add(test);
        liv = new JMenuItem("Information");
        this.add(liv);
        rev = new JMenuItem("Information");
        this.add(rev);
        ded = new JMenuItem("Information");
        this.add(ded);

        this.addChangeListener(e -> {
            test.setText(String.valueOf(game.generation) + " Generations");
            liv.setText(String.valueOf(game.currentCells) + " alive");
            rev.setText(String.valueOf(game.revivedCells) + " revived");
            ded.setText(String.valueOf(game.deceasedCells) + " died");
        });
    }

    public void update() {
        test.setText(String.valueOf(game.generation) + " Generations");
        liv.setText(String.valueOf(game.currentCells) + " alive");
        rev.setText(String.valueOf(game.revivedCells) + " revived");
        ded.setText(String.valueOf(game.deceasedCells) + " died");
    }

}