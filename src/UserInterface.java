
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class UserInterface extends JMenuBar {

    private Main main;

    private InformationJMenu infoJMenu;
    private PatternManager patternManager;

    private JMenu speedMenu;
    private JButton toggleButton, nextButton;
    private JSlider delaySlider, cellSizeSlider, cellAmountXSlider, cellAmountYSlider;
    private JCheckBoxMenuItem drawGridJCheckBox, toroidalGenJCheckBox;

    ButtonGroup filterButtonGroup = new ButtonGroup();

    public UserInterface(final Main main) {
        this.main = main;

        UIManager.getLookAndFeelDefaults().put("PopupMenu.background", new Color(46,48,54));
        UIManager.getLookAndFeelDefaults().put("PopupMenu.border", Color.black);
        setBackground(new Color(60,62,70));
        setPreferredSize(new Dimension(200,25));
        LineBorder border3 = new LineBorder(new Color(26,27,30));
        EmptyBorder border4 = new EmptyBorder(1,1,3,1);
        Border mbBorder = BorderFactory.createCompoundBorder(border3, border4);
        setBorder(mbBorder);

        addSpacer();

        addSettingsMenu();

        addSpacer();

        nextButton = new JButton();
        nextButton.setIcon(new ImageIcon(getClass().getResource("Icons/nextIcon.png")));
        setDesign(nextButton);
        //nextButton.addActionListener(e -> main.getGame().nextGeneration());
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.getGame().nextGeneration();
            }
        });
        add(nextButton);

        addSpacer();

        toggleButton = new JButton("");
        toggleButton.setIcon(new ImageIcon(getClass().getResource("Icons/playIcon.png")));
        setDesign(toggleButton);
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (main.getTimer().isRunning()) {
                    main.getTimer().stop();
                    toggleButton.setIcon(new ImageIcon(getClass().getResource("Icons/playIcon.png")));
                } else {
                    main.getTimer().start();
                    toggleButton.setIcon(new ImageIcon(getClass().getResource("Icons/stopIcon.png")));
                }
            }
        });
        add(toggleButton);

        addSpacer();

        speedMenu = new JMenu("Delay(100ms)");
        setDesign(speedMenu);
        speedMenu.setPreferredSize(new Dimension(110,25));

        delaySlider = new JSlider(JSlider.HORIZONTAL, 0,500,10);
        setDesign(delaySlider);
        delaySlider.setValue(100);
        delaySlider.setPaintLabels(true);
        delaySlider.setPaintTicks(true);
        delaySlider.setMajorTickSpacing(100);
        delaySlider.setMinorTickSpacing(10);
        delaySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                main.getTimer().setDelay(delaySlider.getValue());
                speedMenu.setText("Delay(" + delaySlider.getValue() + "ms)");
            }
        });
        /*delaySlider.addChangeListener(e -> {
            main.getTimer().setDelay(delaySlider.getValue());
            speedMenu.setText("Delay(" + delaySlider.getValue() + "ms)");
        });*/
        speedMenu.add(delaySlider);
        add(speedMenu);

        addSpacer();

        patternManager = new PatternManager(main);
        patternManager.addPatternManager(this);

        addSpacer();

        addFilterMenu();

        addSpacer();

        infoJMenu = new InformationJMenu(main.getGame());
        add(infoJMenu);
    }

    private void addSettingsMenu() {
        JMenu settingsMenu = new JMenu();
        setDesign(settingsMenu);
        settingsMenu.setIcon(new ImageIcon(getClass().getResource("Icons/settingsIcon.png")));

        drawGridJCheckBox = new JCheckBoxMenuItem("draw Grid");
        setDesign(drawGridJCheckBox);
        drawGridJCheckBox.setSelected(false);
        settingsMenu.add(drawGridJCheckBox);

        toroidalGenJCheckBox = new JCheckBoxMenuItem("toroidal");
        setDesign(toroidalGenJCheckBox);
        toroidalGenJCheckBox.setSelected(true);
        toroidalGenJCheckBox.addActionListener(e -> main.getGame().setToroidal(!main.getGame().isToroidal()));
        settingsMenu.add(toroidalGenJCheckBox);


        JMenu boardMenu = new JMenu("Board");
        setDesign(boardMenu);

        JCheckBox enableBoardMenuCheckBox = new JCheckBox("May cause Lag/UI-errs");
        setDesign(enableBoardMenuCheckBox);
        enableBoardMenuCheckBox.addActionListener(e -> {
            cellSizeSlider.setEnabled(!cellSizeSlider.isEnabled());
            cellAmountXSlider.setEnabled(!cellAmountXSlider.isEnabled());
            cellAmountYSlider.setEnabled(!cellAmountYSlider.isEnabled());
        });
        boardMenu.add(enableBoardMenuCheckBox);

        cellSizeSlider = new JSlider(JSlider.HORIZONTAL, 1,8,5);
        setDesign(cellSizeSlider);
        cellSizeSlider.setPaintLabels(true);
        cellSizeSlider.setPaintTicks(true);
        cellSizeSlider.setMajorTickSpacing(5);
        cellSizeSlider.setMinorTickSpacing(1);
        cellSizeSlider.addChangeListener(e -> {
            main.getGamePanel().cellSize = cellSizeSlider.getValue();
            if (cellSizeSlider.getValue() == 0) main.getGamePanel().cellSize = 1;
            main.resize();
        });
        boardMenu.add(cellSizeSlider);

        cellAmountXSlider = new JSlider(JSlider.HORIZONTAL, 100,900,250);
        setDesign(cellAmountXSlider);
        cellAmountXSlider.setPaintLabels(true);
        cellAmountXSlider.setPaintTicks(true);
        cellAmountXSlider.setMajorTickSpacing(200);
        cellAmountXSlider.setMinorTickSpacing(100);
        cellAmountXSlider.addChangeListener(e -> {

            int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            int testV = width / cellAmountXSlider.getValue();
            if (cellSizeSlider.getValue() > testV) {
                cellSizeSlider.setValue(testV);
                main.getGamePanel().cellSize = testV;
            }



            main.getGame().changeCellAmount(cellAmountXSlider.getValue(), main.getGame().getCellAmountY());//TODO: refactor cellamount
            main.resize();
        });
        boardMenu.add(cellAmountXSlider);

        cellAmountYSlider = new JSlider(JSlider.HORIZONTAL, 50,500,150);
        setDesign(cellAmountYSlider);
        cellAmountYSlider.setPaintLabels(true);
        cellAmountYSlider.setPaintTicks(true);
        cellAmountYSlider.setMajorTickSpacing(150);
        cellAmountYSlider.setMinorTickSpacing(50);
        cellAmountYSlider.addChangeListener(e -> {

            int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
            int testV = height / cellAmountYSlider.getValue();

            if (cellSizeSlider.getValue() > testV) {
                cellSizeSlider.setValue(testV);
                main.getGamePanel().cellSize = testV;
            }

            main.getGame().changeCellAmount(main.getGame().getCellAmountX(), cellAmountYSlider.getValue());//TODO: refactor cellamount
            main.resize();
        });
        cellSizeSlider.setEnabled(false);
        cellAmountXSlider.setEnabled(false);
        cellAmountYSlider.setEnabled(false);
        boardMenu.add(cellAmountYSlider);

        settingsMenu.add(boardMenu);

        JMenuItem killALlItem = new JMenuItem("kill all");
        setDesign(killALlItem);
        killALlItem.addActionListener(e -> {
            main.getGame().resetAllCells();
        });
        settingsMenu.add(killALlItem);

        JMenuItem generateRandomItem = new JMenuItem("rand.");
        setDesign(generateRandomItem);
        generateRandomItem.addActionListener(e -> {
            for (int x = 0; x < main.getGame().getCellAmountX(); x++) {
                for (int y = 0; y < main.getGame().getCellAmountY(); y++) {
                    Random random = new Random();
                    if (random.nextInt(2) == 0) {
                        main.getGame().setCell(x,y,true);
                    } else {
                        main.getGame().setCell(x,y,false);
                    }
                }
            }
        });
        settingsMenu.add(generateRandomItem);

        add(settingsMenu);
    }

    private void addFilterMenu() {
        JMenu filterMenu = new JMenu();
        filterMenu.setIcon(new ImageIcon(getClass().getResource("Icons/filterIcon.png")));
        setDesign(filterMenu);

        filterButtonGroup = new ButtonGroup();

        JButton resetFilterButton = new JButton("reset");
        setDesign(resetFilterButton);
        resetFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterButtonGroup.clearSelection();
            }
        });
        filterMenu.add(resetFilterButton);

        JRadioButton tailRadioButton = new JRadioButton("Tail");
        setDesign(tailRadioButton);
        tailRadioButton.setActionCommand("Tail");
        JRadioButton rainbowRadioButton = new JRadioButton("Rainbow");
        setDesign(rainbowRadioButton);
        rainbowRadioButton.setActionCommand("Rainbow");
        filterButtonGroup.add(tailRadioButton);
        filterButtonGroup.add(rainbowRadioButton);

        filterMenu.add(tailRadioButton);
        filterMenu.add(rainbowRadioButton);

        add(filterMenu);
    }

    private void addSpacer() {
        JMenu spacer = new JMenu();
        spacer.setEnabled(false);
        spacer.setMinimumSize(new Dimension(3, 1));
        spacer.setPreferredSize(new Dimension(3, 1));
        spacer.setMaximumSize(new Dimension(3, 1));
        add(spacer);
    }

    public void setDesign(JComponent test) {
        test.setBackground(new Color(46,48,54));
        test.setForeground(new Color(255,255,255));
        test.setOpaque(true);
        if (test instanceof JButton) {
            JButton test2 = (JButton) test;
            test2.setFocusPainted(false);
        }
        if (test instanceof JCheckBoxMenuItem) {
            JCheckBoxMenuItem test3 = (JCheckBoxMenuItem) test;
            test3.setFocusPainted(false);
        }

        LineBorder border1 = new LineBorder(new Color(26,27,30));
        EmptyBorder border2 = new EmptyBorder(2,10,3,10);
        Border newBorder = BorderFactory.createCompoundBorder(border1, border2);
        test.setBorder(newBorder);
    }

    public boolean isdDrawGrid() {
        return drawGridJCheckBox.isSelected();
    }

    public PatternManager getPatternManager() {
        return patternManager;
    }

    public InformationJMenu getInfoJMenu() {
        return infoJMenu;
    }

    public String getFilterButtonGroupActionCommand() {
        if (filterButtonGroup.getSelection() != null) {
            return filterButtonGroup.getSelection().getActionCommand();
        }
        return "nothing";
    }
}
