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
    private int figuresPosition = 0;

    /**
     * Add new figure to array.
     *
     * @param figure Figure to add.
     */
    public void add(Figure figure) {
        this.figures[this.figuresPosition++] = figure;
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
        // Что в заданной ячейки есть фигура. если нет. то выкинуть исключение
        boolean exists = false;
        Figure figure = null;
        for (Figure tempFig : figures) {
            if (source.equals(tempFig.getPosition())) {
                exists = true;
                figure = tempFig;
                break;
            }
        }
        if (!exists) throw new FigureNotFoundException("No figure found in this cell.");
        // Если фигура есть. Проверить может ли она так двигаться. Если нет то упадет исключение
        if (dest.x > MAX_X || dest.x < MIN_X) throw new ImpossibleMoveException("Figure goes out of the chessboard");
        if (dest.y > MAX_Y || dest.y < MIN_Y) throw new ImpossibleMoveException("Figure goes out of the chessboard");
        Cell[] way = figure.way(source, dest);
        // Проверить что полученный путь. не занят фигурами. Если занят выкинуть исключение
        boolean blocked = false;
        for (Cell tempCell : way) {
            for (Figure tempFig : this.figures) {
                if (tempCell.equals(tempFig.getPosition())) {
                    blocked = true;
                    break;
                }
            }
        }
        if (blocked) throw new OccupiedWayException("The way is blocked by another figure.");
        // Если все отлично. Записать в ячейку новое новое положение Figure figure.copy(Cell dest)
        this.replace(figure, figure.copy(dest));
        return true;
    }
}
