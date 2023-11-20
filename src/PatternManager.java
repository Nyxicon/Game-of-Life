import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class PatternManager {

    private UserInterface ui;
    private GamePanel gamePanel;
    private GameOfLife game;

    private ArrayList<Pattern> patterns;
    private ArrayList<Pattern> presetPatterns;

    private JCheckBoxMenuItem selectionToolJCheckBox;
    private JMenu patternMenu;

    private Pattern currentPattern;

    private JFileChooser fileChooser;

    private boolean loadFile;
    private int fileResult;

    boolean isPatternSelected;

    private Pattern patternToSave;


    public PatternManager(Main main) {
        this.gamePanel = main.getGamePanel();
        this.game = main.getGame();
        this.ui = main.getUi();

        patterns = new ArrayList<>();

        patternToSave = new Pattern();

        loadFile = false;
        isPatternSelected = false;

        currentPattern = new Pattern();
        currentPattern.name = "glider";
        boolean[][] gliderPattern = {
                {false, false, true},
                {true, false, true},
                {false, true, true}
        };

        currentPattern.setCells(gliderPattern);

        addPresetPatterns();
    }

    private void addPresetPatterns() {
        boolean[][] gliderPattern = {
                {false, false, true},
                {true, false, true},
                {false, true, true}
        };
        presetPatterns = new ArrayList<>();
        Pattern test1 = new Pattern(gliderPattern);
        test1.name = "Glider";
        test1.preset = true;
        presetPatterns.add(test1);

        boolean[][] spaceshipPattern = {
                {false, true, false, true, false},
                {false, false, false, false, true},
                {true, false, false, false, true},
                {false, false, false, false, true},
                {false, true, false, false, true},
                {false, false, true, true, true}
        };
        Pattern test2 = new Pattern(spaceshipPattern);
        test2.name = "SpaceShip";
        test2.preset = true;
        presetPatterns.add(test2);
    }

    public void addPatternManager(UserInterface ui) {
        selectionToolJCheckBox = new JCheckBoxMenuItem();
        ui.setDesign(selectionToolJCheckBox);
        selectionToolJCheckBox.setText("Selection Tool");
        selectionToolJCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.resetDrag();
                isPatternSelected = false;
            }
        });

        patternMenu = new JMenu();
        ui.setDesign(patternMenu);
        patternMenu.setIcon(new ImageIcon(getClass().getResource("Icons/patternIcon.png")));


        File f = null;
        try {
            f = new File(new File(".").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileChooser = new JFileChooser(f.getPath() + "\\src\\Patterns");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt", "text");
        fileChooser.setFileFilter(filter);
        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileResult == JFileChooser.APPROVE_OPTION) {
                    if (loadFile) {
                        loadPattern();
                    } else {
                        savePattern();
                    }
                }
            }
        });

        JMenuItem loadFileMenuItem, saveFileMenuItem, currentPaternItem;
        loadFileMenuItem = new JMenuItem("load Pattern");
        ui.setDesign(loadFileMenuItem);
        loadFileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile = true;
                fileResult = fileChooser.showOpenDialog(fileChooser);
            }
        });
        saveFileMenuItem = new JMenuItem("save Pattern");
        ui.setDesign(saveFileMenuItem);
        saveFileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile = false;
                fileResult = fileChooser.showSaveDialog(fileChooser);
            }
        });
        currentPaternItem = new JMenuItem("current Pattern");
        ui.setDesign(currentPaternItem);
        currentPaternItem.addActionListener(e -> {
            isPatternSelected = false;
            selectionToolJCheckBox.setSelected(false);
            gamePanel.resetDrag();
        });

        patternMenu.add(selectionToolJCheckBox);
        patternMenu.add(loadFileMenuItem);
        patternMenu.add(saveFileMenuItem);
        patternMenu.add(currentPaternItem);


        JMenu preset = new JMenu("presets");
        ui.setDesign(preset);

        for (Pattern pattern : presetPatterns) {
            JMenuItem toAdd = new JMenuItem(pattern.name);
            ui.setDesign(toAdd);
            toAdd.addActionListener(e -> {
                changeCurrentPattern(pattern);
            });
            preset.add(toAdd);
        }
        patternMenu.add(preset);

        JMenu submenu = new JMenu("Sub Menu");
        ui.setDesign(submenu);
        submenu.addChangeListener(e -> {
            submenu.removeAll();
            for (Pattern pattern : patterns) {
                JMenuItem toAdd = new JMenuItem(pattern.name);
                ui.setDesign(toAdd);
                toAdd.addActionListener(e12 -> {
                    changeCurrentPattern(pattern);
                });
                submenu.add(toAdd);
            }
        });
        patternMenu.add(submenu);

        ui.add(patternMenu);
    }

    private void changeCurrentPattern(Pattern pattern) {
        if (currentPattern != null) {
            if (!currentPattern.preset) {
                Pattern tmpPattern = new Pattern(currentPattern.getCells());
                tmpPattern.name = currentPattern.name;
                Pattern toRemove = null;
                for (Pattern searchPattern : patterns) {
                    if (searchPattern.name.equals(tmpPattern.name)) toRemove = searchPattern;
                }
                if (toRemove != null) patterns.remove(toRemove);
                patterns.add(tmpPattern);
            }
        }
        currentPattern.setCells(pattern.cells);
        currentPattern.name = pattern.name;
        currentPattern.preset = pattern.preset;

        selectionToolJCheckBox.setSelected(false);
        isPatternSelected = true;
        gamePanel.resetDrag();
    }

    private void loadPattern() {
        if (fileResult == JFileChooser.APPROVE_OPTION) {

            BufferedReader inputReader = null;
            try {
                System.out.println(fileChooser.getSelectedFile());
                inputReader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (inputReader != null) {
                ArrayList<String> rows = new ArrayList<>();
                int width = 0;
                int height = 0;

                //calculate width + height
                try {
                    String st;
                    while ((st = inputReader.readLine()) != null) {
                        rows.add(st);
                        width = st.length();
                        height++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                boolean[][] test = new boolean[width][height];
                int t = 0;
                for (String string : rows) {
                    for (int i = 0; i < string.length(); i++) {
                        //TODO: compare chars
                        String test2 = String.valueOf(string.charAt(i));
                        if (test2.equals("#")) test[i][t] = false;
                        if (test2.equals(".")) test[i][t] = true;
                    }
                    t++;
                }

                Pattern tempPattern = new Pattern(test);
                tempPattern.name = String.valueOf(fileChooser.getSelectedFile());

                changeCurrentPattern(tempPattern);

                try {
                    inputReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void savePattern() {
        int firstCellX = gamePanel.getFirstCellX();
        int firstCellY = gamePanel.getFirstCellY();
        int lastCellX = gamePanel.getLastCellX();
        int lastCellY = gamePanel.getLastCellY();

        if (firstCellX != -1 && firstCellY != -1 && lastCellX != -1 && lastCellY != -1) {
            boolean[][] cellsToSave = new boolean[lastCellX - firstCellX][lastCellY - firstCellY];
            for (int x = firstCellX; x < lastCellX; x++) {
                for (int y = firstCellY; y < lastCellY; y++) {
                    if (x > 0 && y > 0) {
                        cellsToSave[x - firstCellX][y - firstCellY] = game.getCell(x, y).isAlive();
                    }
                }
            }

            patternToSave.setCells(cellsToSave);

            gamePanel.resetDrag();
        }

        BufferedWriter outputWriter = null;
        try {
            outputWriter = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile() + ".txt"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (outputWriter != null) {
            for (int y = 0; y < patternToSave.cellAmountY; y++) {
                for (int x = 0; x < patternToSave.cellAmountX; x++) {
                    try {
                        if (patternToSave.getCell(x, y)) {
                            outputWriter.write(".");
                        } else {
                            outputWriter.write("#");
                        }
                    } catch (IOException e1) {
                        System.out.println("error while writing");
                    }
                }
                try {
                    outputWriter.newLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            try {
                outputWriter.flush();
                outputWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void placeCurrentPattern(int cursorCellX, int cursorCellY) {
        for (int x = cursorCellX - currentPattern.cellAmountX; x < cursorCellX; x++) {
            for (int y = cursorCellY - currentPattern.cellAmountY; y < cursorCellY; y++) {
                if (x >= 0 && y >= 0)
                    game.setCell(x, y, currentPattern.getCell(x - (cursorCellX - currentPattern.cellAmountX), y - (cursorCellY - currentPattern.cellAmountY)));
            }
        }
    }


    public boolean isSelectionTool() {
        return selectionToolJCheckBox.isSelected();
    }

    public Pattern getCurrentPattern() {
        return currentPattern;
    }
}