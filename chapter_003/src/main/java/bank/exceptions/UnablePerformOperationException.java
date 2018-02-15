package bank.exceptions;

/**
 * Exception thrown if there is not enough money in source to make an operation.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.02.2018
 */
public class UnablePerformOperationException extends Exception {

    /**
     * Constructor.
     *
     * @param msg messae to show.
     */
    public UnablePerformOperationException(String msg) {
        super(msg);
    }
}
