/**
 * The class TestDataStorage is a test class for the class DataSorage.
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.data.test;

import ch.juventus.kiosk.data.impl.DataStore;
import ch.juventus.kiosk.lib.entity.businessunit.impl.Kiosk;
import ch.juventus.kiosk.lib.entity.businessunit.impl.Supplier;
import ch.juventus.kiosk.lib.entity.businessunit.type.KioskState;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class TestDataStorage {

   @Test
   public void testDataStore() {
       Kiosk kiosk1 = null;

       String name = "Bhf Altstetten";

       DataStore.getInstance().addKiosk(name, "Altstetten", KioskState.CLOSED, 2000.00, new HashSet<>(), new HashMap<>(), new Supplier("test", new HashSet<>()));
       for (Kiosk kiosk : DataStore.getInstance().getKioskList()) {
           if(kiosk.getName() == name)
               kiosk1 = kiosk;
       }

       assertNotNull("Kiosk object not found", kiosk1);


       KioskState oldState = kiosk1.getState();

       DataStore.getInstance().changeKioskState(kiosk1);

       assertNotEquals("State could not change", oldState, kiosk1.getState());

   }
}
