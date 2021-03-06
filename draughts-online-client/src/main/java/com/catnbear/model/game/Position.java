package com.catnbear.model.game;

import javafx.geometry.Pos;

import java.util.ArrayList;

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

    public Position [] getDiagonalNeighboursPositions(int distance, int borderDimension) {
        ArrayList<Position> candidatePositions = new ArrayList<>();
        candidatePositions.add(new Position(x + distance, y + distance));
        candidatePositions.add(new Position(x + distance, y - distance));
        candidatePositions.add(new Position(x - distance, y - distance));
        candidatePositions.add(new Position(x - distance, y + distance));

        ArrayList<Position> positionsToDelete = new ArrayList<>();
        for (Position position : candidatePositions) {
            if (position.getX() >= borderDimension ||
                    position.getY() >= borderDimension ||
                    position.getX() < 0 ||
                    position.getY() < 0) {
                positionsToDelete.add(position);
            }
        }
        candidatePositions.removeAll(positionsToDelete);

        Position [] candidatesPositionsArray = new Position [candidatePositions.size()];
        candidatesPositionsArray = candidatePositions.toArray(candidatesPositionsArray);

        return candidatesPositionsArray;
    }

    static MoveDistance getMoveDistance(Position fromPosition, Position toPosition) {
        int xDelta = Math.abs(fromPosition.getX() - toPosition.getX());
        int yDelta = Math.abs(fromPosition.getY() - toPosition.getY());
        if ((xDelta == yDelta)) {
            if (xDelta == 1) {
                return MoveDistance.ONE_FIELD_DISTANCE;
            } else {
                return MoveDistance.TWO_FIELDS_DISTANCE;
            }
        } else {
            return MoveDistance.INCORRECT_DISTANCE;
        }
    }

    public Position getCopy() {
        return new Position(x,y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
