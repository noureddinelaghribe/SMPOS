package com.noureddine.stockmanagment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActiveLicenseActivity extends AppCompatActivity {

    Button verify;
    EditText editText1,editText2,editText3,editText4,editText5,editText6;
    String code;
    SharedPreferences sharedPreferences;
    private static final String MYKEY = "TrialPrefs";
    TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_active_license);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences(MYKEY,0);

        verify = findViewById(R.id.button_verify);
        editText1 = findViewById(R.id.editTextText2);
        editText2 = findViewById(R.id.editTextText3);
        editText3 = findViewById(R.id.editTextText4);
        editText4 = findViewById(R.id.editTextText5);
        editText5 = findViewById(R.id.editTextText6);
        editText6 = findViewById(R.id.editTextText7);
        textView = findViewById(R.id.textView10);

        String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        textView.setText(   "الرجاء ادخال رمز تفعيل البرنامج :  "+"\n"+androidId);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+
                        editText4.getText().toString()+editText5.getText().toString()+editText6.getText().toString();
                if (code.length()==6){
                    if (code.equals(sharedPreferences.getString("key",null))) {
                        Toast.makeText(ActiveLicenseActivity.this, "تم تفعيل البرنامج", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("active",true);
                        editor.apply();
                        Intent intent = new Intent(ActiveLicenseActivity.this , MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });








    }
}