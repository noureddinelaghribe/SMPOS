package database;



import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import model.Buy;
import model.Customer;
import model.Product;
import model.Sell;
import model.Supplier;
import model.Transactions;

public class Reposetry {

    private SupplierDao supplierDao;
    private CustomerDao customerDao;
    private ProductDao productDao;
    private SellDao sellDao;
    private BuyDao buyDao;
    private TransactionsDao transactionsDao;
    private final DatabaseBackupHelper backupHelper;
    private final DatabaseRestoreHelper restore;


    public Reposetry(Application application) {
        SMDatabase smDatabase = SMDatabase.getDatabase(application);
        supplierDao = smDatabase.supplierDoa();
        customerDao = smDatabase.customerDao();
        productDao = smDatabase.productDao();
        sellDao = smDatabase.sellDao();
        buyDao = smDatabase.buyDao();
        transactionsDao = smDatabase.transactionsDao();
        backupHelper = new DatabaseBackupHelper(application);
        restore = new DatabaseRestoreHelper(application);
    }

    public DatabaseBackupHelper.BackupResult backupDatabase() {
        return backupHelper.backupDatabase();
    }


    // Restore operation
    public void restoreDatabase(RestoreCallback callback) {
        SMDatabase.executorService.execute(() -> {
            boolean success = restore.restoreDatabase();
            // Call callback on main thread
            callback.onRestoreComplete(success);
        });
    }

    // Callback interface
    public interface RestoreCallback {
        void onRestoreComplete(boolean success);
    }


    public LiveData<List<Supplier>> getAllSuppliers(){
        return supplierDao.getAllSuppliers();
    }

