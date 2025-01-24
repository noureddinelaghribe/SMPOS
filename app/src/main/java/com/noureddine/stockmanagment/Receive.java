package com.noureddine.stockmanagment;

import static com.noureddine.stockmanagment.Opiration.hideLoading;
import static com.noureddine.stockmanagment.Opiration.updateTextLoading;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import database.Reposetry;

public class Receive {

    private static final String SERVICE_ID = "com.example.myapp";
    private ConnectionsClient connectionsClient;
    private Context context;
    private File receivedFile;
    private String deviceName = Build.MODEL;
    SMViewModel smViewModel ;

    public Receive(ConnectionsClient connectionsClient, Context context) {
        this.connectionsClient = connectionsClient;
        this.context = context;
    }

    public void startAdvertising() {
        connectionsClient.startAdvertising(
                deviceName, // Name of the device
                SERVICE_ID, // Unique service ID
                connectionLifecycleCallback, // Connection lifecycle callbacks
                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build() // Strategy
        ).addOnSuccessListener(unused -> {
            Opiration.loading(context,"يتم البحث عن الاجهزة القريبة");
            Log.d("NearbyStockManagment", "Advertising started successfully");
        }).addOnFailureListener(e -> {
            Log.e("NearbyStockManagment", "Advertising failed", e);
            Toast.makeText(context, "هنالك مشكلة اعد المحاولة", Toast.LENGTH_SHORT).show();
            hideLoading();
            connectionsClient.stopAdvertising();
        });
    }

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
            connectionsClient.acceptConnection(endpointId, payloadCallback);
        }

        @Override
        public void onConnectionResult(String endpointId, ConnectionResolution result) {
            if (result.getStatus().getStatusCode() == ConnectionsStatusCodes.STATUS_OK) {
                updateTextLoading("تم الاتصال ");
                Log.d("NearbyStockManagment", "Connection successful with endpoint: " + endpointId);
            } else {
                Toast.makeText(context, "هنالك مشكلة في الاتصال اعد المحاولة", Toast.LENGTH_SHORT).show();
                hideLoading();
                connectionsClient.stopAdvertising();
                Log.e("NearbyStockManagment", "Connection failed with endpoint: " + endpointId);
            }
        }

        @Override
        public void onDisconnected(String endpointId) {
            Log.d("NearbyStockManagment", "Disconnected from endpoint: " + endpointId);
            Toast.makeText(context, "تم الغاء الاتصال ", Toast.LENGTH_SHORT).show();
            hideLoading();
            connectionsClient.stopAdvertising();
        }
    };

    private final PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String endpointId, Payload payload) {

            if (payload.getType() == Payload.Type.FILE) {
                receivedFile = payload.asFile().asJavaFile();
                Log.d("NearbyStockManagment", "File received at: " + receivedFile.getAbsolutePath());
            } else {
                Log.d("NearbyStockManagment", "Received non-file payload.");
            }

        }

        @Override
        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
            // Handle progress updates for file transfers
            Log.d("NearbyStockManagment", "Transferred : " + update.getBytesTransferred());

            updateTextLoading("يتم نقل البيانات الى التطبيق :  "+update.getBytesTransferred()+" / "+update.getTotalBytes());

            if (update.getBytesTransferred() == update.getTotalBytes()){

                File sdir = new File(context.getExternalFilesDir(null), "SMDatabase_Backups");

                if (!sdir.exists()) {
                    boolean created = sdir.mkdirs();
                    if (created) {
                        renameFile(receivedFile);
                        moveFile();
                    }
                } else {
                    renameFile(receivedFile);
                    moveFile();
                }

                smViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SMViewModel.class);
                smViewModel.restoreDatabase(new Reposetry.RestoreCallback() {
                    @Override
                    public void onRestoreComplete(boolean success) {
                        if(success){
                            Toast.makeText(context, "تم استقبال الملق بنجاح.", Toast.LENGTH_SHORT).show();
                            hideLoading();
                            connectionsClient.stopAdvertising();

                            Intent intent = new Intent(context,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                            //System.exit(0);
                        }
                    }
                });

            }

        }
    };


    private void renameFile(File receivedFile){

        // Define the source file path
        File sourceFile = new File(receivedFile.getPath());

        // Define the destination file path with the new name (in the same directory)
        File destinationFile = new File(sourceFile.getParent(), "SMDatabase_backup.db");

        // Check if the source file exists
        if (!sourceFile.exists()) {
            Log.d("NearbyStockManagment", "Source file does not exist");
            return;
        }

        try {
            // Rename the file
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For API 26+, use Files.move()
                Files.move(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                // For older APIs, use renameTo()
                if (!sourceFile.renameTo(destinationFile)) {
                    throw new IOException("Failed to rename file");
                }
            }
            Log.d("NearbyStockManagment", "File renamed successfully to: " + destinationFile.getName());

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("NearbyStockManagment", "Failed to rename file: " + e.getMessage());

        }

    }



    private void moveFile(){

        // Define the source file path
        File fromSourceFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),".nearby/SMDatabase_backup.db");

        // Define the destination directory
        //File toDestinationDir = new File("/storage/emulated/0/SMDatabase_Backups");
        File toDestinationDir = new File(context.getExternalFilesDir(null), "SMDatabase_Backups");

        // Create the destination directory if it doesn't exist
        if (!toDestinationDir.exists()) {
            if (!toDestinationDir.mkdirs()) {
                Log.d("NearbyStockManagment", "Failed to create destination directory");
                return;
            }
        }

        // Define the destination file path
        File toDestinationFile = new File(toDestinationDir, fromSourceFile.getName());

        // Check if the source file exists
        if (!fromSourceFile.exists()) {
            Log.d("NearbyStockManagment", "Source file does not exist");
            return;
        }

        try {
            // Move the file
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For API 26+, use Files.move()
                Files.move(fromSourceFile.toPath(), toDestinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                // For older APIs, use renameTo()
                if (!fromSourceFile.renameTo(toDestinationFile)) {
                    throw new IOException("Failed to move file");
                }
            }
            Log.d("NearbyStockManagment", "File moved successfully to: " + toDestinationFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("NearbyStockManagment", "Failed to move file: " + e.getMessage());
        }

    }



}
