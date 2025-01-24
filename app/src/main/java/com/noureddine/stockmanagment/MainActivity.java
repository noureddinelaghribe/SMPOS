package com.noureddine.stockmanagment;

import static android.app.PendingIntent.getActivity;
import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    LinearLayout sell,buy,suppliers,customers,stock,history,settings,share,info;
    ConstraintLayout notification;
    TextView textViewNotification;

    SharedPreferences sharedPreferences;
    private static final String MYKEY = "TrialPrefs";

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sell = findViewById(R.id.LinearLayout_restore_drive);
        buy = findViewById(R.id.LinearLayout_buckup_drive);
        suppliers = findViewById(R.id.LinearLayout3);
        customers = findViewById(R.id.LinearLayout4);
        stock = findViewById(R.id.LinearLayout5);
        history = findViewById(R.id.LinearLayout6);
        settings = findViewById(R.id.ButtonLinearLayout1);
        notification = findViewById(R.id.ButtonLinearLayout2);
        share = findViewById(R.id.ButtonLinearLayout3);
        info = findViewById(R.id.ButtonLinearLayout4);
        textViewNotification = findViewById(R.id.textView_num_notification);

        textViewNotification.setVisibility(GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1000);
            }
        }

//        long starDate = System.currentTimeMillis()-86400000;
//        long sevenDays = System.currentTimeMillis()+86400000*7;
//
        sharedPreferences = getSharedPreferences(MYKEY,0);
//
//        if (!sharedPreferences.contains("starTrial")){
//
//            Log.d("sharedPreferences", "onCreate: key 1" );
//
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putLong("starTrial",System.currentTimeMillis());
//            editor.putLong("lastLog",System.currentTimeMillis());
//            editor.putLong("endTrial",sevenDays);
//            editor.putBoolean("trial",true);
//            editor.putBoolean("active",false);
//            editor.putString("key",generateLicenseKey());
//            editor.apply();
//        }else {
//            if ( !sharedPreferences.getBoolean("active",false) && sharedPreferences.getBoolean("trial",true)) {
//
//                Log.d("sharedPreferences", "onCreate: key 2" );
//
//                if (sharedPreferences.getLong("lastLog",0) >= sharedPreferences.getLong("starTrial",0)
//                        && sharedPreferences.getLong("lastLog",0) < System.currentTimeMillis()
//                        && sharedPreferences.getLong("lastLog",0) <= sharedPreferences.getLong("endTrial",0)){
//
//                    Log.d("sharedPreferences", "onCreate: key 3" );
//
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putLong("lastLog",System.currentTimeMillis());
//                    editor.apply();
//                }else {
//
//                    Log.d("sharedPreferences", "onCreate: key 4" );
//
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("trial",false);
//                    editor.apply();
//
//                    Intent intent = new Intent(MainActivity.this,ActiveLicenseActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }else{
//
//                Log.d("sharedPreferences", "onCreate: key 5" );
//
//                Intent intent = new Intent(MainActivity.this,ActiveLicenseActivity.class);
//                startActivity(intent);
//                finish();
//
//            }
//        }


