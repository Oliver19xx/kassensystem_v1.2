package ninja.oliverwerner.kassensystem_v12;

import java.io.Serializable;

/**
 * Created by Marcel on 11.12.2016.
 */

public class Product {
    private int productID = 0;
    private String name = "";
    private int number = 0;
    private double price = 0;
    private int orderId = 0;
    private int productGroup = 0;

    public Product(String name, double price, int p_id, int productGroup){
        this.setProductID(-1);
        this.setName(name);
        this.setNumber(p_id);
        this.setPrice(price);
        this.setProductGroup(productGroup);
        this.setOrderId(-1);
    }
    public Product(int productID,String name, double price, int p_id, int orderId){
        this.setProductID(productID);
        this.setName(name);
        this.setNumber(p_id);
        this.setPrice(price);
        this.setOrderId(orderId);
        this.setProductGroup(-1);
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

    public int getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(int productGroup) {
        this.productGroup = productGroup;
    }

    public void  setProductID(int productID){ this.productID = productID;}

    public int getProductID(){ return productID; }

    public int getOrderId(){return orderId;}

    public void setOrderId(int orderId){ this.orderId = orderId;}
}