/**
 * excpetion for not enougth money
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.exception;

public class NoMoneyException extends Exception {
    private double cash;
    private double amountToPay;

    public NoMoneyException(double cash, double amountToPay) {
        super("You have only " + cash + " CHF an the bill is " + amountToPay + " CHF");
        this.cash = cash;
        this.amountToPay = amountToPay;
    }
}
