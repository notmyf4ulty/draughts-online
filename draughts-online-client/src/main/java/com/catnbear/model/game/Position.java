package com.catnbear.model.game;

public class Position {
    private final int x;
    private final int y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    boolean isOneFieldDiagonalNeighbour(Position position) {
        return (Math.abs(x - position.getX()) == 1) && (Math.abs(y - position.getY()) == 1);
    }

    boolean isTwoFieldsDiagonalNeighbourhood(Position position) {
        return (Math.abs(x - position.getX()) == 2) && (Math.abs(y - position.getY()) == 2);
    }

    public boolean isXySumEven() {
        return (x + y) % 2 == 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
