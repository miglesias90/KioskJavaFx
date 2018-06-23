/**
 * test class for command handler
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.logic.test;

import ch.juventus.kiosk.lib.entity.businessunit.impl.Kiosk;
import ch.juventus.kiosk.lib.entity.businessunit.type.KioskState;
import ch.juventus.kiosk.logic.impl.CommandHandler;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class TestCommandHandler {

    @Test
    public void testDCommandHandler() {
        Kiosk kiosk1 = null;

        String name = "Bhf Altstetten";

        try {
            CommandHandler.getInstance().addKisok(name, "Altstetten", 2500.00, new HashSet<>());
            kiosk1 = CommandHandler.getInstance().getKioskByName(name);

            assertNotNull("Kiosk object not found", kiosk1);


            KioskState oldState = kiosk1.getState();

            CommandHandler.getInstance().changeKioskState(kiosk1);

            assertNotEquals("State could not change", oldState, kiosk1.getState());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
