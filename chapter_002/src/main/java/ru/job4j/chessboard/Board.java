package ru.job4j.chessboard;

import java.util.Arrays;

/**
 * Chessboard class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 03.02.2018
 */
public class Board {

    /**
     * Maximum and minimum cell coordinates (chessboard borders).
     */
    private static final int MIN_X = 0, MIN_Y = 0, MAX_X = 7, MAX_Y = 7;

    /**
     * Array of figures on the chessboard.
     */
    private Figure[] figures = new Figure[32];

    /**
     * Position of a new figure in figures array.
     */
    private int figArrPos = 0;

    /**
     * Add new figure to array.
     *
     * @param figure Figure to add.
     */
    public void add(Figure figure) {
        this.figures[this.figArrPos++] = figure;
    }

    /**
     * Replace existing figure with new figure (on a new cell).
     *
     * @param oldFigure Figure to replace.
     * @param newFigure New figure to this place.
     */
    private void replace(Figure oldFigure, Figure newFigure) {
        this.figures[
                Arrays.asList(this.figures).indexOf(oldFigure)
                ] = newFigure;
    }

    /**
     * Find figure which is in the specified position.
     *
     * @param position Position where to look.
     * @return Figure in the position or null if figure not found.
     */
    private Figure findFigureByPosition(Cell position) {
        Figure result = null;
        for (int i = 0; i < this.figArrPos; i++) {
            if (this.figures[i].isInPosition(position)) {
                result = this.figures[i];
                break;
            }
        }
        return result;
    }

    /**
     * Checks if there are figures in the given positions or not.
     *
     * @param positions Array of positions to check.
     * @return true if there is a figure in any of the given positions, false if not.
     */
    boolean arefiguresInPosititons(Cell[] positions) {
        boolean result = false;
        for (Cell tempCell : positions) {
            if (this.findFigureByPosition(tempCell) != null) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Moves figure from one cell to another if possible.
     *
     * @param source Cell to move figure from.
     * @param dest   Cell to move figure to.
     * @return true, if moved; false if it was not possible.
     * @throws ImpossibleMoveException If the figure cannot make this move.
     * @throws OccupiedWayException    If there is a block on the way (another figure).
     * @throws FigureNotFoundException If there is no figure in the source cell.
     */
    boolean move(Cell source, Cell dest) throws ImpossibleMoveException, OccupiedWayException, FigureNotFoundException {
        // Метод должен проверить
        // Что в заданной ячейки есть фигура. если нет. то выкинуть исключение
        Figure figure = this.findFigureByPosition(source);
        if (figure == null) {
            throw new FigureNotFoundException("No figure found in this cell.");
        }
        // Если фигура есть. Проверить может ли она так двигаться. Если нет то упадет исключение
        if (!dest.isInRange(MIN_X, MAX_X, MIN_Y, MAX_Y)) {
            throw new ImpossibleMoveException("Figure goes out of the chessboard.");
        }
        Cell[] way = figure.way(source, dest); // throws extension
        // Проверить что полученный путь. не занят фигурами. Если занят выкинуть исключение
        if (this.arefiguresInPosititons(way)) {
            throw new OccupiedWayException("The way is blocked by another figure.");
        }
        // Если все отлично. Записать в ячейку новое новое положение Figure figure.copy(Cell dest)
        this.replace(figure, figure.copy(dest));
        return true;
    }
}
