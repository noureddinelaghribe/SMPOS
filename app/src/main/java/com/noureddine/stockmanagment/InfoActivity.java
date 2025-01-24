package com.noureddine.stockmanagment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class InfoActivity extends AppCompatActivity {

    LinearLayout whatsapp,facebook;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        facebook = findViewById(R.id.linear_facebook);
        whatsapp = findViewById(R.id.linear_whatsapp);

        facebook.setOnClickListener(v -> {



        });

        whatsapp.setOnClickListener(v -> {

            try {
                String url = "https://wa.link/9gdb0o"; // The URL you want to share
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);

                // Specify WhatsApp package to directly open WhatsApp
                sendIntent.setPackage("com.whatsapp");

                // Start the intent
                startActivity(sendIntent);

            } catch (android.content.ActivityNotFoundException ex) {
                // Handle the case where WhatsApp is not installed
                Toast.makeText(this, "تطبيق الواتساب غير مثبت لديك", Toast.LENGTH_SHORT).show();
            }

        });


    }
}