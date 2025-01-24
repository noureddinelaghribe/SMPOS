package model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import Utles.Utel;

@Entity(tableName = Utel.TABLE_PRODUCT)
public class Product {

    @PrimaryKey(autoGenerate = true)
    int id;
    String idp; //conver to string becouse remove 0 after number
    String name;
    String info;
    int priceBuy;
    int priceSell;
    int quantity;
    int limit;
    long expiryDate;
    String imagePath;
    long dateInsert;
    private boolean isDeleted;


    public Product() {}

    public Product(int id, String idp, String name, String info, int priceBuy,int priceSell, int quantity, int limit, long expiryDate, String imagePath,long dateInsert) {
        this.id = id;
        this.idp = idp;
        this.name = name;
        this.info = info;
        this.priceBuy = priceBuy;
        this.priceSell = priceSell;
        this.quantity = quantity;
        this.limit = limit;
        this.expiryDate = expiryDate;
        this.imagePath = imagePath;
        this.dateInsert = dateInsert;
    }

    public Product(String info, int quantity, int limit, long expiryDate, String imagePath, int priceBuy, int priceSell, String idp, String name,long dateInsert,boolean isDeleted) {
        this.info = info;
        this.quantity = quantity;
        this.limit = limit;
        this.expiryDate = expiryDate;
        this.imagePath = imagePath;
        this.priceSell = priceSell;
        this.priceBuy = priceBuy;
        this.idp = idp;
        this.name = name;
        this.dateInsert = dateInsert;
        this.isDeleted = isDeleted;
    }

    public long getDateInsert() {
        return dateInsert;
    }

    public void setDateInsert(long dateInsert) {
        this.dateInsert = dateInsert;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getPriceBuy() {
        return priceBuy;
    }

    public void setPriceBuy(int priceBuy) {
        this.priceBuy = priceBuy;
    }

    public int getPriceSell() {
        return this.priceSell;
    }

    public void setPriceSell(int PriceSell) {
        this.priceSell = PriceSell;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
