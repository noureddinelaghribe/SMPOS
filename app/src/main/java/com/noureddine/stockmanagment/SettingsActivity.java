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
import com.noureddine.stockmanagment.R;

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
                //requestPermissionsIfNecessary();

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
                //receive.startAdvertising();

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

                //backupDatabase(SettingsActivity.this);

                smViewModel.backupDatabase();

                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
//                System.exit(0);

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
                                            //System.exit(0);
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


//    private void requestExternalStoregePermissions() {
//
//        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
//                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            }, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
//        }
//
//    }



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
//
//    private void checkAndRequestPermissions() {
//        String[] permissions = {
//                android.Manifest.permission.ACCESS_FINE_LOCATION,
//                android.Manifest.permission.ACCESS_WIFI_STATE,
//                android.Manifest.permission.CHANGE_WIFI_STATE,
//                android.Manifest.permission.INTERNET,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//        };
//
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        for (String permission : permissions) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(permission);
//            }
//        }
//
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 100);
//        }
//    }







//    private void startAdvertising() {
//        connectionsClient.startAdvertising(
//                "HostDevice", // Name of the device
//                SERVICE_ID, // Unique service ID
//                connectionLifecycleCallback, // Connection lifecycle callbacks
//                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build() // Strategy
//        ).addOnSuccessListener(unused -> {
//            Log.d("NearbyStockManagment", "Advertising started successfully");
//        }).addOnFailureListener(e -> {
//            Log.e("NearbyStockManagment", "Advertising failed", e);
//        });
//    }
//
//    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
//        @Override
//        public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
//            connectionsClient.acceptConnection(endpointId, payloadCallback);
//        }
//
//        @Override
//        public void onConnectionResult(String endpointId, ConnectionResolution result) {
//            if (result.getStatus().getStatusCode() == ConnectionsStatusCodes.STATUS_OK) {
//                Log.d("NearbyStockManagment", "Connection successful with endpoint: " + endpointId);
//            } else {
//                Log.e("NearbyStockManagment", "Connection failed with endpoint: " + endpointId);
//            }
//        }
//
//        @Override
//        public void onDisconnected(String endpointId) {
//            Log.d("NearbyStockManagment", "Disconnected from endpoint: " + endpointId);
//        }
//    };
//
//    private final PayloadCallback payloadCallback = new PayloadCallback() {
//        @Override
//        public void onPayloadReceived(String endpointId, Payload payload) {
//            if (payload.getType() == Payload.Type.FILE) {
////                File receivedFile = payload.asFile().asJavaFile();
////                Log.d("NearbyStockManagment", "File received: " + receivedFile.getAbsolutePath());
//
//                if (payload.getType() == Payload.Type.FILE) {
//                    // Get the received file
//                    File receivedFile = payload.asFile().asJavaFile();
//                    Log.d("NearbyStockManagment", "File received at: " + receivedFile.getAbsolutePath());
//
//                    // Define the target directory and file
//                    File targetDir = new File(getExternalFilesDir(null), "SMDatabase_Backups");
//                    if (!targetDir.exists()) {
//                        targetDir.mkdirs(); // Ensure the directory exists
//                    }
//
//                    File targetFile = new File(targetDir, "SMDatabase_backup.db");
//
//                    // Move the received file to the target directory
//                    boolean isMoved = receivedFile.renameTo(targetFile);
//                    if (isMoved) {
//                        Log.d("Nearby", "File moved to: " + targetFile.getAbsolutePath());
//                    } else {
//                        Log.e("Nearby", "Failed to move the file.");
//                    }
//
//
//                } else {
//                    Log.d("NearbyStockManagment", "Received non-file payload");
//                }
//
//            }
//        }
//
//        @Override
//        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
//            // Handle progress updates for file transfers
//        }
//
//    };

//
//    @Override
//    public void onPayloadReceived(String endpointId, Payload payload) {
//        if (payload.getType() == Payload.Type.FILE) {
//            File receivedFile = payload.asFile().asJavaFile();
//
//            if (receivedFile != null) {
//                Log.d("Nearby", "File received at: " + receivedFile.getAbsolutePath());
//                // Move the file to a permanent location if needed
//            } else {
//                Log.e("Nearby", "Received file is null");
//            }
//        } else {
//            Log.d("Nearby", "Received non-file payload");
//        }
//    }

















