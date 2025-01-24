package com.noureddine.stockmanagment;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import database.DatabaseBackupHelper;
import database.Reposetry;
import model.Buy;
import model.Customer;
import model.Product;
import model.Sell;
import model.Supplier;
import model.Transactions;

public class SMViewModel extends AndroidViewModel {
    private Reposetry reposetry;

    public SMViewModel(@NonNull Application application) {
        super(application);
        reposetry = new Reposetry(application);
    }

    public DatabaseBackupHelper.BackupResult backupDatabase() {
        return reposetry.backupDatabase();
    }

    public void restoreDatabase(Reposetry.RestoreCallback callback) {
        reposetry.restoreDatabase(callback);
    }

    public LiveData<Long> insertSupplier(Supplier supplier){
        return reposetry.insertSupplier(supplier);
    }

    public LiveData<List<Supplier>> getAllSuppliers(){
        return reposetry.getAllSuppliers();
    }

    public void updateSupplier(Supplier supplier){
        reposetry.updateSupplier(supplier);
    }

    public void deleteSupplierById(int id){
        reposetry.deleteSupplierById(id);
    }

    public LiveData<List<Supplier>> searchSupplier(String text){
        return reposetry.searchSupplier(text);
    }

    public LiveData<Integer> getCountSuppliers(long timeStare,long timeEnd){
        return reposetry.getCountSuppliers( timeStare, timeEnd);
    }

    public Supplier getSupplierById(int id){
        return reposetry.getSupplierById(id);
    }


    public void updateCreditSupplier(int userId, double credit){
        reposetry.updateCreditSupplier( userId, credit);
    }



    public LiveData<Long> insertCustomer(Customer customer){
        return reposetry.insertCustomer(customer);
    }

    public LiveData<List<Customer>> getAllCustomers(){
        return reposetry.getAllCustomers();
    }

    public void updateCustomer(Customer customer){
        reposetry.updateCustomer(customer);
    }

    public void deleteCustomerById(int id){
        reposetry.deleteCustomerById(id);
    }


    public LiveData<List<Customer>> searchCustomer(String text){
        return reposetry.searchCustomer(text);
    }

    public LiveData<Integer> getCountCustomers(long timeStare,long timeEnd){
        return reposetry.getCountCustomers( timeStare, timeEnd);
    }

    public Customer getCustomerById(int id){
        return reposetry.getCustomerById(id);
    }

    public void updateDebetCustomer(int userId, double debet){
        reposetry.updateDebetCustomer( userId, debet);
    }

    public void updateCreditCustomer(int userId, double credit){
        reposetry.updateCreditCustomer( userId, credit);
    }






    public LiveData<Long> insertProduct(Product product){
        return reposetry.insertProduct(product);
    }

    public void updateProduct(Product product){
        reposetry.updateProduct(product);
    }

    public void updateQuantity(int id,int quantity){
        reposetry.updateQuantity(id,quantity);
    }

    public LiveData<List<Product>> getAllProduct(){
        return reposetry.getAllProduct();
    }

    public LiveData<List<Product>> searchProduct(String text){
        return reposetry.searchProduct(text);
    }

    public void deleteProductById(int id){
        reposetry.deleteProductById(id);}

    public Product getProductByIdp(String idp){
        Log.d("onActivityResult", "getProductByIdp: product "+reposetry.getProductByIdp(idp)+" id "+idp);
        return reposetry.getProductByIdp(idp);
    }


    public Product getProductById(int id){
        return reposetry.getProductById(id);
    }


    public LiveData<Integer> getCountProducts(long timeStare,long timeEnd){
        return reposetry.getCountProducts( timeStare, timeEnd);
    }

    public LiveData<Integer> getCountProductsStockexpiryDate(long timeStare,long timeEnd){
        return reposetry.getCountProductsStockexpiryDate( timeStare, timeEnd);
    }

    public LiveData<List<Product>> getProductsExpiryDate(long timeStare,long timeEnd){
        return reposetry.getProductsExpiryDate( timeStare, timeEnd);
    }

    public LiveData<List<Product>> getProductsOutStock(){
        return reposetry.getProductsOutStock();
    }






    public LiveData<Long> insertSelling(Sell sell){
        return reposetry.insertSelling(sell);
    }

    public LiveData<List<Sell>> getAllSelles(){
        return reposetry.getAllSelles();
    }

    public LiveData<Integer> getCountSelles(long timeStare,long timeEnd){
        return reposetry.getCountSelles( timeStare, timeEnd);
    }

    public LiveData<List<Sell>> getCountSellesCash(long timeStare,long timeEnd){
        return reposetry.getCountSellesCash( timeStare, timeEnd);
    }

    public LiveData<List<Sell>> getCountSellesDebts(long timeStare,long timeEnd){
        return reposetry.getCountSellesDebts( timeStare, timeEnd);
    }

    public Sell getSelleById(int id){
        return reposetry.getSelleById(id);
    }





    public LiveData<Long> insertBuying(Buy buy){
        return reposetry.insertBuying(buy);
    }

    public LiveData<List<Buy>> getAllBuyers(){
        return reposetry.getAllBuyers();
    }

//    public int getCountBuyers(){
//        return reposetry.getCountBuyers();
//    }

    public LiveData<List<Buy>> getCountBuyersCash(long timeStare,long timeEnd){
        return reposetry.getCountBuyersCash( timeStare, timeEnd);
    }

    public LiveData<List<Buy>> getCountBuyersDebts(long timeStare,long timeEnd){
        return reposetry.getCountBuyersDebts( timeStare, timeEnd);
    }

    public Buy getBayeingById(int id){
        return reposetry.getBayeingById(id);
    }

    public LiveData<Integer> getCountBayersByTime(long timeStare,long timeEnd){
        return reposetry.getCountBayersByTime( timeStare, timeEnd);
    }






    public void inserttransaction(Transactions transactions){
        reposetry.insertTransaction(transactions);
    }

    public int getCounttransactions(){
        return reposetry.getCountTransactions();
    }

    public LiveData<List<Transactions>> timeTransactions(long timeStare,long timeEnd){
        return reposetry.timeTransactions(timeStare,timeEnd);
    }

    public LiveData<List<Transactions>> getAllTransactions(){
        return reposetry.getAllTransactions();
    }


}
