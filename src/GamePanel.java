import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class GamePanel extends JPanel {

    private Main main;

    int cellSize = 5;

    private int cursorX, cursorY;

    private int startDragX = -1;
    private int startDragY = -1;
    private int endDragX = -1;
    private int endDragY = -1;

    public GamePanel(Main main) {
        this.main = main;
        this.setPreferredSize(new Dimension(main.getGame().getCellAmountX() * cellSize, main.getGame().getCellAmountY() * cellSize));

        MouseInput mouseInput = new MouseInput();
        this.addMouseListener(mouseInput);
        this.addMouseMotionListener(mouseInput);
        this.addMouseWheelListener(mouseInput);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < main.getGame().getCellAmountX(); x++) {
            for (int y = 0; y < main.getGame().getCellAmountY(); y++) {
                if (main.getGame().getCell(x, y).isAlive()) {

                    if(main.getUi().getFilterButtonGroupActionCommand().equals("Rainbow")) {
                        if (main.getGame().getCell(x, y).getLivedGeneration() == 1) {
                            g.setColor(new Color(255, 0, 0));
                        } else if (main.getGame().getCell(x, y).getLivedGeneration() == 2) {
                            g.setColor(new Color(255, 150, 0));
                        } else if (main.getGame().getCell(x, y).getLivedGeneration() == 3) {
                            g.setColor(new Color(255, 255, 0));
                        } else if (main.getGame().getCell(x, y).getLivedGeneration() == 4) {
                            g.setColor(new Color(0, 255, 0));
                        } else if (main.getGame().getCell(x, y).getLivedGeneration() == 5) {
                            g.setColor(new Color(0, 0, 255));
                        }

                    } else {
                        g.setColor(new Color(100, 100, 100));
                    }
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                } else {
                    g.setColor(new Color(43, 43, 43));
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);

                    drawFilter(g, x, y);
                }
            }
        }

        if (main.getUi().isdDrawGrid()) {
            for (int x = 0; x < main.getGame().getCellAmountX(); x++) {
                for (int y = 0; y < main.getGame().getCellAmountY(); y++) {
                    g.setColor(Color.BLACK);
                    g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }

        if (startDragX != -1 && startDragY != -1 && endDragX != -1 && endDragY != -1) drawSelectedCells(g);

        if (main.getUi().getPatternManager().isPatternSelected) drawCurrentPattern(g);

        //draws Amount of neighbors
        /*for (int x = 0; x < main.getGame().getCellAmountX(); x++) {
            for (int y = 0; y < main.getGame().getCellAmountY(); y++) {
                g.setColor(Color.GRAY);
                g.drawString(String.valueOf(main.getGame().getCell(x, y).getNeighbors()), x * cellSize, y * cellSize + cellSize);
            }
        }*/

        repaint();
    }


    private void drawFilter(Graphics g, int cellX, int cellY) {
        if (main.getUi().getFilterButtonGroupActionCommand().equals("Tail")) {
            if (main.getGame().getCell(cellX, cellY).getAlpha() > 0) {
                g.setColor(new Color(100, 100, 100, main.getGame().getCell(cellX, cellY).getAlpha()));
                g.fillRect(cellX * cellSize, cellY * cellSize, cellSize, cellSize);
            }
        }
    }


    private void drawCurrentPattern(Graphics g) {
        if (main.getUi().getPatternManager().getCurrentPattern() != null) {
            boolean[][] test = main.getUi().getPatternManager().getCurrentPattern().getCells();

            for (int x = 0; x < test.length; x++) {
                for (int y = 0; y < test[0].length; y++) {
                    int drawX = (cursorX / cellSize * cellSize - test.length * cellSize) + x * cellSize;
                    int drawY = (cursorY / cellSize * cellSize - test[0].length * cellSize) + y * cellSize;
                    if (test[x][y]) {
                        g.setColor(Color.GRAY);
                        g.fillRect(drawX, drawY, cellSize, cellSize);
                    } else {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(drawX, drawY, cellSize, cellSize);
                    }
                    if (main.getUi().isdDrawGrid()) {
                        g.setColor(Color.BLACK);
                        g.drawRect(drawX, drawY, cellSize, cellSize);
                    }

                }
            }
            g.setColor(new Color(0, 49, 164));
            g.drawRect(cursorX / cellSize * cellSize - test.length * cellSize, cursorY / cellSize * cellSize - test[0].length * cellSize, test.length * cellSize, test[0].length * cellSize);
        }
    }

    private void drawSelectedCells(Graphics g) {
        g.setColor(new Color(0, 49, 164));
        g.drawRect(getFirstCellX() * cellSize, getFirstCellY() * cellSize, (getLastCellX() - getFirstCellX()) * cellSize, (getLastCellY() - getFirstCellY()) * cellSize);
    }


    public class MouseInput extends MouseAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            cursorX = e.getX();
            cursorY = e.getY();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (main.getUi().getPatternManager().isPatternSelected) {
                if (e.getButton() == 1) {
                    main.getUi().getPatternManager().placeCurrentPattern((cursorX / cellSize * cellSize) / cellSize, (cursorY / cellSize * cellSize) / cellSize);
                } else if (e.getButton() == 3) {
                    main.getUi().getPatternManager().isPatternSelected = false;
                }
            } else {
                if (e.getButton() == 1) {
                    main.getGame().changeCellState(e.getX() / cellSize, e.getY() / cellSize);
                } else if (e.getButton() == 3) {
                    if (!main.getUi().getPatternManager().isSelectionTool())
                        main.getUi().getPatternManager().isPatternSelected = true;
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {
            //1 = left, 3 = right, 2 = mousewheel
            if (e.getButton() == 3) {
                resetDrag();
            } else if (e.getButton() == 1) {
                if (main.getUi().getPatternManager().isSelectionTool()) {
                    startDragX = e.getX();
                    startDragY = e.getY();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (main.getGame().getCellAmountX() * cellSize > e.getX() && main.getGame().getCellAmountY() * cellSize > e.getY()) {
                if (main.getUi().getPatternManager().isSelectionTool()) {
                    endDragX = e.getX();
                    endDragY = e.getY();
                } else {
                    if (!main.getUi().getPatternManager().isPatternSelected) {
                        if (!main.getGame().getCell(e.getX() / cellSize, e.getY() / cellSize).isAlive()) {
                            main.getGame().changeCellState(e.getX() / cellSize, e.getY() / cellSize);
                        }
                    }
                }
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (main.getUi().getPatternManager().isPatternSelected) {
                if (e.getWheelRotation() == -1) {
                    main.getUi().getPatternManager().getCurrentPattern().rotateClockwise();
                } else if (e.getWheelRotation() == 1) {
                    main.getUi().getPatternManager().getCurrentPattern().rotateCounterClockwise();
                }
            }
        }
    }

    public void resetDrag() {
        startDragX = startDragY = endDragX = endDragY = -1;
    }

    public int getFirstCellX() {
        if (startDragX > endDragX) {
            return endDragX / cellSize;
        } else {
            return startDragX / cellSize;
        }
    }

    public int getFirstCellY() {
        if (startDragY > endDragY) {
            return endDragY / cellSize;
        } else {
            return startDragY / cellSize;
        }
    }

    public int getLastCellX() {
        if (startDragX > endDragX) {
            return startDragX / cellSize + 1;
        } else {
            return endDragX / cellSize + 1;
        }
    }

    public int getLastCellY() {
        if (startDragY > endDragY) {
            return startDragY / cellSize + 1;
        } else {
            return endDragY / cellSize + 1;
        }
    }
}
