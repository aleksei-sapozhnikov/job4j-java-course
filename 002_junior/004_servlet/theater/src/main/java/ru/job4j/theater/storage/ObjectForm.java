package ru.job4j.theater.storage;

import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Payment;
import ru.job4j.theater.model.Seat;
import ru.job4j.util.database.DbExecutor.ObjValue;

import java.util.Map;

/**
 * Utility static methods to use with databaseRepositories.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ObjectForm {
    /**
     * Forms and returns Account object.
     *
     * @param row One row.
     * @return Account object.
     */
    public static Account formAccount(Map<Integer, ObjValue> row) {
        int i = 0;
        Account.Builder account = new Account.Builder(
                row.get(++i).asString(),         // name
                row.get(++i).asString()          // phone
        );
        return account.build();
    }

    /**
     * Forms and returns Payment object.
     *
     * @param row One row.
     * @return Payment object.
     */
    public static Payment formPayment(Map<Integer, ObjValue> row) {
        int i = 0;
        Account from = new Account.Builder(
                row.get(++i).asString(),         // name
                row.get(++i).asString()         // phone
        ).build();
        Payment.Builder payment = new Payment.Builder(
                row.get(++i).asInteger(),       // amount
                from                                // from account
        );
        String comment = row.get(++i).asString();
        if (comment != null) {
            payment.comment(comment);
        }
        return payment.build();
    }

    /**
     * Forms and returns Seat object.
     *
     * @param row One row.
     * @return Seat object.
     */
    public static Seat formSeat(Map<Integer, ObjValue> row) {
        int i = 0;
        Seat.Builder seat = new Seat.Builder(
                row.get(++i).asInteger(),        // row
                row.get(++i).asInteger(),        // column
                row.get(++i).asInteger()         // price
        );
        if (row.get(++i).asInteger() != 0) {                         // if owner_id is not null
            seat = seat.owner(new Account.Builder(
                    row.get(++i).asString(),                     // name
                    row.get(++i).asString()                      // phone
            ).build());
        }
        return seat.build();
    }
}

