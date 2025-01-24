package database;

import android.app.Person;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Utles.Utel;
import model.Product;


@Dao
public interface ProductDao {

    @Insert
    Long insertProduct(Product product);

    @Query("SELECT * FROM "+ Utel.TABLE_PRODUCT +" WHERE isDeleted = 0")
    LiveData<List<Product>> getAllProducts();

    @Update
    void updateProduct(Product product);

    @Query("UPDATE Product SET quantity = :quantity WHERE id = :id")
    void updateQuantity(int id, int quantity);

    //    @Query("SELECT * FROM " + Utel.TABLE_PRODUCT + " WHERE idp LIKE '%' || :text || '%' OR name LIKE '%' || :text || '%'")
    @Query("SELECT * FROM " + Utel.TABLE_PRODUCT + " WHERE idp = :text OR name LIKE '%' || :text || '%' AND isDeleted = 0")
    LiveData<List<Product>> searchProduct(String text);

    @Query("DELETE FROM "+Utel.TABLE_PRODUCT+" WHERE id=:id")
    void deleteProductById(int id);

    @Query("SELECT * FROM " + Utel.TABLE_PRODUCT + " WHERE idp = :idp AND isDeleted = 0")
    Product getProductByIdp(String idp);

    @Query("SELECT * FROM " + Utel.TABLE_PRODUCT + " WHERE id = :id")
    Product getProductById(int id);

    @Query("SELECT COUNT(*) FROM "+Utel.TABLE_PRODUCT+ " WHERE isDeleted = 0 AND dateInsert <= :timeEnd AND dateInsert >= :timeStare")
    LiveData<Integer> getCountProducts(long timeStare,long timeEnd);

    //@Query("SELECT COUNT(*) FROM "+Utel.TABLE_PRODUCT+ " WHERE isDeleted = 0 AND expiryDate <= :timeEnd AND expiryDate >= :timeStare OR quantity <= 'limit'")
    @Query("SELECT COUNT(*) FROM " + Utel.TABLE_PRODUCT + " WHERE isDeleted = 0 AND (expiryDate <= :timeEnd AND expiryDate >= :timeStare OR quantity <= `limit`) AND quantity > 0")
    LiveData<Integer> getCountProductsStockexpiryDate(long timeStare,long timeEnd);

    @Query("SELECT * FROM " + Utel.TABLE_PRODUCT + " WHERE isDeleted = 0 AND quantity <= `limit` AND quantity > 0")
    LiveData<List<Product>> getProductsOutStock();

    @Query("SELECT * FROM " + Utel.TABLE_PRODUCT + " WHERE isDeleted = 0 AND expiryDate <= :timeEnd AND expiryDate >= :timeStare")
    LiveData<List<Product>> getProductsExpiryDate(long timeStare,long timeEnd);

}
