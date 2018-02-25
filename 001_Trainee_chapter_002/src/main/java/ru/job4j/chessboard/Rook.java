package ru.job4j.chessboard;

import java.util.Arrays;

class Rook extends Figure {

    /**
     * Constructor.
     *
     * @param position Cell where the figure is.
     */
    Rook(Cell position) {
        super(position);
    }

    /**
     * Creates figure in the cell.
     *
     * @param dest cell where to create figure.
     * @return Figure with new coordinates.
     */
    @Override
    Rook copy(Cell dest) {
        return new Rook(dest);
    }

    /**
     * Returns an array of cells which figure will pass to go to destination cell.
     *
     * @param source Cell where figure starts from.
     * @param dest   Cell where we want it to go.
     * @return Array of cells which figure will pass going from source cell to destination.
     * @throws ImpossibleMoveException if figure movement cannot reach destination position.
     */
    @Override
    Cell[] way(Cell source, Cell dest) throws ImpossibleMoveException {
        if (source.horizontalDistanceTo(dest) != 0 && source.verticalDistanceTo(dest) != 0) {
            throw new ImpossibleMoveException("Rook figure cannot move like this.");
        }
        Enum horizontal;
        Enum vertical;
        if (source.horizontalDistanceTo(dest) == 0) {
            horizontal = Cell.HorizontalDirection.NONE;
            vertical = source.verticalDistanceTo(dest) > 0 ? Cell.VerticalDirection.UP : Cell.VerticalDirection.DOWN;
        } else {
            vertical = Cell.VerticalDirection.NONE;
            horizontal = source.horizontalDistanceTo(dest) > 0 ? Cell.HorizontalDirection.RIGHT : Cell.HorizontalDirection.LEFT;
        }
        Cell[] temp = new Cell[100];
        int pos = 0;
        Cell current = source.step(horizontal, vertical);
        while (!current.equals(dest)) {
            temp[pos++] = current;
            current = current.step(horizontal, vertical);
        }
        temp[pos++] = current;
        return Arrays.copyOf(temp, pos);
    }

}
