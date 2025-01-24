package com.noureddine.stockmanagment;

import static com.noureddine.stockmanagment.Opiration.longToDate;
import static com.noureddine.stockmanagment.Opiration.longToDateTime;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;


import java.util.ArrayList;
import java.util.List;

import Controlar.AdapterReport;
import model.Buy;
import model.ReportT1;
import model.ReportT2;
import model.Sell;
import model.Transactions;

public class ReportsActivity extends AppCompatActivity {

    TextView textViewTotalItems,textViewTotalCustomers,textViewTotalSuppliers,
            textViewTotalDebtsCustomers,textViewTotalCheckouts,
            textViewTotalCheckins,textViewTotalDebtsSuppliers,
    button_from,button_to,textViewMoreLess;

    Button button;

    RecyclerView recyclerView;
    AdapterReport adapterReport;

    List<Object> reportList = new ArrayList<>();

    SMViewModel smViewModel;

    long timeStare = 0 , timeEnd = 0 ;

    LinearLayout more,linear_less,linear_more;

    boolean viewMore = false;

    //add liners layouts to show every one in recycler view

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewTotalItems = findViewById(R.id.textView_total_items);
        textViewTotalCustomers = findViewById(R.id.textView_total_customers);
        textViewTotalSuppliers = findViewById(R.id.textView_total_suppliers);
        textViewTotalDebtsCustomers = findViewById(R.id.textView_total_debts_customers);
        textViewTotalDebtsSuppliers = findViewById(R.id.textView_total_debts_suppliers);
        textViewTotalCheckouts = findViewById(R.id.textView_total_checkouts);
        textViewTotalCheckins = findViewById(R.id.textView_total_checkins);
        button_to = findViewById(R.id.button_to);
        button_from = findViewById(R.id.button_from);
        button = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recyclerView);
        more = findViewById(R.id.LinearLayout6);
        linear_more = findViewById(R.id.linear_more);
        linear_less = findViewById(R.id.linear_less);
        textViewMoreLess = findViewById(R.id.textView13);

        button_from.setText("--/--/----");
        button_to.setText("--/--/----");

        smViewModel = new ViewModelProvider(this).get(SMViewModel.class);

        adapterReport = new AdapterReport();
        recyclerView.setAdapter(adapterReport);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        linear_more.setVisibility(View.GONE);
        linear_less.setVisibility(View.VISIBLE);
        textViewMoreLess.setText("عرض المزيد ...");

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View v) {

                if ( timeEnd == 0 || timeStare == 0 ){
                    Toast.makeText(ReportsActivity.this, "الرجاء اختيار تاريخ الاستعلام", Toast.LENGTH_SHORT).show();
                }else {
                    loadReport();
                }
            }
        });

        button_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the MaterialDatePicker with customizations
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("تاريخ البدء :") // Custom title in Arabic
                        .setPositiveButtonText("اختيار")  // Custom positive button text
                        .setNegativeButtonText("إلغاء")  // Custom negative button text
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Default to today's date
                        .build();

                // Show the date picker
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    // Get the selected date and set it to the TextView
                    timeStare =datePicker.getSelection();
                    Log.d("datePicker", "System.currentTimeMillis() : "+ System.currentTimeMillis());
                    Log.d("datePicker", "timeStare : "+ timeStare);
                    button_from.setText(longToDate(timeStare));  // Display the selected date
                    //loadReport();
                });

                // Optional: Handle negative button click (e.g., dismiss dialog)
                datePicker.addOnNegativeButtonClickListener(dialog -> {
                    // Handle cancellation
                });

            }
        });

        button_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final long[] data = new long[1];

                // Create the MaterialDatePicker with customizations
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("تاريخ انتهاء :") // Custom title in Arabic
                        .setPositiveButtonText("اختيار")  // Custom positive button text
                        .setNegativeButtonText("إلغاء")  // Custom negative button text
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Default to today's date
                        .build();

                // Show the date picker
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    // Get the selected date and set it to the TextView
                    timeEnd =datePicker.getSelection();
                    //Log.d("TAG", "getExpiryDate : "+ data);
                    Log.d("datePicker", "timeEnd : "+ timeEnd);
                    button_to.setText(longToDate(timeEnd));  // Display the selected date
                    //loadReport();
                });

                // Optional: Handle negative button click (e.g., dismiss dialog)
                datePicker.addOnNegativeButtonClickListener(dialog -> {
                    // Handle cancellation
                });

            }
        });


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( timeStare == 0 || timeEnd == 0 ){
                    Toast.makeText(ReportsActivity.this, "اخر الوقت اولا", Toast.LENGTH_SHORT).show();
                }else {
                    if (!viewMore){
                        linear_less.setVisibility(View.VISIBLE);
                        linear_more.setVisibility(View.GONE);
                        textViewMoreLess.setText("عرض المزيد ...");
                        viewMore = true;
                    }else {
                        linear_less.setVisibility(View.GONE);
                        linear_more.setVisibility(View.VISIBLE);
                        textViewMoreLess.setText("عرض اقل ...");
                        viewMore = false;
                    }

                }

            }
        });








    }





    public void loadReport(){

        adapterReport.clearData();

        if (timeStare == timeEnd){
            timeEnd = timeStare + 86400000 - 1 ;
        }

        smViewModel.timeTransactions( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<List<Transactions>>() {
            @Override
            public void onChanged(List<Transactions> transactions) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (Transactions t : transactions){
                            switch (t.getType()){
                                case "insertProduct":

                                case "updateProduct":

                                case "deleteProduct":

                                    reportList.add(new ReportT1(t.getType(), smViewModel.getProductById(t.getProductId()).getName(), longToDateTime(t.getTimestamp())));

                                    break;

                                case "insertCustomer":

                                case "updateCustomer":

                                case "deleteCustomer":

                                    reportList.add(new ReportT1(t.getType(), smViewModel.getCustomerById(t.getCustmerId()).getName(), longToDateTime(t.getTimestamp())));

                                    break;

                                case "insertSupplier":

                                case "updateSupplier":

                                case "deleteSupplier":

                                    reportList.add(new ReportT1(t.getType(), smViewModel.getSupplierById(t.getSupplierId()).getName(), longToDateTime(t.getTimestamp())));

                                    break;

                                case "insertSelling":

                                    String namesProductsSelling = "";
                                    int i = 0;
                                    for (String item : smViewModel.getSelleById(t.getSellingId()).getIdsProducts() ){
                                        if (namesProductsSelling.isEmpty()){
                                            namesProductsSelling = smViewModel.getProductByIdp(item).getName()+"  :  "+t.getQuantity().get(i);
                                            i++;
                                        }else {
                                            namesProductsSelling = namesProductsSelling + "\n" + smViewModel.getProductByIdp(item).getName()+"  :  "+t.getQuantity().get(i);
                                            i++;
                                        }
                                    }
                                    //List<String> idsProducts, int customerID, double totalAmount,double discountAmount, double paymentAmount,double restAmount, String typePayment, String note
                                    Sell sell = new Sell(smViewModel.getSelleById(t.getSellingId()).getTotalAmount(),
                                            smViewModel.getSelleById(t.getSellingId()).getDiscountAmount(),
                                            smViewModel.getSelleById(t.getSellingId()).getpaymentAmount(),
                                            smViewModel.getSelleById(t.getSellingId()).getRestAmount(),
                                            smViewModel.getSelleById(t.getSellingId()).getTypePayment(),
                                            smViewModel.getSelleById(t.getSellingId()).getNote());

                                    //String type, String name, List<String> namesProducts, Sell sell, String date
                                    reportList.add(new ReportT2(t.getSellingId(),
                                            t.getType(),
                                            smViewModel.getCustomerById(smViewModel.getSelleById(t.getSellingId()).getCustomerID()).getName(),
                                            namesProductsSelling,
                                            sell,
                                            longToDateTime(t.getTimestamp()),
                                            t.getQuantity()
                                    ));

                                    break;

                                case "insertBuying":

                                    String namesProductsBuying = "";
                                    int n = 0;
                                    for (String item : smViewModel.getBayeingById(t.getBuyingId()).getIdsProducts() ){
                                        if (namesProductsBuying.isEmpty()){
                                            namesProductsBuying = smViewModel.getProductByIdp(item).getName()+"  :  "+t.getQuantity().get(n);
                                            n++;
                                        }else {
                                            namesProductsBuying = namesProductsBuying + "\n" + smViewModel.getProductByIdp(item).getName()+"  :  "+t.getQuantity().get(n);
                                            n++;
                                        }
                                    }
                                    //double discountAmount, double totalAmount, double paymentAmount, double restAmount, String typePayment, String note
                                    Buy buy =new Buy(
                                            smViewModel.getBayeingById(t.getBuyingId()).getTotalAmount(),
                                            smViewModel.getBayeingById(t.getBuyingId()).getDiscountAmount(),
                                            smViewModel.getBayeingById(t.getBuyingId()).getpaymentAmount(),
                                            smViewModel.getBayeingById(t.getBuyingId()).getRestAmount(),
                                            smViewModel.getBayeingById(t.getBuyingId()).getTypePayment(),
                                            smViewModel.getBayeingById(t.getBuyingId()).getNote()
                                    );
                                    //String type, String name, List<String> namesProducts, Buy buy, String date
                                    reportList.add(new ReportT2(t.getBuyingId(),
                                            t.getType(),
                                            smViewModel.getSupplierById(smViewModel.getBayeingById(t.getBuyingId()).getSupplierID()).getName(),
                                            namesProductsBuying,
                                            buy,
                                            longToDateTime(t.getTimestamp()),
                                            t.getQuantity()
                                    ));

                                    break;

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapterReport.addReport(reportList);


                                }
                            });

                        }

                    }
                }).start();
            }
        });


        smViewModel.getCountSuppliers( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewTotalSuppliers.setText(String.valueOf(integer));
            }
        });

        smViewModel.getCountCustomers( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewTotalCustomers.setText(String.valueOf(integer));
            }
        });

        smViewModel.getCountProducts( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewTotalItems.setText(String.valueOf(integer));
            }
        });

        smViewModel.getCountSelles( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewTotalCheckouts.setText(String.valueOf(integer));
            }
        });

        smViewModel.getCountBayersByTime( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewTotalCheckins.setText(String.valueOf(integer));
            }
        });




        smViewModel.getCountSellesDebts( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<List<Sell>>() {
            @Override
            public void onChanged(List<Sell> sells) {
                int total = 0;
                for (Sell s : sells){
                    total+=s.getRestAmount();
                }
                textViewTotalDebtsCustomers.setText("("+sells.size()+") "+total);
            }
        });

        smViewModel.getCountBuyersDebts( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<List<Buy>>() {
            @Override
            public void onChanged(List<Buy> buys) {
                int total = 0;
                for (Buy b : buys){
                    total+=b.getRestAmount();
                }
                textViewTotalDebtsSuppliers.setText("("+buys.size()+") "+total);
            }
        });



    }



}