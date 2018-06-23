/**
 * enum for kiosk state
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.entity.businessunit.type;

public enum KioskState {
    OPEN("Offen"),
    CLOSED("Geschlossen");

    private String text;

    KioskState(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static KioskState findByText(String text) {
        for (KioskState kioskState : KioskState.values()) {
            if (kioskState.getText().equals(text)) {
                return kioskState;
            }
        }

        return null;
    }
}
