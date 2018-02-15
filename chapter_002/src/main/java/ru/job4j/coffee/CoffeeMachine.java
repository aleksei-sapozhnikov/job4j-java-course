package ru.job4j.coffee;

import java.util.Arrays;

/**
 * Coffee machine taking money: giving coffee and change.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class CoffeeMachine {

    /**
     * Possible values of coins.
     */
    private final int[] coinValues = {1, 2, 5, 10};

    /**
     * Gives change using coins only.
     *
     * @param price price to be paid.
     * @param given money given by user.
     * @return change as array of coins.
     */
    int[] change(int given, int price) throws NotEnoughMoneyException {
        int left = given - price;
        if (left < 0) {
            throw new NotEnoughMoneyException("Not enough money given to buy coffee.");
        }
        int[] result = new int[left];
        int iResult = 0;
        int iCoin = this.coinValues.length - 1;
        while (left != 0 && iCoin >= 0) {
            int coin = this.coinValues[iCoin];
            int n = left / coin;
            for (int i = 0; i < n; i++) {
                result[iResult++] = coin;
            }
            left %= coin;
            iCoin--;
        }
        return Arrays.copyOf(result, iResult);
    }

}