//
//    private void requestPermissionsIfNecessary() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                requestBluetoothPermissions();
//            } else {
//                share.startDiscovery();
//            }
//        } else {
//            share.startDiscovery();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.S)
//    private void requestBluetoothPermissions() {
//        if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
//                checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{
//                    android.Manifest.permission.BLUETOOTH_SCAN,
//                    android.Manifest.permission.BLUETOOTH_CONNECT
//            }, BLUETOOTH_PERMISSION_REQUEST_CODE);
//        } else {
//            share.startDiscovery();
//        }
//    }
//
//
//    private void requestExternalStoregePermissions() {
//
//        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
//                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            }, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
//        }
//
//    }


//    public void startDiscovery() {
//        connectionsClient.startDiscovery(
//                SERVICE_ID,
//                endpointDiscoveryCallback,
//                new com.google.android.gms.nearby.connection.DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
//        ).addOnSuccessListener(unused -> {
//            Log.d("NearbyStockManagment", "Discovery started successfully");
//            Toast.makeText(this, "Discovery started", Toast.LENGTH_SHORT).show();
//        }).addOnFailureListener(e -> {
//            Log.e("NearbyStockManagment", "Discovery failed", e);
//            Toast.makeText(this, "Discovery failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        });
//    }
//
//
//    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
//        @Override
//        public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
//            Log.d("NearbyStockManagment", "Endpoint found: " + endpointId);
//            Toast.makeText(SettingsActivity.this, "Device found: " + info.getEndpointName(), Toast.LENGTH_SHORT).show();
//
//            // Request connection to the found endpoint
//            connectionsClient.requestConnection(
//                            "DiscovererDevice", // Local device name
//                            endpointId,
//                            connectionLifecycleCallbackDiscovery
//                    ).addOnSuccessListener(unused -> Log.d("NearbyStockManagment", "Connection request sent"))
//                    .addOnFailureListener(e -> Log.e("NearbyStockManagment", "Connection request failed", e));
//        }
//
//        @Override
//        public void onEndpointLost(String endpointId) {
//            Log.d("NearbyStockManagment", "Endpoint lost: " + endpointId);
//            Toast.makeText(SettingsActivity.this, "Device lost: " + endpointId, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    /**
//     * Callback for managing connection lifecycle events.
//     */
//    private final ConnectionLifecycleCallback connectionLifecycleCallbackDiscovery = new ConnectionLifecycleCallback() {
//        @Override
//        public void onConnectionInitiated(String endpointId, com.google.android.gms.nearby.connection.ConnectionInfo connectionInfo) {
//            Log.d("NearbyStockManagment", "Connection initiated with endpoint: " + endpointId);
//            connectionsClient.acceptConnection(endpointId, payloadCallbackDiscovery);
//        }
//
//        @Override
//        public void onConnectionResult(String endpointId, ConnectionResolution result) {
//            if (result.getStatus().getStatusCode() == ConnectionsStatusCodes.STATUS_OK) {
//                connectedEndpointId = endpointId;
//                Log.d("NearbyStockManagment", "Connected to endpoint: " + endpointId);
//                Toast.makeText(SettingsActivity.this, "Connected to device", Toast.LENGTH_SHORT).show();
//
////                File filePath = new File("/storage/emulated/0/Android/data/com.example.stockmanagment/files/SMDatabase_Backups/SMDatabase_backup.db");
//
//                // Define the target directory and file
//                File targetDir = new File(getExternalFilesDir(null), "SMDatabase_Backups");
//                if (!targetDir.exists()) {
//                    Toast.makeText(SettingsActivity.this, "لا توجد بيانات لمشاركتها", Toast.LENGTH_SHORT).show();
//                }
//
//                File targetFile = new File(targetDir, "SMDatabase_backup.db");
//                sendFile(targetFile);
//
//
//            } else {
//                Log.e("NearbyStockManagment", "Connection failed with endpoint: " + endpointId);
//                Toast.makeText(SettingsActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onDisconnected(String endpointId) {
//            Log.d("NearbyStockManagment", "Disconnected from endpoint: " + endpointId);
//            Toast.makeText(SettingsActivity.this, "Disconnected from device", Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    /**
//     * Callback for handling payload transfers.
//     */
//    private final PayloadCallback payloadCallbackDiscovery = new PayloadCallback() {
//        @Override
//        public void onPayloadReceived(String endpointId, Payload payload) {
//            if (payload.getType() == Payload.Type.BYTES) {
//                String receivedMessage = new String(payload.asBytes());
//                Log.d("NearbyStockManagment", "Payload received: " + receivedMessage);
//                Toast.makeText(SettingsActivity.this, "Received: " + receivedMessage, Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
//            // Handle payload transfer updates if necessary
//        }
//    };
//
//    private void sendFile(File fileToSend) {
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            if (fileToSend.exists() && fileToSend.canRead()) {
//                Payload filePayload = null;
//                try {
//                    filePayload = Payload.fromFile(fileToSend);
//                } catch (FileNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//                connectionsClient.sendPayload(connectedEndpointId, filePayload)
//                        .addOnSuccessListener(unused -> Log.d("Nearby", "File sent successfully"))
//                        .addOnFailureListener(e -> Log.e("Nearby", "Failed to send file", e));
//            } else {
//                Log.e("NearbyStockManagment", "File not found or cannot be read");
//            }
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1009);
//        }
//    }


