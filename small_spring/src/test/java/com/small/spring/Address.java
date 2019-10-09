package com.small.spring;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 10/8/19 4:42 PM
 */
public class Address implements Liveable {
    private String local;
    private Driveable car;

    public Driveable getCar() {
        return car;
    }

    public void setCar(Driveable car) {
        this.car = car;
    }

    public Address() {
    }

    @Override
    public String toString() {
        return "Address{" +
                "local='" + local + '\'' +
                '}';
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public void living() {
        System.out.println("address is living");
    }
}