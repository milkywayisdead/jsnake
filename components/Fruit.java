package components;

import java.util.Random;


public class Fruit extends Cell {
    public Fruit(int x, int y) {
        super(x, y);
    }

    public static Fruit randomFruit(int xUpperLimit, int yUpperLimit) {
        Random rand = new Random();
        int fruitX = rand.nextInt(0, xUpperLimit);
        int fruitY = rand.nextInt(0, yUpperLimit);
        Fruit fruit = new Fruit(fruitX, fruitY);
        return fruit;
    }
}