//
//        Log.d("sharedPreferences", "onCreate: key "+ sharedPreferences.getString("key",null));
//        Log.d("sharedPreferences", "onCreate: active "+ sharedPreferences.getBoolean("active",true));
//        Log.d("sharedPreferences", "onCreate: if 1  "+ (sharedPreferences.getLong("lastLog",0) >= sharedPreferences.getLong("starTrial",0)));
//        Log.d("sharedPreferences", "onCreate: if 2  "+ (sharedPreferences.getLong("lastLog",0) < System.currentTimeMillis()));
//        Log.d("sharedPreferences", "onCreate: if 3  "+ (sharedPreferences.getLong("lastLog",0) <= sharedPreferences.getLong("endTrial",0)));
//        Log.d("sharedPreferences", "onCreate: starTrial "+ sharedPreferences.getLong("starTrial",0) +" "+longToDate(sharedPreferences.getLong("starTrial",0)));
//        Log.d("sharedPreferences", "onCreate: endTrial "+ sharedPreferences.getLong("endTrial",0) +" "+longToDate(sharedPreferences.getLong("endTrial",0)));
//        Log.d("sharedPreferences", "onCreate: lastLog "+ sharedPreferences.getLong("lastLog",0) +" "+longToDate(sharedPreferences.getLong("lastLog",0)));
//        Log.d("sharedPreferences", "onCreate: now "+ System.currentTimeMillis() +" "+longToDate(System.currentTimeMillis()));


        databaseReference = FirebaseDatabase.getInstance().getReference();

        long ONE_DAY = 24 * 60 * 60 * 1000; // Milliseconds in a day
        long TRIAL_DURATION = 7 * ONE_DAY;
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        long currentTime = System.currentTimeMillis();
        long startTrial = sharedPreferences.getLong("starTrial", -1);
        long endTrial = sharedPreferences.getLong("endTrial", -1);
        long lastLog = sharedPreferences.getLong("lastLog", 0);

        if (startTrial == -1) {
            // Initialize trial period
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("starTrial", currentTime);
            editor.putLong("endTrial", currentTime + TRIAL_DURATION);
            editor.putLong("lastLog", currentTime);
            editor.putBoolean("trial", true);
            editor.putBoolean("active", false);
            editor.putBoolean("sendToFirebase", false);
            editor.putString("key", generateLicenseKey());
            editor.apply();

            sendMessage(androidId,sharedPreferences.getString("key",null));

        } else {
            isActive = sharedPreferences.getBoolean("active", false);
            boolean isTrial = sharedPreferences.getBoolean("trial", true);

            if (!sharedPreferences.getBoolean("sendToFirebase",false)){
                sendMessage(androidId,sharedPreferences.getString("key",null));
            }

            if (!isActive && isTrial) {
                if (lastLog >= startTrial && lastLog <= endTrial) {
                    if (currentTime < lastLog) {

                        // Optionally terminate the app or restrict access
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("trial", false);
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this,ActiveLicenseActivity.class);
                        startActivity(intent);
                        finish();

                    } else {

                        // Update last log time
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("lastLog", currentTime);
                        editor.apply();

                    }
                } else {

                    // Trial expired
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("trial", false);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this,ActiveLicenseActivity.class);
                    startActivity(intent);
                    finish();

                }
            } else {
                if (!isActive){
                    Intent intent = new Intent(MainActivity.this,ActiveLicenseActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }

        Log.d("sharedPreferences", "onCreate: key "+ sharedPreferences.getString("key",null));













        SMViewModel smViewModel = new ViewModelProvider(this).get(SMViewModel.class);

        smViewModel.getCountProductsStockexpiryDate(currentTime,currentTime+TRIAL_DURATION).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer > 0){
                    textViewNotification.setVisibility(View.VISIBLE);
                    textViewNotification.setText(String.valueOf(integer));
                }else {
                    textViewNotification.setVisibility(GONE);
                }
            }
        });













//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("product", " "+smViewModel.getProductByIdp("2025"));
//            }
//        }).start();
//
//        smViewModel.getAllSuppliers().observe(MainActivity.this, new Observer<List<Supplier>>() {
//            @Override
//            public void onChanged(List<Supplier> suppliers) {
//                for (Supplier s : suppliers){
//                    Log.d("Supplier", "Supplier: "+s.getId()+" "+s.getName());
//                }
//            }
//        });
//
//        smViewModel.getAllCustomers().observe(MainActivity.this, new Observer<List<Customer>>() {
//            @Override
//            public void onChanged(List<Customer> customers) {
//                for (Customer c : customers){
//                    Log.d("customers", "customers: "+c.getId()+" "+c.getName());
//                }
//            }
//        });
//
//        smViewModel.getAllProduct().observe(MainActivity.this, new Observer<List<Product>>() {
//            @Override
//            public void onChanged(List<Product> products) {
//                for (Product p : products){
//                    //String info, int quantity, int limit, long expiryDate, String imagePath, Double priceBuy, Double priceSell, String idp, String name
//                    Log.d("products", "Product : "+
//                            "id "+p.getId()+" "+
//                            p.getName()+" "+
//                            "idp "+p.getIdp()+" "+
//                            p.getPriceSell()+" "+
//                            p.getPriceBuy()+" "+
//                            p.getExpiryDate()+" "+
//                            p.getLimit()+" "+
//                            p.getQuantity()+" "+
//                            p.getInfo()+" "+
//                            p.getImagePath()
//                    );
//
//                }
//            }
//        });
//
//        smViewModel.getAllSelles().observe(MainActivity.this, new Observer<List<Sell>>() {
//            @Override
//            public void onChanged(List<Sell> sells) {
//                for (Sell s : sells){
//                    Log.d("TAG", "onChanged: "+s.getId()+" "+s.getpaymentAmount());
//                }
//            }
//        });
//
//        smViewModel.getAllBuyers().observe(MainActivity.this, new Observer<List<Buy>>() {
//            @Override
//            public void onChanged(List<Buy> buys) {
//                for (Buy b : buys){
//                    Log.d("TAG", "All Buyers : "+b.getId()+" "+b.getTypePayment()+" "+b.getTotalAmount());
//                }
//            }
//        });
//
        // int product_id, int custmer_id, int supplier_id, String type, int quantity, long timestamp
