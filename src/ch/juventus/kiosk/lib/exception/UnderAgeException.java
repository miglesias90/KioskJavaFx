/**
 * excpetion for no authorized buy of underaged
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.exception;

public class UnderAgeException extends Exception {
    private int age;

    public UnderAgeException(int age) {
        super(age + " years: To young for buying this article");
        this.age = age;
    }
}
