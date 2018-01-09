package ru.job4j.loop;

/**
 * Class to draw chessboard.
 *
 * @author Aleksei Sapozhnikov
 * @version $Id$
 * @since 09.01.2018
 */
public class Board {

    /**
     * Forms a string with a chessboard.
     *
     * @param width  Board width (number of cells).
     * @param height Board height (number of cells).
     * @return String with chessboard.
     */
    public String paint(int width, int height) {
        StringBuilder screen = new StringBuilder();
        String ln = System.lineSeparator();
        // forming line
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if ((i + j) % 2 == 0) {
                    screen.append("X");
                } else {
                    screen.append(" ");
                }
            }
            // next line symbol
            screen.append(ln);
        }
        return screen.toString();
    }
}
