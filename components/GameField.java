package components;

import java.awt.*;


public class GameField extends Canvas {
    private int colsNumber;
    private int rowsNumber;
    private int cellSize;
    private String gameOverString = "Game Over";
    private Font gameOverFont;
    private Color gameOverColor = Color.BLACK;
    private Color bgColor = Color.GRAY;

    public GameField(int colsNumber, int rowsNumber, int cellSize) {
        super();
        this.colsNumber = colsNumber;
        this.rowsNumber = rowsNumber;
        this.cellSize = cellSize;
        this.gameOverFont = new Font("Arial", Font.BOLD, cellSize);
        this.setBackground(bgColor);
        this.setSize(colsNumber*cellSize, rowsNumber*cellSize);
    }

    public void paintCell(Cell cell, Color color) {
        Graphics g = this.getGraphics();
        g.setColor(color);
        g.fillRect(cellSize*cell.getX(), cellSize*cell.getY(), cellSize, cellSize);
    }

    public void paintCells(Snake cells, Color color) {
        for(Cell cell : cells) {
            paintCell(cell, color);
        }
    }

    public boolean checkOutOfBounds(Cell cell) {
        int cellX = cell.getX();
        int cellY = cell.getY();
        boolean outOfBounds = false;
        if(
            cellX > colsNumber - 1 || 
            cellX < 0 ||
            cellY > rowsNumber - 1 ||
            cellY < 0
        ) {
            outOfBounds = true;
        }
        return outOfBounds;
    }

    public void showGameOver() {
        Graphics g = getGraphics();
        g.setColor(gameOverColor);
        g.setFont(gameOverFont);
        g.drawString(gameOverString, getWidth()/2 - 50, getHeight()/2 + 10);
    }

    public void hideGameOver() {
        Graphics g = getGraphics();
        g.setColor(bgColor);
        g.setFont(gameOverFont);
        g.drawString(gameOverString, getWidth()/2 - 50, getHeight()/2 + 10);   
    }
}
