package database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Utles.Utel;
import model.Buy;
import model.Sell;

@Dao
public interface SellDao {

    @Insert
    Long insertSelling(Sell sell);

    @Query("SELECT * FROM "+ Utel.TABLE_SELL)
    LiveData<List<Sell>> getAllSelles();

    @Query("SELECT * FROM "+ Utel.TABLE_SELL+" WHERE id=:id")
    Sell getSelleById(int id);

    @Query("SELECT COUNT(*) FROM "+Utel.TABLE_SELL + " WHERE dateInsert <= :timeEnd AND dateInsert >= :timeStare")
    LiveData<Integer> getCountSelles(long timeStare,long timeEnd);

    @Query("SELECT * FROM "+Utel.TABLE_SELL+ " WHERE typePayment = 'cash' AND dateInsert <= :timeEnd AND dateInsert >= :timeStare")
    LiveData<List<Sell>> getCountSellesCash(long timeStare,long timeEnd);

    @Query("SELECT * FROM "+Utel.TABLE_SELL+ " WHERE typePayment = 'debt' AND dateInsert <= :timeEnd AND dateInsert >= :timeStare")
    LiveData<List<Sell>> getCountSellesDebt(long timeStare,long timeEnd);

}
