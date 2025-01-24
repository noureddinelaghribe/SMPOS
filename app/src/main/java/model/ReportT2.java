package model;

import java.util.List;

public class ReportT2 {

    int id;
    private List<String> quantity;
    String type;
    String name;
    String namesProducts;
    Sell sell; // this to get *totalAmount *discountAmount *paymentAmount *restAmount *typePayment *note
    Buy buy; // this to get *totalAmount *discountAmount *paymentAmount *restAmount *typePayment *note
    String date;

    public ReportT2() {}

    public ReportT2(int id,String type, String name, String namesProducts, Sell sell, String date, List<String> quantity) {
        this.type = type;
        this.name = name;
        this.namesProducts = namesProducts;
        this.sell = sell;
        this.date = date;
        this.quantity = quantity;
        this.id = id;
    }

    public ReportT2(int id,String type, String name, String namesProducts, Buy buy, String date, List<String> quantity) {
        this.type = type;
        this.name = name;
        this.namesProducts = namesProducts;
        this.buy = buy;
        this.date = date;
        this.quantity = quantity;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<String> quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamesProducts() {
        return namesProducts;
    }

    public void setNamesProducts(String namesProducts) {
        this.namesProducts = namesProducts;
    }

    public Sell getSell() {
        return sell;
    }

    public void setSell(Sell sell) {
        this.sell = sell;
    }

    public Buy getBuy() {
        return buy;
    }

    public void setBuy(Buy buy) {
        this.buy = buy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