//    private void discoverPeers() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Log.d("Sender", "Discovery started");
//            }
//
//            @Override
//            public void onFailure(int reason) {
//                Log.d("Sender", "Discovery failed: " + reason);
//            }
//        });
//    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // إذا تم منح الإذن، استدعِ discoverPeers
//                Toast.makeText(this, "Permission success.", Toast.LENGTH_SHORT).show();
//                discoverPeers();
//            } else {
//                // إذا تم رفض الإذن
//                Toast.makeText(this, "Permission denied. Cannot discover peers.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//
//    private void discoverPeers() {
//        // تحقق من إذن الموقع
//        if (ContextCompat.checkSelfPermission(this,  android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // طلب الإذن إذا لم يكن مُمنحًا
//            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
//            return;
//        }
//
//        WifiP2pManager manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        WifiP2pManager.Channel channel = manager.initialize(this, getMainLooper(), null);
//
//        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(SettingsActivity.this, "Discovering peers...", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int reason) {
//                Toast.makeText(SettingsActivity.this, "Failed to discover peers: " + reason, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
//            @Override
//            public void onPeersAvailable(WifiP2pDeviceList peerList) {
//                Collection<WifiP2pDevice> devices = peerList.getDeviceList();
//
//                if (devices.isEmpty()) {
//                    Toast.makeText(SettingsActivity.this, "No devices found", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                StringBuilder deviceInfo = new StringBuilder("Discovered devices:\n");
//                for (WifiP2pDevice device : devices) {
//                    deviceInfo.append("Name: ").append(device.deviceName)
//                            .append(", Address: ").append(device.deviceAddress)
//                            .append("\n");
//                }
//
//                Toast.makeText(SettingsActivity.this, deviceInfo.toString(), Toast.LENGTH_LONG).show();
//                Log.d("WifiP2pDevices", deviceInfo.toString());
//            }
//        });
//    }







