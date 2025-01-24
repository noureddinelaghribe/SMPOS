package database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import Utles.Utel;
import model.Transactions;

@Dao
public interface TransactionsDao {

    @Insert
    void insertTransaction(Transactions transactions);

    @Query("SELECT * FROM "+ Utel.TABLE_TRANSACTION)
    LiveData<List<Transactions>> getAllTransactions();

    @Query("SELECT COUNT(*) FROM "+Utel.TABLE_TRANSACTION)
    int getCountTransactions();

    @Query("SELECT * FROM " + Utel.TABLE_TRANSACTION + " WHERE timestamp <= :timeEnd AND timestamp >= :timeStare")
    LiveData<List<Transactions>> timeTransactions(long timeStare,long timeEnd);


}
