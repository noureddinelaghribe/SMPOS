package model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

import Utles.Utel;
import database.Converters;

@Entity(tableName = Utel.TABLE_BUY)
@TypeConverters(Converters.class)
public class Buy {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private List<String> idsProducts;
    private int supplierID;
    private int discountAmount;
    private int totalAmount;
    private int paymentAmount;
    private int restAmount;
    private String typePayment;
    private String note;
    long dateInsert;


    public Buy() {}

    public Buy(int id, List<String> idsProducts, int supplierID, int totalAmount, int discountAmount, int paymentAmount,int restAmount, String typePayment, String note,long dateInsert) {
        this.id = id;
        this.idsProducts = idsProducts;
        this.supplierID = supplierID;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.paymentAmount = paymentAmount;
        this.restAmount = restAmount;
        this.typePayment = typePayment;
        this.note = note;
        this.dateInsert = dateInsert;
    }

    public Buy(List<String> idsProducts, int supplierID, int totalAmount, int discountAmount, int paymentAmount,int restAmount, String typePayment, String note,long dateInsert) {
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.paymentAmount = paymentAmount;
        this.restAmount = restAmount;
        this.typePayment = typePayment;
        this.note = note;
        this.supplierID = supplierID;
        this.idsProducts = idsProducts;
        this.dateInsert = dateInsert;
    }

    public Buy(int totalAmount, int discountAmount, int paymentAmount, int restAmount, String typePayment, String note) {
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.paymentAmount = paymentAmount;
        this.restAmount = restAmount;
        this.typePayment = typePayment;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String>  getIdsProducts() {
        return idsProducts;
    }

    public void setIdsProducts(List<String> idsProducts) {
        this.idsProducts = idsProducts;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(String typePayment) {
        this.typePayment = typePayment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getpaymentAmount() {
        return paymentAmount;
    }

    public void setpaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public int getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(int restAmount) {
        this.restAmount = restAmount;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setsupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public long getDateInsert() {
        return dateInsert;
    }

    public void setDateInsert(long dateInsert) {
        this.dateInsert = dateInsert;
    }
}
