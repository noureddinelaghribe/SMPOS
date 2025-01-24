package com.noureddine.stockmanagment;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.TimeUnit;
import database.Reposetry;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout buckup,restore,scheduleBackup,scheduleBackupButton,receptionButton,shareButton;
    boolean isExpanded = false;
    ImageView imageView;
    RadioGroup radioGroup;
    RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
    PeriodicWorkRequest periodicWorkRequest ;
    SharedPreferences sharedPreferences,sharedPreferencesActive;
    private static final String MYKEY = "RadioButton";
    private static final String MYKEYACTIVE = "TrialPrefs";


    //    private static final String SERVICE_ID = "com.example.myapp";
    private ConnectionsClient connectionsClient;
//    private String connectedEndpointId;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 1002;
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1003;
    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 104;

    Receive receive;
    Share share;

    @SuppressLint({"MissingInflatedId", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buckup = findViewById(R.id.LinearLayout_buckup);
        restore = findViewById(R.id.LinearLayout_restore);
        scheduleBackup = findViewById(R.id.scheduleBackup);
        scheduleBackupButton = findViewById(R.id.scheduleBackupButton);
        shareButton = findViewById(R.id.LinearLayout_share);
        receptionButton = findViewById(R.id.LinearLayout_reception);
        imageView = findViewById(R.id.imageView7);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        imageScheduleBackup(isExpanded);

        SMViewModel smViewModel = new ViewModelProvider(this).get(SMViewModel.class);
        sharedPreferences = getSharedPreferences(MYKEY, Context.MODE_PRIVATE);
        sharedPreferencesActive = getSharedPreferences(MYKEYACTIVE, Context.MODE_PRIVATE);

        connectionsClient = Nearby.getConnectionsClient(this);
        receive = new Receive(connectionsClient,this);
        share = new Share(connectionsClient,this);

        //requestExternalStoregePermissions();

        if (sharedPreferences.getInt("ScheduledBuckup",-1) == -1){
            cancelSpecificPeriodicWork();
            radioButton4.setChecked(true);
        }else {
            if (sharedPreferences.getInt("ScheduledBuckup",-1) == 1){
                radioButton1.setChecked(true);
            }else if(sharedPreferences.getInt("ScheduledBuckup",-1) == 2){
                radioButton2.setChecked(true);
            }else if(sharedPreferences.getInt("ScheduledBuckup",-1) == 3){
                radioButton3.setChecked(true);
            }else if(sharedPreferences.getInt("ScheduledBuckup",-1) == 4){
                radioButton4.setChecked(true);
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == radioButton1.getId()){
                    cancelSpecificPeriodicWork();
                    periodicWorkRequest = new PeriodicWorkRequest.Builder(ScheduledBuckup.class,1, TimeUnit.DAYS).build();
                    WorkManager.getInstance(SettingsActivity.this).enqueue(periodicWorkRequest);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("ScheduledBuckup",1);
                    editor.commit();

                }else if(checkedId == radioButton2.getId()){
                    cancelSpecificPeriodicWork();
                    periodicWorkRequest = new PeriodicWorkRequest.Builder(ScheduledBuckup.class,3, TimeUnit.DAYS).build();
                    WorkManager.getInstance(SettingsActivity.this).enqueue(periodicWorkRequest);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("ScheduledBuckup",2);
                    editor.commit();

                }else if(checkedId == radioButton3.getId()){
                    cancelSpecificPeriodicWork();
                    periodicWorkRequest = new PeriodicWorkRequest.Builder(ScheduledBuckup.class,8, TimeUnit.DAYS).build();
                    WorkManager.getInstance(SettingsActivity.this).enqueue(periodicWorkRequest);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("ScheduledBuckup",3);
                    editor.commit();

                }else if(checkedId == radioButton4.getId()){
                    cancelSpecificPeriodicWork();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("ScheduledBuckup",4);
                    editor.commit();

                }

            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //for sending

                if(sharedPreferences.getBoolean("active", false)){
                    turnOnLocation();
                }else {
                    Toast.makeText(SettingsActivity.this, "البرنامج غير مفعل", Toast.LENGTH_SHORT).show();
                }

            }
        });

        receptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //for reciveing

                if(sharedPreferences.getBoolean("active", false)){
                    manageFileRequestPermissions();
                }else {
                    Toast.makeText(SettingsActivity.this, "البرنامج غير مفعل", Toast.LENGTH_SHORT).show();
                }

            }
        });

        scheduleBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                imageScheduleBackup(isExpanded);
            }
        });

        buckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                smViewModel.backupDatabase();

                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

                Toast.makeText(SettingsActivity.this, "تم نسخ", Toast.LENGTH_SHORT).show();

            }
        });

        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sharedPreferences.getBoolean("active", false)){

                    new AlertDialog.Builder(SettingsActivity.this)
                            .setTitle("تأكيد الاستعادة")
                            .setMessage("هل أنت متأكد أنك تريد استعادة النسخة الاحتياطية؟ سيتم استبدال البيانات الحالية.")
                            .setPositiveButton("استعادة", (dialog, which) -> {
                                // Show progress dialog
                                ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
                                progressDialog.setMessage("استعادة قاعدة البيانات...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                smViewModel.restoreDatabase(new Reposetry.RestoreCallback() {
                                    @Override
                                    public void onRestoreComplete(boolean success) {
                                        if(success){
                                            Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                                Toast.makeText(SettingsActivity.this, "تم الاستعادة", Toast.LENGTH_SHORT).show();

                            })
                            .setNegativeButton("Cancel", null)
                            .show();

                }else {
                    Toast.makeText(SettingsActivity.this, "البرنامج غير مفعل", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                share.startDiscovery();
            } else {
                Log.d("NearbyStockManagment", "Location permission is required for discovery");
            }
        }

        if (requestCode == 1010) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("NearbyStockManagment", "Permission granted. Now you can access storage.");
            } else {
                Log.e("NearbyStockManagment", "Permission denied. Cannot access storage.");
            }
        }

        if (requestCode == 1006) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("NearbyStockManagment", "User enabled location.");
            } else {
                Log.e("NearbyStockManagment", "User denied location settings change.");
            }
        }

        if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    receive.startAdvertising();
                } else {
                    Log.e("NearbyStockManagment", "User denied manage file.");
                }
            }
        }

    }



    private void requestPermissionsIfNecessary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestBluetoothPermissions();
            } else {
                share.startDiscovery();
            }
        } else {
            share.startDiscovery();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void requestBluetoothPermissions() {
        if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    android.Manifest.permission.BLUETOOTH_SCAN,
                    android.Manifest.permission.BLUETOOTH_CONNECT
            }, BLUETOOTH_PERMISSION_REQUEST_CODE);
        } else {
            share.startDiscovery();
        }
    }


    private void turnOnLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true); // Show dialog even if location settings are already satisfied

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            // Location settings are satisfied, proceed with location-based tasks
            Log.d("NearbyStockManagment", "Location settings are satisfied.");

            requestPermissionsIfNecessary();

        });

        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult()
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(this, 1006);
                } catch (IntentSender.SendIntentException sendEx) {
                    Log.e("NearbyStockManagment", "Error opening settings dialog.", sendEx);
                }
            }
        });
    }



    public void manageFileRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_EXTERNAL_STORAGE);
            }else {
                receive.startAdvertising();
            }
        }
    }


    public void imageScheduleBackup(boolean isExpanded){
        if (isExpanded){
            imageView.setImageResource(R.drawable.arrow_up_sign_to_navigate);
            scheduleBackupButton.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(scheduleBackupButton,"alpha",0f,1f);
            objectAnimator.setDuration(300);
            objectAnimator.start();
        }else {
            imageView.setImageResource(R.drawable.arrow_down_sign_to_navigate);
            scheduleBackupButton.setVisibility(View.GONE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(scheduleBackupButton,"alpha",1f,0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();
        }
    }

    // Cancel the specific periodic work request
    private void cancelSpecificPeriodicWork() {
        WorkManager.getInstance(this).cancelAllWork();
    }







}
