package components;

import java.util.ArrayList;


public class Snake extends ArrayList<Cell> {
    public Snake() {
        super();
        init();
    }

    public void init() {
        clear();
        add(new Cell(0, 2));
        add(new Cell(0, 1));
        add(new Cell(0, 0));
    }

    public Cell dropTail() {
        int tailIndex = size() - 1;
        Cell tail = get(tailIndex);
        remove(tailIndex);
        return tail;
    }

    public Cell getHead() {
        return get(0);
    }

    public void setHead(int x, int y) {
        Cell head = new Cell(x ,y);
        add(0, head);
    }

    public boolean cellInSnake(Cell cell) {
        boolean cellInSnake = false;
        for(Cell c : this) {
            if(c.getX() == cell.getX() && c.getY() == cell.getY()){
                cellInSnake = true;
                break;
            }
        }
        return cellInSnake;
    }

    public static Cell getNextHead(Cell currentHead, String direction) {
        int x = currentHead.getX();
        int y = currentHead.getY();
        int dx = 0;
        int dy = 0;
        if(direction == "Up") {
            dy = -1;
        } else if(direction == "Right") {
            dx = 1;
        } else if(direction == "Down") {
            dy = 1;
        } else if(direction == "Left") {
            dx = -1;
        }

        return new Cell(x + dx, y + dy);
    }
}