package ru.job4j.tictactoe.player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.tictactoe.cell.ICell;

/**
 * Stupid computer player.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class StupidComputer implements IPlayer {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(StupidComputer.class);

    /**
     * Returns next move input by user.
     *
     * @param table Game table.
     * @return Next move coordinates.
     */
    @Override
    public Move doMove(ICell[][] table) {
        Move result = null;
        out:
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                var cell = table[i][j];
                if (!cell.hasMarkX() && !cell.hasMarkO()) {
                    result = new Move(i, j);
                    break out;
                }
            }
        }
        return result;
    }
}