//    public static void backupDatabase(Context context) {
//        String BACKUP_FOLDER_NAME = "SMDatabase_Backups";
//        String BACKUP_FILE_NAME = "SMDatabase_backup.db";
//        SMDatabase appDatabase = SMDatabase.getDatabase(context);
//        appDatabase.close();
//        File dbfile = context.getDatabasePath(DATABASE_NAME);
//        File sdir = new File(context.getExternalFilesDir(null), BACKUP_FOLDER_NAME);
//        String sfpath = sdir.getPath() + File.separator + BACKUP_FILE_NAME;
//        if (!sdir.exists()) {
//            sdir.mkdirs();
//        } else {
//            checkAndDeleteBackupFile(sdir, sfpath);
//        }
//        File savefile = new File(sfpath);
//        if (savefile.exists()) {
//            Log.d("LOGGER", "File exists. Deleting it and then creating new file.");
//            savefile.delete();
//        }
//        try {
//            if (savefile.createNewFile()) {
//                int buffersize = 8 * 1024;
//                byte[] buffer = new byte[buffersize];
//                int bytes_read = buffersize;
//                OutputStream savedb = new FileOutputStream(sfpath);
//                InputStream indb = new FileInputStream(dbfile);
//                while ((bytes_read = indb.read(buffer, 0, buffersize)) > 0) {
//                    savedb.write(buffer, 0, bytes_read);
//                }
//                savedb.flush();
//                indb.close();
//                savedb.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("LOGGER", "ex: " + e);
//        }
//    }
//
//    public static void checkAndDeleteBackupFile(File directory, String path) {
//        //This is to prevent deleting extra file being deleted which is mentioned in previous comment lines.
//        int MAXIMUM_DATABASE_FILE = 10000;
//        File currentDateFile = new File(path);
//        int fileIndex = -1;
//        long lastModifiedTime = System.currentTimeMillis();
//
//        if (!currentDateFile.exists()) {
//            File[] files = directory.listFiles();
//            if (files != null && files.length >= MAXIMUM_DATABASE_FILE) {
//                for (int i = 0; i < files.length; i++) {
//                    File file = files[i];
//                    long fileLastModifiedTime = file.lastModified();
//                    if (fileLastModifiedTime < lastModifiedTime) {
//                        lastModifiedTime = fileLastModifiedTime;
//                        fileIndex = i;
//                    }
//                }
//
//                if (fileIndex != -1) {
//                    File deletingFile = files[fileIndex];
//                    if (deletingFile.exists()) {
//                        deletingFile.delete();
//                    }
//                }
//            }
//        }
//    }




