package ru.job4j.tictactoe.player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.tictactoe.cell.ICell;
import ru.job4j.util.function.ConsumerEx;
import ru.job4j.util.function.SupplierEx;

/**
 * Player taking moves as entered by human from console.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ConsoleInputHuman implements IPlayer {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(ConsoleInputHuman.class);
    /**
     * User commands supplier.
     */
    private SupplierEx<String> in;
    /**
     * To-user messages consumer.
     */
    private ConsumerEx<String> out;

    /**
     * Constructor.
     *
     * @param in  User commands supplier.
     * @param out To-user messages consumer.
     */
    public ConsoleInputHuman(SupplierEx<String> in, ConsumerEx<String> out) {
        this.in = in;
        this.out = out;
    }

    /**
     * Returns next move input by user.
     *
     * @param table Game table.
     * @return Next move coordinates.
     * @throws Exception If I/O problem happens.
     */
    @Override
    public Move doMove(ICell[][] table) throws Exception {
        String input;
        do {
            this.out.accept("Enter coordinates divided by space (e.g.: 3 2)");
            input = this.in.get();
        } while (!(input.matches("\\d+ \\d+")));
        var coords = input.split(" ");
        var iHeight = Integer.parseInt(coords[0]);
        var iWidth = Integer.parseInt(coords[1]);
        return new Move(iHeight, iWidth);

    }
}
