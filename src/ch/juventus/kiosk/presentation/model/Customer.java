package ch.juventus.kiosk.presentation.model;

import ch.juventus.kiosk.lib.entity.businessunit.impl.Kiosk;

public class Customer {
    private String name;
    private int age;
    private Kiosk kiosk;

    public Customer(String name, int age, Kiosk kiosk) {
        this.name = name;
        this.age = age;
        this.kiosk = kiosk;
    }


    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Kiosk getKiosk() {
        return kiosk;
    }
}
