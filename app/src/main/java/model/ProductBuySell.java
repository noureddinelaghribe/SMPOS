package model;

import androidx.room.PrimaryKey;

public class ProductBuySell {
    //@PrimaryKey(autoGenerate = true)
    int id;
    String idp;
    int quantity;

    public ProductBuySell() {}

    public ProductBuySell(int id, String idp, int quantity) {
        this.id = id;
        this.idp = idp;
        this.quantity = quantity;
    }

    public ProductBuySell(String idp, int quantity) {
        this.idp = idp;
        this.quantity = quantity;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

