package database;

import android.app.Person;

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
public interface CustomerDao {

    @Insert
    Long insertCustomer(Customer customer);

    @Query("SELECT * FROM "+ Utel.TABLE_CUSTOMER+" WHERE isDeleted = 0")
    LiveData<List<Customer>> getAllCustomers();

    @Query("SELECT * FROM "+ Utel.TABLE_CUSTOMER+" WHERE id=:id")
    Customer getCustomerById(int id);

    @Update
    void updateCustomer(Customer customer);

    @Query("UPDATE "+Utel.TABLE_CUSTOMER+" SET debet = :debet WHERE id = :userId")
    void updateDebet(int userId, double debet);

    @Query("SELECT * FROM " + Utel.TABLE_CUSTOMER + " WHERE phoneNumber = :text OR name LIKE '%' || :text || '%' AND isDeleted = 0")
    LiveData<List<Customer>> searchCustomer(String text);

    @Query("DELETE FROM "+Utel.TABLE_CUSTOMER+" WHERE id=:id")
    void deleteCustomerById(int id);

    @Query("SELECT COUNT(*) FROM "+Utel.TABLE_CUSTOMER+ " WHERE isDeleted = 0 AND dateInsert <= :timeEnd AND dateInsert >= :timeStare")
    LiveData<Integer> getCountCustomers(long timeStare,long timeEnd);

}
