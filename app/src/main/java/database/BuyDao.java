package database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import Utles.Utel;
import model.Buy;

@Dao
public interface BuyDao {

    @Insert
    Long insertBuying(Buy buy);

    @Query("SELECT * FROM "+ Utel.TABLE_BUY)
    LiveData<List<Buy>> getAllBayers();

    @Query("SELECT * FROM "+ Utel.TABLE_BUY +" WHERE id=:id")
    Buy getBayeingById(int id);


    @Query("SELECT * FROM "+Utel.TABLE_BUY+ " WHERE typePayment = 'cash' AND dateInsert <= :timeEnd AND dateInsert >= :timeStare")
    LiveData<List<Buy>> getCountBuyersCash(long timeStare,long timeEnd);

    @Query("SELECT * FROM "+Utel.TABLE_BUY+ " WHERE typePayment = 'debt' AND dateInsert <= :timeEnd AND dateInsert >= :timeStare")
    LiveData<List<Buy>> getCountBuyersDebts(long timeStare,long timeEnd);

    @Query("SELECT COUNT(*) FROM "+ Utel.TABLE_BUY + " WHERE dateInsert <= :timeEnd AND dateInsert >= :timeStare")
    LiveData<Integer> getCountBayersByTime(long timeStare,long timeEnd);

}
