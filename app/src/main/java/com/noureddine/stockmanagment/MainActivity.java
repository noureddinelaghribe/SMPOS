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


        sharedPreferences = getSharedPreferences(MYKEY,0);


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