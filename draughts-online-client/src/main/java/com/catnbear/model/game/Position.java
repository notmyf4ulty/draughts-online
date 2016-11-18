package com.catnbear.model.game;

import java.util.ArrayList;

/**
 * Position's class. Enables fields to be placed in a appropriate order.
 */
public class Position {
    /**
     * x coordinate.
     */
    private final int x;

    /**
     * y coordinate.
     */
    private final int y;

    /**
     * Class' constructor.
     * @param x New x coordinate.
     * @param y New y coordinate.
     */
    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Checks whether x and y sum is even.
     * @return True if it's even. False otherwise.
     */
    public boolean isXySumEven() {
        return (x + y) % 2 == 0;
    }

    /**
     * Gets the positions of the neighbouring fields.
     * @param distance diagonal distance (in 'fields' number).
     * @param borderDimension Dimension of the board's border.
     * @return Array of the available positions. It doesn't have a fixed size because of the fact, that sometimes
     * position can be on the edge of the board.
     */
    Position [] getDiagonalNeighboursPositions(int distance, int borderDimension) {
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

    /**
     * Gets the move distance from the start to the stop position.
     * @param fromPosition Start position.
     * @param toPosition Stop position.
     * @return A MoveDistance.
     */
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

    /**
     * Getter of the Position object's copy.
     * @return Position object's copy
     */
    Position getCopy() {
        return new Position(x,y);
    }

    /**
     * Getter of the x coordinate.
     * @return x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter of the y coordinate.
     * @return y coordinate.
     */
    public int getY() {
        return y;
    }
}
