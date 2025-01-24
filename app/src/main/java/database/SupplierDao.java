package database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Utles.Utel;
import model.Customer;
import model.Product;
import model.Supplier;

@Dao
public interface SupplierDao {

    @Insert
    Long insertSupplier(Supplier supplier);

    @Query("SELECT * FROM "+ Utel.TABLE_SUPPLIER+" WHERE isDeleted = 0")
    LiveData<List<Supplier>> getAllSuppliers();


    @Query("SELECT * FROM "+ Utel.TABLE_SUPPLIER+" WHERE id=:id")
    Supplier getSupplierById(int id);

    @Update
    void updateSupplier(Supplier supplier);

    @Query("UPDATE "+Utel.TABLE_SUPPLIER+" SET credit = :credit WHERE id = :userId")
    void updateCreditSupplier(int userId, double credit);

    @Query("SELECT * FROM " + Utel.TABLE_SUPPLIER + " WHERE phoneNumber = :text OR name LIKE '%' || :text || '%' AND isDeleted = 0")
    LiveData<List<Supplier>> searchSupplier(String text);

    @Query("DELETE FROM "+Utel.TABLE_SUPPLIER+" WHERE id=:id")
    void deleteSupplierById(int id);

    @Query("SELECT COUNT(*) FROM "+Utel.TABLE_SUPPLIER + " WHERE isDeleted = 0 AND dateInsert <= :timeEnd AND dateInsert >= :timeStare")
    LiveData<Integer> getCountSuppliers(long timeStare,long timeEnd);







}
