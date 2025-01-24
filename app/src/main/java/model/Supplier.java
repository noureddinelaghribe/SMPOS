package model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import Utles.Utel;

@Entity(tableName = Utel.TABLE_SUPPLIER)
public class Supplier {

    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String Address;
    String phoneNumber;
    String Note;
    long dateInsert;
    private boolean isDeleted;
    int credit;

    public Supplier() {}

    public Supplier(int id, String name, String address, String phoneNumber, String note,long dateInsert,int credit) {
        this.id = id;
        this.name = name;
        this.Address = address;
        this.phoneNumber = phoneNumber;
        this.Note = note;
        this.dateInsert = dateInsert;
        this.credit = credit;
    }

    public Supplier(String name, String address, String phoneNumber, String note,long dateInsert,boolean isDeleted ,int credit) {
        this.name = name;
        this.Address = address;
        this.phoneNumber = phoneNumber;
        this.Note = note;
        this.dateInsert = dateInsert;
        this.isDeleted = isDeleted;
        this.credit = credit;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
