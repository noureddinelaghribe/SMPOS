package com.noureddine.stockmanagment;

import static com.noureddine.stockmanagment.ImageUtils.deleteImage;
import static com.noureddine.stockmanagment.ImageUtils.uriToBitmap;

import static com.noureddine.stockmanagment.Opiration.editProduct;
import static com.noureddine.stockmanagment.Opiration.startScanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import Controlar.AdapterProduct;
import Controlar.OnItemClickListener;
import model.Product;
import model.Transactions;

public class StockActivity extends AppCompatActivity {

    FloatingActionButton fab;
    SMViewModel smViewModel;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    OnItemClickListener onItemClickListener;
    EditText editTextSearch;
    ImageView scanBarCode;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    Opiration opiration;
    TextView noResult;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stock);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.RecyclerView);
        editTextSearch = findViewById(R.id.editText_search);
        scanBarCode = findViewById(R.id.imageView1);
        noResult = findViewById(R.id.textView20);

        smViewModel = new ViewModelProvider(this).get(SMViewModel.class);
        opiration = new Opiration(this);

        noResult.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        smViewModel.getAllProduct().observe(StockActivity.this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {

                if (products.isEmpty()){

                    recyclerView.setVisibility(View.GONE);
                    noResult.setVisibility(View.VISIBLE);
                    noResult.setText("لا توجد منتجات في المخزون");

                }else{

                    noResult.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    adapter = new AdapterProduct(products, getBaseContext(), new OnItemClickListener() {
                        @Override
                        public void onItemCklick(Product product, String s) {
                            if (s.equals("حذف")){
                                //smViewModel.deleteProductById(product.getId());
                                product.setDeleted(true);
                                smViewModel.updateProduct(product);
                                smViewModel.inserttransaction(new Transactions(0,0,product.getId(),0,0,null,"deleteProduct",System.currentTimeMillis()));

                                deleteImage(StockActivity.this, String.valueOf(product.getId()));
                                Toast.makeText(StockActivity.this, "تم حذف المنتج", Toast.LENGTH_SHORT).show();
                            }else{
                                //editProduct(product,true);
                                //    (Context context, String type, Product product, Boolean edit)
                                editProduct(product,true,true,null);
                            }
                        }
                    });
                    recyclerView.setAdapter(adapter);

                }


            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProduct(new Product(),false,true,null);

            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                smViewModel.searchProduct(s.toString()).observe(StockActivity.this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {

                        if (products.isEmpty()){

                            recyclerView.setVisibility(View.GONE);
                            noResult.setVisibility(View.VISIBLE);
                            noResult.setText("هدا المنتج غير متوفر");

                        }else{

                            noResult.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            adapter = new AdapterProduct(products, getBaseContext(), new OnItemClickListener() {
                                @Override
                                public void onItemCklick(Product product, String txt) {
                                    //Log.d("onItemCklick", "onItemCklick: "+txt);
                                    if (s.equals("حذف")){
                                        //smViewModel.deleteProductById(product.getId());
                                        product.setDeleted(true);
                                        smViewModel.updateProduct(product);
                                        smViewModel.inserttransaction(new Transactions(0,0,product.getId(),0,0,null,"deleteProduct",System.currentTimeMillis()));
                                        Toast.makeText(StockActivity.this, "تم حذف المنتج", Toast.LENGTH_SHORT).show();
                                    }else{
                                        //editProduct(product,true);
                                        editProduct(product,true,true,null);
                                    }
                                }
                            });
                            recyclerView.setAdapter(adapter);

                        }

                    }
                });

            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        scanBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if the app has camera permission
                if (ContextCompat.checkSelfPermission(StockActivity.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission if not granted
                    ActivityCompat.requestPermissions(StockActivity.this, new String[]{android.Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    // Start the scanner activity if permission is granted
                    startScanner(0);
                }



            }
        });


    }



    // Handle the result of the barcode scan
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //Toast.makeText(this, requestCode+" "+resultCode, Toast.LENGTH_SHORT).show();
            String scanResult = data.getStringExtra("SCAN_RESULT");
            if (requestCode == 0){
                editTextSearch.setText(scanResult);
            }else if(requestCode == 1){
                //barcode.setText(scanResult);
                opiration = new Opiration(scanResult);
            } else if (requestCode == 2){
                Uri imageUri = data.getData();
                Bitmap photo = uriToBitmap(StockActivity.this,imageUri);
                opiration = new Opiration(photo);
            }else if (requestCode == 3){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                opiration = new Opiration(photo);
            }
        }
    }




}