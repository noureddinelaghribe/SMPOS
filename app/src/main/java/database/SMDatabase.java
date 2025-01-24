package database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Utles.Utel;
import model.Buy;
import model.Customer;
import model.Product;
import model.Sell;
import model.Supplier;
import model.Transactions;


@androidx.room.Database(entities = {Buy.class,Sell.class, Product.class, Customer.class, Supplier.class, Transactions.class},
        version = Utel.DATABASE_VERTION,exportSchema = false)
public abstract class SMDatabase extends RoomDatabase {

    public abstract SupplierDao supplierDoa();
    public abstract CustomerDao customerDao();
    public abstract ProductDao productDao();
    public abstract SellDao sellDao();
    public abstract BuyDao buyDao();
    public abstract TransactionsDao transactionsDao();

    private static volatile SMDatabase INSTANCE ;
    private static final int NUMBER_OF_THREDS = 4;
    static ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREDS);

    public static SMDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SMDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SMDatabase.class, Utel.DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }


}
