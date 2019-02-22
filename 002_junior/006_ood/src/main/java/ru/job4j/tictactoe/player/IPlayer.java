package ru.job4j.tictactoe.player;

import ru.job4j.tictactoe.cell.ICell;

import java.util.Objects;

/**
 * Player object.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface IPlayer {

    /**
     * Returns next move input by user.
     *
     * @param table Game table.
     * @return Next move coordinates.
     * @throws Exception In case of problems.
     */
    Move doMove(ICell[][] table) throws Exception;

    /**
     * Class holding move coordinates.
     */
    class Move {
        /**
         * Height index.
         */
        private final int iHeight;
        /**
         * Width index.
         */
        private final int iWidth;

        /**
         * Consturctor.
         *
         * @param iHeight Height index.
         * @param iWidth  Width index.
         */
        public Move(int iHeight, int iWidth) {
            this.iHeight = iHeight;
            this.iWidth = iWidth;
        }

        /**
         * Returns iHeight.
         *
         * @return Value of iHeight field.
         */
        public int getIHeight() {
            return this.iHeight;
        }

        /**
         * Returns iWidth.
         *
         * @return Value of iWidth field.
         */
        public int getIWidth() {
            return this.iWidth;
        }

        /**
         * Equals method.
         *
         * @param o Other object.
         * @return If objects are equal or not.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Move move = (Move) o;
            return iHeight == move.iHeight
                    && iWidth == move.iWidth;
        }

        /**
         * Returns hash code.
         *
         * @return Hash code.
         */
        @Override
        public int hashCode() {
            return Objects.hash(iHeight, iWidth);
        }
    }
}
