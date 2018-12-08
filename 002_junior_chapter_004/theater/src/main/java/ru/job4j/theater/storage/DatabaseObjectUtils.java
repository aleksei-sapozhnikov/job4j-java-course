package ru.job4j.theater.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Payment;
import ru.job4j.theater.model.Seat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility static methods to use with databaseRepositories.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class DatabaseObjectUtils {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(DatabaseObjectUtils.class);

    /**
     * Utility class. Methods to fill for different repository objects.
     *
     * @author Aleksei Sapozhnikov (vermucht@gmail.com)
     * @version 0.1
     * @since 0.1
     */
    public static class FillStatement {
        /**
         * Logger.
         */
        @SuppressWarnings("unused")
        private static final Logger LOG = LogManager.getLogger(FillStatement.class);

        public static class AccountStatements {
            /**
             * Fills statement to add account.
             *
             * @param statement Statement to fill.
             * @param account   Account to add.
             * @throws SQLException If problems in database occur.
             */
            public static void fillAdd(PreparedStatement statement, Account account) throws SQLException {
                int i = 0;
                statement.setString(++i, account.getName());        // name
                statement.setString(++i, account.getPhone());       // phone
            }

            /**
             * Fills prepared statement with needed values.
             *
             * @param statement Statement to fill.
             * @param name      Account name to find.
             * @param phone     Phone to find.
             * @throws SQLException If problems with database occur.
             */
            public static void fillGetByNamePhone(PreparedStatement statement, String name, String phone) throws SQLException {
                int i = 0;
                statement.setString(++i, name);
                statement.setString(++i, phone);
            }
        }

        public static class SeatStatements {

            /**
             * Fills given statement with needed values.
             *
             * @param statement Statement to fill.
             * @param seat      Object with information to fill into statement.
             * @throws SQLException If problems with database occur.
             */
            public static void fillAdd(PreparedStatement statement, Seat seat) throws SQLException {
                int i = 0;
                statement.setInt(++i, seat.getRow());           // row
                statement.setInt(++i, seat.getColumn());        // column
                statement.setInt(++i, seat.getPrice());         // price
                statement.setString(++i, seat.getOwner().getName());    // owner name
                statement.setString(++i, seat.getOwner().getPhone());   // owner phone
            }

            /**
             * Fills given statement with needed values.
             *
             * @param statement Statement to fill.
             * @param newSeat   Object with information to fill into statement.
             * @throws SQLException If problems with database occur.
             */
            public static void fillUpdateByPlace(PreparedStatement statement, int row, int column, Seat newSeat) throws SQLException {
                int i = 0;
                statement.setInt(++i, newSeat.getRow());            // set row
                statement.setInt(++i, newSeat.getColumn());         // set column
                statement.setInt(++i, newSeat.getPrice());          // set price
                statement.setString(++i, newSeat.getOwner().getName());         // find owner name
                statement.setString(++i, newSeat.getOwner().getPhone());        // find owner phone
                statement.setInt(++i, row);                    // find by row
                statement.setInt(++i, column);                 // find by column
            }

            /**
             * Fills given statement with needed values.
             *
             * @param statement Statement to fill.
             * @param row       Row of the seat we're looking for.
             * @param column    Column of the seat we're looking for.
             * @throws SQLException If problems with database occur.
             */
            public static void fillGetByPlace(PreparedStatement statement, int row, int column) throws SQLException {
                int i = 0;
                statement.setInt(++i, row);
                statement.setInt(++i, column);
            }
        }

        public static class PaymentStatements {

            /**
             * Fills statement to add payment.
             *
             * @param statement Statement to fill.
             * @param payment   Payment to add.
             * @throws SQLException If problems in database occur.
             */
            public static void fillAdd(PreparedStatement statement, Payment payment) throws SQLException {
                int i = 0;
                statement.setInt(++i, payment.getAmount());                 // amount
                statement.setString(++i, payment.getFrom().getName());      // from name
                statement.setString(++i, payment.getFrom().getPhone());     // from phone
                statement.setString(++i, payment.getComment());             // comment
            }


        }


    }

    /**
     * Contains static methods to form objects from result sets.
     *
     * @author Aleksei Sapozhnikov (vermucht@gmail.com)
     * @version 0.1
     * @since 0.1
     */
    public static class FormObject {
        /**
         * Logger.
         */
        @SuppressWarnings("unused")
        private static final Logger LOG = LogManager.getLogger(FormObject.class);

        /**
         * Forms and returns Account object from given ResultSet.
         *
         * @param res ResultSet object.
         * @return Account object.
         * @throws SQLException If problems with database occur.
         */
        public static Account formAccount(ResultSet res) throws SQLException {
            int i = 0;
            Account.Builder account = new Account.Builder(
                    res.getString(++i),         // name
                    res.getString(++i)          // phone
            );
            return account.build();
        }

        /**
         * Forms and returns Payment object from given ResultSet.
         *
         * @param res ResultSet object.
         * @return Payment object.
         * @throws SQLException If problems with database occur.
         */
        public static Payment formPayment(ResultSet res) throws SQLException {
            Account from = new Account.Builder(
                    res.getString("from_name"),         // name
                    res.getString("from_phone")         // phone
            ).build();
            Payment.Builder payment = new Payment.Builder(
                    res.getInt("payment_amount"),       // amount
                    from                                // from account
            );
            String comment = res.getString("payment_comment");
            if (comment != null) {
                payment.comment(comment);
            }
            return payment.build();
        }

        /**
         * Forms and returns Seat object from given ResultSet.
         *
         * @param res ResultSet object.
         * @return Seat object.
         * @throws SQLException If problems with database occur.
         */
        public static Seat formSeat(ResultSet res) throws SQLException {
            int i = 0;
            Seat.Builder seat = new Seat.Builder(
                    res.getInt(++i),        // row
                    res.getInt(++i),        // column
                    res.getInt(++i)         // price
            );
            if (res.getInt(++i) != 0) {                         // if owner_id is not null
                seat = seat.owner(new Account.Builder(
                        res.getString(++i),                     // name
                        res.getString(++i)                      // phone
                ).build());
            }
            return seat.build();
        }
    }
}
