package model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

import Utles.Utel;
import database.Converters;
@Entity(tableName = Utel.TABLE_TRANSACTION)
@TypeConverters(Converters.class)
public class Transactions {

    @PrimaryKey(autoGenerate = true)
    int id;
    private List<String> quantity;
    int custmerId;
    int supplierId;
    int productId;
    int sellingId;
    int buyingId;
    //int quantity;
    long timestamp;
    String type;

    public Transactions() {}

    public Transactions(int id, int custmerId, int supplierId, int productId, int sellingId, int buyingId, long timestamp, String type) {
        this.id = id;
        this.custmerId = custmerId;
        this.supplierId = supplierId;
        this.productId = productId;
        this.sellingId = sellingId;
        this.buyingId = buyingId;
        this.timestamp = timestamp;
        this.type = type;
    }


    public Transactions(int custmerId, int supplierId, int productId, int buyingId, int sellingId, List<String> quantity, String type, long timestamp) {
        this.custmerId = custmerId;
        this.supplierId = supplierId;
        this.productId = productId;
        this.buyingId = buyingId;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.type = type;
        this.sellingId = sellingId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustmerId() {
        return custmerId;
    }

    public void setCustmerId(int custmerId) {
        this.custmerId = custmerId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSellingId() {
        return sellingId;
    }

    public void setSellingId(int sellingId) {
        this.sellingId = sellingId;
    }

    public int getBuyingId() {
        return buyingId;
    }

    public List<String> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<String> quantity) {
        this.quantity = quantity;
    }

    public void setBuyingId(int buyingId) {
        this.buyingId = buyingId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
