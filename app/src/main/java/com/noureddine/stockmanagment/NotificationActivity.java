package com.noureddine.stockmanagment;

import static com.noureddine.stockmanagment.Opiration.longToDate;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Controlar.AdapterNotification;
import model.Product;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterNotification adapter;
    List<String> listOutStock = new ArrayList<>();
    List<String> listexpiryDate = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);

        adapter = new AdapterNotification();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SMViewModel smViewModel = new ViewModelProvider(this).get(SMViewModel.class);

        long starDate = System.currentTimeMillis()-86400000;
        long sevenDays = System.currentTimeMillis()+86400000*7;

        listOutStock.clear();
        listexpiryDate.clear();
        adapter.clearNotification();

        smViewModel.getProductsExpiryDate(starDate,sevenDays).observe(NotificationActivity.this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> productList) {
                for (Product p : productList){
                    listexpiryDate.add("المنتج : "+p.getName()+" على وشك انتهاء صلاحيته : "+longToDate(p.getExpiryDate()));
                }
                adapter.updateNotification(listexpiryDate);
                recyclerView.setAdapter(adapter);
            }
        });

        smViewModel.getProductsOutStock().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> productList) {
                for (Product p : productList){
                    listOutStock.add("المنتج : "+p.getName()+" على وشك النفاد باقي : "+p.getQuantity());
                }
                adapter.updateNotification(listOutStock);
                recyclerView.setAdapter(adapter);
            }
        });





    }
}