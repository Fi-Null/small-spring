package com.small.spring;

/**
 * @author null
 * @version 1.0
 * @title
 * @description
 * @createDate 10/8/19 2:37 PM
 */
public class Car implements Driveable {
    private String name;
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private Liveable address;

    public Car() {
    }

    public Car(String name, Liveable address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Liveable getAddress() {
        return address;
    }

    public void setAddress(Liveable address) {
        this.address = address;
    }

    @Override
    public void running() {
        System.out.println("car is running");
    }

    public void init() {
        System.out.println("car use init()");
    }
}