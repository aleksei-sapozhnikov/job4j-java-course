package ru.job4j.tictactoe;

public class Logic3T {
    private final Figure3T[][] table;

    public Logic3T(Figure3T[][] table) {
        this.table = table;
    }

    public boolean isWinnerX() {
        return this.checkWinner(true);
    }

    public boolean isWinnerO() {
        return this.checkWinner(false);
    }

    private boolean checkWinner(boolean hasMarkX) {
        boolean result = false;
        for (int i = 0; !result && i < table.length; i++) {
            result = this.isLineFilled(i, 0, table.length - 1, true, hasMarkX);
        }
        for (int i = 0; !result && i < table[0].length; i++) {
            result = this.isLineFilled(0, i, table.length - 1, false, hasMarkX);
        }
        result = result || this.isDiagonalFilled(0, 0, table.length - 1, table.length - 1, hasMarkX);
        result = result || this.isDiagonalFilled(table.length - 1, 0, 0, table.length - 1, hasMarkX);
        return result;
    }

    private boolean isLineFilled(int xStart, int yStart, int end, boolean horizontal, boolean hasMarkX) {
        boolean result = true;
        int x = xStart;
        int y = yStart;
        while (horizontal ? y <= end : x <= end) {
            boolean condition = hasMarkX ? table[x][y].hasMarkX() : table[x][y].hasMarkO();
            if (!condition) {
                result = false;
                break;
            }
            x = horizontal ? x : x + 1;
            y = horizontal ? y + 1 : y;
        }
        return result;
    }

    boolean isDiagonalFilled(int xStart, int yStart, int xEnd, int yEnd, boolean hasMarkX) {
        if (Math.abs(xStart - xEnd) != Math.abs(yStart - yEnd)) {
            throw new RuntimeException("No diagonal by given coordinates");
        }
        boolean result = true;
        boolean downRight = xEnd > xStart;
        int x = xStart;
        int y = yStart;
        while (y <= yEnd) {
            boolean condition = hasMarkX ? table[x][y].hasMarkX() : table[x][y].hasMarkO();
            if (!condition) {
                result = false;
                break;
            }
            x = downRight ? x + 1 : x - 1;
            y++;
        }
        return result;
    }

    public boolean hasGap() {
        boolean result = false;
        out:
        for (Figure3T[] line : table) {
            for (Figure3T cell : line) {
                if (!cell.hasMarkX() && !cell.hasMarkO()) {
                    result = true;
                    break out;
                }
            }
        }
        return result;
    }
}

