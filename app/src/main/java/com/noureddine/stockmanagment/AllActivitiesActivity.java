package com.noureddine.stockmanagment;

import static com.noureddine.stockmanagment.Opiration.longToDate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Controlar.AdapterReport;

public class AllActivitiesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterReport adapterReport;
    SMViewModel smViewModel;
    TextView  button_to , button_from ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_activities);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView = findViewById(R.id.recyclerView);

        smViewModel = new ViewModelProvider(this).get(SMViewModel.class);

        adapterReport = new AdapterReport();
        recyclerView.setAdapter(adapterReport);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();

        button_from.setText(longToDate(extras.getLong("timeStare")));
        button_to.setText(longToDate(extras.getLong("timeEnd")));



    }
}