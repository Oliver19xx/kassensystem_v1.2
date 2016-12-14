package ninja.oliverwerner.kassensystem_v12;

import java.io.Serializable;

/**
 * Created by Marcel on 11.12.2016.
 */

public class Product {
    private String name = "";
    private int number = 0;
    private double price = 0;

    public Product(String name, double price, int number){
        this.setName(name);
        this.setNumber(number);
        this.setPrice(price);
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}