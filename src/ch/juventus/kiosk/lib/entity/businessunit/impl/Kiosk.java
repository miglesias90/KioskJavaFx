/**
 * class for business unit kiosk
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.lib.entity.businessunit.impl;

import ch.juventus.kiosk.lib.entity.businessunit.type.KioskState;

import java.util.Set;


public class Kiosk {
    private String name;
    private String location;
    private KioskState state;
    private Double cash;
    private Set<String> employees;
    private Supplier supplier;



    public Kiosk(String name, String location, KioskState state, Double cash, Set<String> employees, Supplier supplier) {
        this.name = name;
        this.location = location;
        this.state = state;
        this.cash = cash;
        this.employees = employees;
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public KioskState getState() {
        return state;
    }

    public String getStateText() {
        return state.getText();
    }

    public Double getCash() {
        return cash;
    }

    public Set<String> getEmployees() {
        return employees;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * Add cash to cashier
     *
     * @param amount cash to add
     */
    public void addCash(double amount) {
        cash += amount;
    }

    /**
     * Remove cash from cashier
     *
     * @param amount cash to remove
     */
    public void restCash(double amount) {
        cash -= amount;
    }

    /**
     * Changes state to the opposite
     */
    public void changeState() {
        if(state == KioskState.CLOSED) {
            state = KioskState.OPEN;
        } else {
            state = KioskState.CLOSED;
        }
    }
}