    public LiveData<Long> insertSupplier(Supplier supplier){

        MutableLiveData<Long> insertedIdLiveData = new MutableLiveData<>();

        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = supplierDao.insertSupplier(supplier);
                insertedIdLiveData.postValue(id);
            }
        });
        return insertedIdLiveData;
    }

    public void updateSupplier(Supplier supplier){
        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                supplierDao.updateSupplier(supplier);
            }
        });
    }

    public void deleteSupplierById(int id){
        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                supplierDao.deleteSupplierById(id);
            }
        });
    }

    public LiveData<List<Supplier>> searchSupplier(String text){
        return supplierDao.searchSupplier(text);
    }

    public LiveData<Integer> getCountSuppliers(long timeStare, long timeEnd) {
        return supplierDao.getCountSuppliers(timeStare, timeEnd);
    }

    public Supplier getSupplierById(int id){
        return supplierDao.getSupplierById(id);
    }


    public void updateCreditSupplier(int userId, double credit){
        supplierDao.updateCreditSupplier( userId, credit);
    }








    public LiveData<List<Customer>> getAllCustomers(){
        return customerDao.getAllCustomers();
    }

    public LiveData<Long> insertCustomer(Customer customer){
        MutableLiveData<Long> insertedIdLiveData = new MutableLiveData<>();

        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = customerDao.insertCustomer(customer);
                insertedIdLiveData.postValue(id);
            }
        });
        return insertedIdLiveData;
    }

    public void updateCustomer(Customer customer){
        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                customerDao.updateCustomer(customer);
            }
        });
    }

    public void deleteCustomerById(int id){
        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                customerDao.deleteCustomerById(id);
            }
        });
    }

    public LiveData<List<Customer>> searchCustomer(String text){
        return customerDao.searchCustomer(text);
    }

    public LiveData<Integer> getCountCustomers(long timeStare,long timeEnd){
        return customerDao.getCountCustomers( timeStare, timeEnd);
    }

    public Customer getCustomerById(int id){
        return customerDao.getCustomerById(id);
    }

    public void updateDebetCustomer(int userId, double debet){
        customerDao.updateDebet( userId, debet);
    }

    public void updateCreditCustomer(int userId, double credit){
        customerDao.updateDebet( userId, credit);
    }








    public LiveData<List<Product>> getAllProduct(){
        return productDao.getAllProducts();
    }

    public LiveData<Long> insertProduct(Product product){
        MutableLiveData<Long> insertedIdLiveData = new MutableLiveData<>();
        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = productDao.insertProduct(product);
                insertedIdLiveData.postValue(id);
            }
        });
        return insertedIdLiveData;
    }

    public void updateProduct(Product product){
        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                productDao.updateProduct(product);
            }
        });
    }

    public void updateQuantity(int id,int quantity){
        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                productDao.updateQuantity(id,quantity);
            }
        });
    }

    public LiveData<List<Product>> searchProduct(String text){
        return productDao.searchProduct(text);
    }

    public void deleteProductById(int id){
        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                productDao.deleteProductById(id);
            }
        });
    }

    public Product getProductByIdp(String idp){
        return productDao.getProductByIdp(idp);
    }

    public Product getProductById(int id){
        return productDao.getProductById(id);
    }


    public LiveData<Integer> getCountProducts(long timeStare,long timeEnd){
        return productDao.getCountProducts( timeStare, timeEnd);
    }

    public LiveData<Integer> getCountProductsStockexpiryDate(long timeStare,long timeEnd){
        return productDao.getCountProductsStockexpiryDate( timeStare, timeEnd);
    }

    public LiveData<List<Product>> getProductsOutStock(){
        return productDao.getProductsOutStock();
    }

    public LiveData<List<Product>> getProductsExpiryDate(long timeStare,long timeEnd){
        return productDao.getProductsExpiryDate( timeStare, timeEnd);
    }





    public LiveData<List<Sell>> getAllSelles(){
        return sellDao.getAllSelles();
    }

    public LiveData<Long> insertSelling(Sell sell){
        MutableLiveData<Long> insertedIdLiveData = new MutableLiveData<>();

        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = sellDao.insertSelling(sell);
                insertedIdLiveData.postValue(id);
            }
        });
        return insertedIdLiveData;
    }

    public LiveData<Integer> getCountSelles(long timeStare,long timeEnd){
        return sellDao.getCountSelles( timeStare, timeEnd);
    }

    public LiveData<List<Sell>> getCountSellesCash(long timeStare,long timeEnd){
        return sellDao.getCountSellesCash( timeStare, timeEnd);
    }

    public LiveData<List<Sell>> getCountSellesDebts(long timeStare,long timeEnd){
        return sellDao.getCountSellesDebt( timeStare, timeEnd);
    }

    public Sell getSelleById(int id){
        return sellDao.getSelleById(id);
    }






    public LiveData<List<Buy>> getAllBuyers(){
        return buyDao.getAllBayers();
    }

    public LiveData<Long> insertBuying(Buy buy){
        MutableLiveData<Long> insertedIdLiveData = new MutableLiveData<>();

        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                long id = buyDao.insertBuying(buy);
                insertedIdLiveData.postValue(id);
            }
        });
        return insertedIdLiveData;
    }


    public LiveData<List<Buy>> getCountBuyersCash(long timeStare,long timeEnd){
        return buyDao.getCountBuyersCash( timeStare, timeEnd);
    }

    public LiveData<List<Buy>> getCountBuyersDebts(long timeStare,long timeEnd){
        return buyDao.getCountBuyersDebts( timeStare, timeEnd);
    }

    public Buy getBayeingById(int id){
        return buyDao.getBayeingById(id);
    }

    public LiveData<Integer> getCountBayersByTime(long timeStare,long timeEnd){
        return buyDao.getCountBayersByTime( timeStare, timeEnd);
    }





    public void insertTransaction(Transactions transactions){
        SMDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                transactionsDao.insertTransaction(transactions);
            }
        });
    }

    public int getCountTransactions(){
        return transactionsDao.getCountTransactions();
    }

    public LiveData<List<Transactions>> timeTransactions(long timeStare,long timeEnd){
        return transactionsDao.timeTransactions( timeStare, timeEnd);
    }

    public LiveData<List<Transactions>> getAllTransactions(){
        return transactionsDao.getAllTransactions();
    }


}
