package com.noureddine.stockmanagment;

import static com.noureddine.stockmanagment.Opiration.hideLoading;
import static com.noureddine.stockmanagment.Opiration.updateTextLoading;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileNotFoundException;

public class Share {

    private static final String SERVICE_ID = "com.example.myapp";
    private ConnectionsClient connectionsClient;
    private Context context;
    private String connectedEndpointId;
    private String deviceName = Build.MODEL;

    public Share(ConnectionsClient connectionsClient, Context context) {
        this.connectionsClient = connectionsClient;
        this.context = context;
    }

    public void startDiscovery() {

        SMViewModel smViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SMViewModel.class);
        smViewModel.backupDatabase();

        connectionsClient.startDiscovery(
                SERVICE_ID,
                endpointDiscoveryCallback,
                new com.google.android.gms.nearby.connection.DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
        ).addOnSuccessListener(unused -> {
            Opiration.loading(context,"يتم البحث عن الاجهزة القريبة");
            Log.d("NearbyStockManagment", "Discovery started successfully");
            //Toast.makeText(this, "Discovery started", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "هنالك مشكلة في العثور على الاجهزو القريبة اعد المحاولة", Toast.LENGTH_SHORT).show();
            hideLoading();
            connectionsClient.stopDiscovery();
            Log.e("NearbyStockManagment", "Discovery failed", e);
            //Toast.makeText(this, "Discovery failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
            updateTextLoading("تم العثور على : " + info.getEndpointName());

            Log.d("NearbyStockManagment", "Endpoint found: " + endpointId+" "+info.getEndpointName());
            //Toast.makeText(SettingsActivity.this, "Device found: " + info.getEndpointName(), Toast.LENGTH_SHORT).show();

            // Request connection to the found endpoint
            connectionsClient.requestConnection(
                            deviceName, // Local device name
                            endpointId,
                            connectionLifecycleCallbackDiscovery
                    ).addOnSuccessListener(unused -> Log.d("NearbyStockManagment", "Connection request sent"))
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "هنالك مشكلة في الاتصال بالاجهزة القريبة اعد المحاولة", Toast.LENGTH_SHORT).show();
                            hideLoading();
                            connectionsClient.stopDiscovery();
                        }
                    });
        }

        @Override
        public void onEndpointLost(String endpointId) {
            Toast.makeText(context, "هنالك مشكلة لقد تم فقد الاتصال اعد المحاولة", Toast.LENGTH_SHORT).show();
            hideLoading();
            connectionsClient.stopDiscovery();

            Log.d("NearbyStockManagment", "Endpoint lost: " + endpointId);
            //Toast.makeText(SettingsActivity.this, "Device lost: " + endpointId, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Callback for managing connection lifecycle events.
     */
    private final ConnectionLifecycleCallback connectionLifecycleCallbackDiscovery = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(String endpointId, com.google.android.gms.nearby.connection.ConnectionInfo connectionInfo) {
            Log.d("NearbyStockManagment", "Connection initiated with endpoint: " + endpointId);
            connectionsClient.acceptConnection(endpointId, payloadCallbackDiscovery);
        }

        @Override
        public void onConnectionResult(String endpointId, ConnectionResolution result) {
            if (result.getStatus().getStatusCode() == ConnectionsStatusCodes.STATUS_OK) {
                connectedEndpointId = endpointId;
                Log.d("NearbyStockManagment", "Connected to endpoint: " + endpointId);

                updateTextLoading("تم الاتصال ");
                updateTextLoading("يتم ارسال البيانات الان الرجاء الانتضار...");

                //Toast.makeText(SettingsActivity.this, "Connected to device", Toast.LENGTH_SHORT).show();

//                File filePath = new File("/storage/emulated/0/Android/data/com.example.stockmanagment/files/SMDatabase_Backups/SMDatabase_backup.db");

                // Define the target directory and file
                File targetDir = new File(context.getExternalFilesDir(null), "SMDatabase_Backups");
                if (!targetDir.exists()) {
                    //Toast.makeText(SettingsActivity.this, "لا توجد بيانات لمشاركتها", Toast.LENGTH_SHORT).show();
                }


                File targetFile = new File(targetDir, "SMDatabase_backup.db");
                sendFile(targetFile);


            } else {
                Log.e("NearbyStockManagment", "Connection failed with endpoint: " + endpointId);
                //Toast.makeText(SettingsActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "هنالك مشكلة اعد المحاولة", Toast.LENGTH_SHORT).show();
                hideLoading();
                connectionsClient.stopDiscovery();
            }
        }

        @Override
        public void onDisconnected(String endpointId) {
            Log.d("NearbyStockManagment", "Disconnected from endpoint: " + endpointId);
            //Toast.makeText(SettingsActivity.this, "Disconnected from device", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "تم الغاء الاتصال ", Toast.LENGTH_SHORT).show();
            hideLoading();
            connectionsClient.stopDiscovery();
//            connectionsClient.stopAllEndpoints();

        }
    };

    /**
     * Callback for handling payload transfers.
     */
    private final PayloadCallback payloadCallbackDiscovery = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String endpointId, Payload payload) {
            if (payload.getType() == Payload.Type.BYTES) {
                String receivedMessage = new String(payload.asBytes());
                Log.d("NearbyStockManagment", "Payload received: " + receivedMessage);
                //Toast.makeText(SettingsActivity.this, "Received: " + receivedMessage, Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
            // Handle payload transfer updates if necessary
        }
    };

    private void sendFile(File fileToSend) {

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            if (fileToSend.exists() && fileToSend.canRead()) {
                Payload filePayload = null;
                try {
                    filePayload = Payload.fromFile(fileToSend);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                connectionsClient.sendPayload(connectedEndpointId, filePayload)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "تم ارسال الملق بنجاح.", Toast.LENGTH_SHORT).show();
                                hideLoading();
                                connectionsClient.stopDiscovery();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "هنالك مشكلة اعد المحاولة", Toast.LENGTH_SHORT).show();
                                hideLoading();
                                connectionsClient.stopDiscovery();
                            }
                        });
            } else {
                Log.e("NearbyStockManagment", "File not found or cannot be read");
            }

        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1009);
        }

    }
}
