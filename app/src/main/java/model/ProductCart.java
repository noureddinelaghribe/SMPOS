package model;

public class ProductCart {

    int id;
    String idp;
    int price;
    int qty;

    public ProductCart(int id,String idp, int price, int qty) {
        this.id = id;
        this.idp = idp;
        this.price = price;
        this.qty = qty;
    }

    public ProductCart() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdp() {
        return idp;
    }

    public void setIdp(String idp) {
        this.idp = idp;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
