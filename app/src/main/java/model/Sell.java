package model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

import Utles.Utel;
import database.Converters;

@Entity(tableName = Utel.TABLE_SELL)
@TypeConverters(Converters.class)
public class Sell {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private List<String> idsProducts;
    private int customerID;
    private int totalAmount;
    private int discountAmount;
    private int paymentAmount;
    private int restAmount;
    private String typePayment;
    private String note;
    long dateInsert;



    public Sell() {}

    public Sell(int id, List<String> idsProducts, int customerID, int totalAmount,int discountAmount, int paymentAmount,int restAmount, String typePayment, String note,long dateInsert) {
        this.id = id;
        this.idsProducts = idsProducts;
        this.customerID = customerID;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.paymentAmount = paymentAmount;
        this.restAmount = restAmount;
        this.typePayment = typePayment;
        this.note = note;
        this.dateInsert = dateInsert;
    }

    public Sell(List<String> idsProducts, int customerID, int totalAmount,int discountAmount, int paymentAmount,int restAmount, String typePayment, String note,long dateInsert) {
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.paymentAmount = paymentAmount;
        this.restAmount = restAmount;
        this.typePayment = typePayment;
        this.note = note;
        this.customerID = customerID;
        this.idsProducts = idsProducts;
        this.dateInsert = dateInsert;
    }

    public Sell(int totalAmount, int discountAmount, int paymentAmount, int restAmount, String typePayment, String note) {
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.paymentAmount = paymentAmount;
        this.restAmount = restAmount;
        this.typePayment = typePayment;
        this.note = note;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
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

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
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

    public void setcustomerID(int customerID) {
        this.customerID = customerID;
    }

    public long getDateInsert() {
        return dateInsert;
    }

    public void setDateInsert(long dateInsert) {
        this.dateInsert = dateInsert;
    }

}