//        smViewModel.inserttransaction(new Transactions(1,1,1,"try-1",0,10));
//        smViewModel.inserttransaction(new Transactions(2,2,2,"try-2",0,15));
//        smViewModel.inserttransaction(new Transactions(3,3,3,"try-3",0,85));
//        smViewModel.inserttransaction(new Transactions(4,4,4,"try-4",0,44));
//        smViewModel.inserttransaction(new Transactions(5,5,5,"try-5",0,100000960));
//        smViewModel.inserttransaction(new Transactions(6,6,6,"try-6",0,100000320));
//        smViewModel.inserttransaction(new Transactions(7,7,7,"try-7",0,100000520));
//        smViewModel.inserttransaction(new Transactions(8,8,8,"try-8",0,100000450));



//        smViewModel.getAllTransactions().observe(MainActivity.this, new Observer<List<Transactions>>() {
//            @Override
//            public void onChanged(List<Transactions> transactions) {
//                for (Transactions t : transactions){
//                    Log.d("Transactions", "All transactions : "+t.getId()+" "+t.getType()+" "+t.getTimestamp());
//                }
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("Transactions", "onCreate: smViewModel.getCounttransactions() "+smViewModel.getCounttransactions());
//            }
//        }).start();


//        smViewModel.timeTransactions(10,85).observe(MainActivity.this, new Observer<List<Transactions>>() {
//            @Override
//            public void onChanged(List<Transactions> transactions) {
//                for (Transactions t : transactions){
//                    Log.d("Transactions", "spicific transactions : "+t.getId()+" "+t.getType());
//                }
//            }
//        });


        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuySellActivity.class);
                intent.putExtra("type","sell");
                startActivity(intent);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuySellActivity.class);
                intent.putExtra("type","buy");
                startActivity(intent);
            }
        });

        suppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomerSuppliersActivity.class);
                intent.putExtra("type","suppliers");
                startActivity(intent);
            }
        });

        customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomerSuppliersActivity.class);
                intent.putExtra("type","customers");
                startActivity(intent);
            }
        });

        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StockActivity.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReportsActivity.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActive){
                    Toast.makeText(MainActivity.this, "التطبيق مفعل", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, ActiveLicenseActivity.class);
                    startActivity(intent);
                }

            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
////                        for (int i = 1; i <=10000;i++){
////
////                            smViewModel.insertProduct(new Product("info : "+i,100,10,100000,"",10.00,12.00,i+"00","name P"+i,100000,false));
////                            smViewModel.insertCustomer(new Customer("C"+i,"","","",100000,false));
////                            smViewModel.insertSupplier(new Supplier("S"+i,"","","",100000,false));
////
////                            Log.d("finished", "onClick: "+i);
////
////                        }
//
//                    }
//                }).start();

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });


    }




    public static String generateLicenseKey() {
        StringBuilder licenseKey = new StringBuilder(6); // Length of the license key

        for (int i = 0; i < 6; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            licenseKey.append(CHARACTERS.charAt(randomIndex));
        }

        return licenseKey.toString();
    }



    private void sendMessage( String key , String message ) {

        // Send the message to the database
        databaseReference.child("devices").child(key).setValue(message)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("sendToFirebase", true);
                        editor.apply();
                    }
                });
    }


}