//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_DATABASE_REQUEST_CODE && resultCode == RESULT_OK) {
//            Uri selectedFileUri = data.getData();
//
//            // Validate file
//            if (isValidDatabaseFile(selectedFileUri)) {
//                // Make a backup of the current database
//                makeBackupOfCurrentDatabase();
//
//                // Restore the selected database
//                restoreDatabase(selectedFileUri);
//
//                // Validate the restored database
//                if (validateRestoredDatabase()) {
//                    Log.d("Restore", "Database restored successfully!");
//                } else {
//                    Log.e("Restore", "Invalid database file");
//                }
//            } else {
//                Log.e("Restore", "Selected file is not a valid database file");
//            }
//        }
//    }
//
//    private boolean isValidDatabaseFile(Uri uri) {
//        // Implement logic to check if the file is a valid database
//        // For example, you can check if the file contains a user table
//        return true; // Placeholder, you should implement your actual validation logic here
//    }
//
//    private void makeBackupOfCurrentDatabase() {
//        File currentDbFile = getDatabasePath(DATABASE_NAME);
//        File backupFolder = new File(getExternalFilesDir(null), BACKUP_FOLDER_NAME);
//        if (!backupFolder.exists()) {
//            backupFolder.mkdir();
//        }
//        File backupFile = new File(backupFolder, BACKUP_FILE_NAME);
//
//        try {
//            // Copy the current database to backup file
//            copyFile(currentDbFile, backupFile);
//            Log.d("Backup", "Database backup created.");
//        } catch (IOException e) {
//            Log.e("Backup", "Failed to create database backup.", e);
//        }
//    }
//
//
//    // Method to restore the database from backup
//    public static void restoreDatabase(Context context) {
//        // Path to the backup file
//        File backupFile = new File(context.getExternalFilesDir(null), "SMDatabase_Backups/SMDatabase_backup.db");
//
//        Log.d("TAG", "restoreDatabase: "+backupFile.getPath());
//
//        // Path to the destination database (the database that you want to restore)
//        File destinationDatabase = context.getDatabasePath(DATABASE_NAME);
//
//        Log.d("TAG", "restoreDatabase: "+destinationDatabase.getPath());
//
//        if (backupFile.exists()) {
//            try {
//                // Open the backup file
//                InputStream inputStream = new FileInputStream(backupFile);
//
//                // Open the destination database file for writing
//                OutputStream outputStream = new FileOutputStream(destinationDatabase);
//
//                // Buffer for copying the data
//                byte[] buffer = new byte[1024];
//                int length;
//
//                // Copy the backup data to the destination database
//                while ((length = inputStream.read(buffer)) > 0) {
//                    outputStream.write(buffer, 0, length);
//                }
//
//                // Close the streams after the copy is complete
//                inputStream.close();
//                outputStream.close();
//
//                Log.d("TAG", "Database restored successfully!");
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("TAG", "Error while restoring database: " + e.getMessage());
//            }
//        } else {
//            Log.e("TAG", "Backup file does not exist at: " + backupFile.getAbsolutePath());
//        }
//    }
//
//    public void restoreDatabase(InputStream inputStream) {
//        // Your logic to restore the database using the inputStream
//        try {
//            // Example: Using the inputStream to restore the database
//            // You'll need to copy the input stream content to your database file
//            File sdir = new File(context.getExternalFilesDir(null), BACKUP_FOLDER_NAME);
//            String sfpath = sdir.getPath() + File.separator + BACKUP_FILE_NAME;
//            FileOutputStream databaseOutputStream = new FileOutputStream(getDatabasePath(sfpath));
//
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                databaseOutputStream.write(buffer, 0, length);
//            }
//
//            databaseOutputStream.flush();
//            databaseOutputStream.close();
//            inputStream.close();
//
//            // Successfully restored database
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Handle exception (e.g., log, show error message)
//        }
//    }
//
//
//    private void copyFile(File srcFile, File destFile) throws IOException {
//        try (FileChannel src = new FileInputStream(srcFile).getChannel();
//             FileChannel dst = new FileOutputStream(destFile).getChannel()) {
//            dst.transferFrom(src, 0, src.size());
//        }
//    }
//
//    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = inputStream.read(buffer)) > 0) {
//            outputStream.write(buffer, 0, length);
//        }
//        inputStream.close();
//        outputStream.close();
//    }
//
//    private boolean validateRestoredDatabase() {
//        // Implement custom validation for the restored database
//        // For example, check if a "user" table exists and has data
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(getDatabasePath(DATABASE_NAME).getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM user", null);
//        cursor.moveToFirst();
//        int rowCount = cursor.getInt(0);
//        cursor.close();
//
//        return rowCount > 0;  // Assuming there must be at least one user for a valid database
//    }
//
//    // Clean up backup file after success
//    private void deleteBackupFile() {
//        File backupFile = new File(getExternalFilesDir(null), BACKUP_FOLDER_NAME + "/" + BACKUP_FILE_NAME);
//        if (backupFile.exists()) {
//            backupFile.delete();
//            Log.d("Backup", "Backup file deleted.");
//        }
//    }




//
//    private void restoreDBIntent() {
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.setType("*/*");
//        startActivityForResult(Intent.createChooser(i, "Select DB File"), 12);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {
//            Uri fileUri = data.getData();
//            try {
//                assert fileUri != null;
//                InputStream inputStream = getContentResolver().openInputStream(fileUri);
//                if (validFile(fileUri)) {
//                    restoreDatabase(inputStream);
//                } else {
//                    Utils.showSnackbar(findViewById(android.R.id.content), getString("restore failed"), 1);
//                }
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private boolean validFile(Uri fileUri) {
//        ContentResolver cr = this.getContentResolver();
//        String mime = cr.getType(fileUri);
//        return "application/octet-stream".equals(mime);
//    }
//
//    public static void backupDatabaseForRestore(Activity activity, Context context) {
//        File dbfile = activity.getDatabasePath(DATABASE_NAME);
//        File sdir = new File(getFilePath(context, 0), "backup");
//        String sfpath = sdir.getPath() + File.separator + BACKUP_RESTORE_ROLLBACK_FILE_NAME;
//        if (!sdir.exists()) {
//            sdir.mkdirs();
//        }
//        File savefile = new File(sfpath);
//        if (savefile.exists()) {
//            Log.d("LOGGER", "Backup Restore - File exists. Deleting it and then creating new file.");
//            savefile.delete();
//        }
//        try {
//            if (savefile.createNewFile()) {
//                int buffersize = 8 * 1024;
//                byte[] buffer = new byte[buffersize];
//                int bytes_read = buffersize;
//                OutputStream savedb = new FileOutputStream(sfpath);
//                InputStream indb = new FileInputStream(dbfile);
//                while ((bytes_read = indb.read(buffer, 0, buffersize)) > 0) {
//                    savedb.write(buffer, 0, bytes_read);
//                }
//                savedb.flush();
//                indb.close();
//                savedb.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("LOGGER", "ex for restore file: " + e);
//        }
//    }
//
//    private void restoreDatabase(InputStream inputStreamNewDB) {
//        SMDatabase appDatabase = SMDatabase.getDatabase(context);
//        appDatabase.close();
//        //Delete the existing restoreFile and create a new one.
////        sharedPreferences.edit().putBoolean("restoringDatabase", true).apply();
////        deleteRestoreBackupFile(getApplicationContext());
////        backupDatabaseForRestore(this, getApplicationContext());
//
//        File oldDB = this.getDatabasePath(DATABASE_NAME);
//        if (inputStreamNewDB != null) {
//            try {
//                Utils.copyFile((FileInputStream) inputStreamNewDB, new FileOutputStream(oldDB));
//                Utils.showSnackbar(findViewById(android.R.id.content), getString("restore success"), 1);
//                //Take the user to home screen and there we will validate if the database file was actually restored correctly.
//            } catch (IOException e) {
//                Log.d("LOGGER", "ex for is of restore: " + e);
//                e.printStackTrace();
//            }
//        } else {
//            Log.d("LOGGER", "Restore - file does not exists");
//        }
//    }
//
//    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
//        FileChannel fromChannel = null;
//        FileChannel toChannel = null;
//        try {
//            fromChannel = fromFile.getChannel();
//            toChannel = toFile.getChannel();
//            fromChannel.transferTo(0, fromChannel.size(), toChannel);
//        } finally {
//            try {
//                if (fromChannel != null) {
//                    fromChannel.close();
//                }
//            } finally {
//                if (toChannel != null) {
//                    toChannel.close();
//                }
//            }
//        }
//    }

//    private boolean validateDB() {
//        //One - DB might be corrupt with some wrong data flow.
//        //Two - Restoring using a bad file (Possibly, not a DB file).
//        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
//        if (sharedPreferences.getBoolean("restoringDatabase", false)) {
//            sharedPreferences.edit().putBoolean("restoringDatabase", false).apply();
//            //Check if restore has been done properly and delete the backupCheckpoint.
//            if (appDatabase.userDao().getUserCount() <= 0) {
//                //reset the file and delete the restoredFile. Alert the user of the same.
//                if (restoreDatabase()) {
//                    //File successfully restored. Delete the backup file.
//                    restoreSuccessDialog();
//                    deleteRestoreBackupFile(getApplicationContext());
//                } else {
//                    //File probably deleted or some other issue. Alert here with flushing DB.
//                    flushDB();
//                    resetScreen();
//                    restoreFailureDialog();
//                }
//                return false;
//            }
//        } else {
//            if (appDatabase.userDao().getUserCount() <= 0) {
//                //Alert here that no user so DB flushed.
//                //Flush DB, take user to Name screen, delete restore File if present and create a new one.
//                flushDB();
//                resetScreen();
//                deleteRestoreBackupFile(getApplicationContext());
//                backupDatabaseForRestore(this, getApplicationContext());
//                dbIssueDialog();
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public static void deleteRestoreBackupFile(Context context) {
//        File directory = new File(getFilePath(context, 0), "backup");
//        String sfpath = directory.getPath() + File.separator + BACKUP_RESTORE_ROLLBACK_FILE_NAME;
//        //This is to prevent deleting extra file being deleted which is mentioned in previous comment lines.
//        File restoreFile = new File(sfpath);
//
//        if (restoreFile.exists()) {
//            restoreFile.delete();
//        }
//    }
//



}
