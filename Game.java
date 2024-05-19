import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import components.*;

import java.lang.Thread;


public class Game {
    private int colsNumber = 20;
    private int rowsNumber = 20;
    private int cellSize = 20;
    private Snake snake = new Snake();
    private String direction = "Down";
    private GameField gameField;
    private boolean playing = false;
    private int speed = 5;
    private int baseMsBetweenSteps = 500;
    private Thread gameThread;
    private Color snakeColor = Color.ORANGE;
    private Color bgColor = Color.GRAY;
    private Fruit fruit = new Fruit(0, 0);
    private Color fruitColor = Color.RED;
    private Label snakeSizeLabel;

    Game() {
        Frame mainFrame = createMainFrame();

        Panel gameFieldPanel = new Panel(new FlowLayout());
        this.gameField = new GameField(colsNumber, rowsNumber, cellSize) {
            @Override
            public void paint(Graphics g) {
                paintSnake();
                gameField.paintCell(fruit, fruitColor);
            }
        };
        gameFieldPanel.add(gameField);

        Panel buttonsPanel = createButtonsPanel();
        Label snakeSizeLabel = new Label();
        this.snakeSizeLabel = snakeSizeLabel;
        buttonsPanel.add(snakeSizeLabel);

        mainFrame.add(gameFieldPanel);
        mainFrame.add(buttonsPanel);
        updateSnakeSize();
        mainFrame.setVisible(true);
        
        setNewFruit();
    }

    private Frame createMainFrame() {
        Frame mainFrame = new Frame();
        mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));
        mainFrame.setSize(500, 500);
        mainFrame.setTitle("Змейка");
        mainFrame.setResizable(false);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stop();
                mainFrame.dispose();
            }
        });
        mainFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(!playing) return;
                String key = KeyEvent.getKeyText(e.getKeyCode());
                if(canChangeDirection(key)) {
                    direction = key;
                }
            }
        });
        return mainFrame;
    }

    private Panel createButtonsPanel() {
        Panel buttonsPanel = new Panel(new FlowLayout());
        Button startButton = new Button();
        startButton.setLabel("Старт");
        startButton.setFocusable(false);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        Button stopButton = new Button();
        stopButton.setLabel("Стоп");
        stopButton.setFocusable(false);
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
        Button restartButton = new Button();
        restartButton.setLabel("Заново");
        restartButton.setFocusable(false);
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stop();
                while(gameThread.isAlive()) {}
                eraseSnake();
                snake.init();
                updateSnakeSize();
                direction = "Down";
                paintSnake();
                start();
            }
        });
        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(restartButton);
        return buttonsPanel;
    }

    public void paintHead() {
        gameField.paintCell(snake.getHead(), snakeColor);
    }

    public void paintSnake() {
        gameField.paintCells(snake, snakeColor);
    }

    private boolean canChangeDirection(String direction) {
        if(this.direction == "Up" && direction != "Down") {
            return true;
        }
        if(this.direction == "Down" && direction != "Up") {
            return true;
        }
        if(this.direction == "Left" && direction != "Right") {
            return true;
        }
        if(this.direction == "Right" && direction != "Left") {
            return true;
        }
        return false;
    }

    public void start() {
        if(playing) return;
        playing = true;
        gameThread = getNewGameThread();
        gameField.hideGameOver();
        gameThread.start();
    }

    public Thread getNewGameThread() {
        Game _this = this;
        return new Thread() {
            @Override
            public void run() {
                while(playing) {
                    try {
                        Thread.sleep(baseMsBetweenSteps / speed);
                    } catch(InterruptedException err) {}

                    if(!playing) return;

                    Cell newHead = Snake.getNextHead(snake.getHead(), direction);
                    boolean shouldGrow = gotTheFruit(newHead);
                    if(shouldGrow) {
                        snake.setHead(fruit.getX(), fruit.getY());
                        paintHead();
                        _this.updateSnakeSize();
                        putAFruit();
                    }

                    newHead = Snake.getNextHead(snake.getHead(), direction);
                    boolean canMove = !gameField.checkOutOfBounds(newHead) && !snake.cellInSnake(newHead);
                    if(canMove) {
                        Cell tail = snake.dropTail();
                        gameField.paintCell(tail, bgColor);
                        snake.setHead(newHead.getX(), newHead.getY());
                        paintHead();
                    } else {
                        _this.stop();
                        gameField.showGameOver();
                    }
                }
            }
        };
    }

    public void stop() {
        playing = false;
    }

    private void eraseSnake() {
        gameField.paintCells(snake, bgColor);
    }

    private void putAFruit() {
        setNewFruit();
        gameField.paintCell(fruit, fruitColor);
    }

    private void setNewFruit() {
        while(snake.cellInSnake(fruit)) {
            fruit = Fruit.randomFruit(colsNumber, rowsNumber);
        }
    }

    private boolean gotTheFruit(Cell head) {
        return head.getX() == fruit.getX() && head.getY() == fruit.getY();
    }

    private void updateSnakeSize() {
        snakeSizeLabel.setText(String.format("Длина змейки: %d", snake.size()));
    }

    public static void main(String[] args) {
        new Game();
    }
}
