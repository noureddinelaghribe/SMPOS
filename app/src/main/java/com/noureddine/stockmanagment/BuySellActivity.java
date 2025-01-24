package com.noureddine.stockmanagment;

import static com.noureddine.stockmanagment.ImageUtils.uriToBitmap;
import static com.noureddine.stockmanagment.Opiration.checkout;
import static com.noureddine.stockmanagment.Opiration.editProduct;
import static com.noureddine.stockmanagment.Opiration.editSupplierCustomer;
import static com.noureddine.stockmanagment.Opiration.startScanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

import Controlar.AdapterProductCart;
import Controlar.OnCPClickListener;
import model.Customer;
import model.Product;
import model.ProductCart;
import model.Supplier;

public class BuySellActivity extends AppCompatActivity {

    Spinner spinner;
    int customerID = -1 ;
    int supplierID = -1 ;
    
    List<Supplier> listSupplier = new ArrayList<>();
    List<Customer> listCustomer = new ArrayList<>();
    List<String> listName = new ArrayList<>();

    ArrayAdapter adapter;
    ImageView scanBarCode;
    SMViewModel smViewModel;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapterRecyclerView;
    List<Product> products = new ArrayList<>();
    List<ProductCart> productCarts = new ArrayList<>();
    List<String> idsProducts = new ArrayList<>();
    FloatingActionButton fab;
    Bundle extras;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    int totalPrice=0;
    Opiration opiration;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buy_sell);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner = findViewById(R.id.spinner);
        scanBarCode = findViewById(R.id.imageView1);
        recyclerView = findViewById(R.id.RecyclerView);
        fab = findViewById(R.id.fab);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        smViewModel = new ViewModelProvider(this).get(SMViewModel.class);
        extras = getIntent().getExtras();
        opiration = new Opiration(this);

        if (extras.getString("type").equals("buy")){
            fab.setImageResource(R.drawable.checkin);
            listSupplier.clear();
            listName.clear();
            listName.add("اختر المورد");
            listName.add("اضافة مورد");
            smViewModel.getAllSuppliers().observe(BuySellActivity.this, new Observer<List<Supplier>>() {
                @Override
                public void onChanged(List<Supplier> suppliers) {
                    listSupplier.clear();
                    listName.clear();
                    listName.add("اختر المورد");
                    listName.add("اضافة مورد");
                    for (Supplier s : suppliers){
                        listSupplier.add(s);
                        listName.add(s.getName());
                    }
                }
            });
            adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listName);
        }else{
            fab.setImageResource(R.drawable.checkout);
            listCustomer.clear();
            listName.clear();
            listName.add("اختر العميل");
            listName.add("اضافة العميل");
            smViewModel.getAllCustomers().observe(BuySellActivity.this, new Observer<List<Customer>>() {
                @Override
                public void onChanged(List<Customer> customers) {
                    listCustomer.clear();
                    listName.clear();
                    listName.add("اختر العميل");
                    listName.add("اضافة العميل");
                    for (Customer c : customers){
                        listCustomer.add(c);
                        listName.add(c.getName());
                    }
                }
            });
            adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listName);
        }




        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (extras.getString("type").equals("buy")){
                    if (position>1){
                        if (!(listSupplier.isEmpty() || listSupplier == null)){
                            Toast.makeText(BuySellActivity.this, listSupplier.get(position-2).getName(), Toast.LENGTH_SHORT).show();
                            supplierID=listSupplier.get(position-2).getId();
                        }
                    }else {
                        if (position==1){
                            //add
                            editSupplierCustomer(
                                    BuySellActivity.this,
                                    "suppliers",
                                    new Supplier(),
                                    new Customer(),
                                    false
                            );
                        }
                    }
                }else {
                    if (position>1){
                        if (!(listCustomer.isEmpty() || listCustomer == null)){
                            Toast.makeText(BuySellActivity.this, listCustomer.get(position-2).getName(), Toast.LENGTH_SHORT).show();
                            customerID=listCustomer.get(position-2).getId();
                        }
                    }else {
                        if (position==1){
                            //add
                            editSupplierCustomer(
                                    BuySellActivity.this,
                                    "customers",
                                    new Supplier(),
                                    new Customer(),
                                    false
                            );
                        }
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        scanBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if the app has camera permission
                if (ContextCompat.checkSelfPermission(BuySellActivity.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission if not granted
                    ActivityCompat.requestPermissions(BuySellActivity.this, new String[]{android.Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    // Start the scanner activity if permission is granted
                    startScanner(0);
                }



            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (productCarts != null && !productCarts.isEmpty()){

                    if (extras.getString("type").equals("buy")){
                        if (supplierID == -1){
                            Toast.makeText(BuySellActivity.this, "الرجاء اختيار المورد", Toast.LENGTH_SHORT).show();
                        }else {

                            if (totalPrice!=0){
                                totalPrice=0;
                            }
                            for (ProductCart productCart : productCarts){
                                Log.d("onClick", "onClick: "+productCart.getIdp()+" "+productCart.getPrice()+" "+productCart.getQty());
                                totalPrice +=productCart.getPrice()*productCart.getQty();
                            }
                            // (List<ProductCart> productCarts,List<String> idsProducts,int supplierID,int customerID, String type, double totalPrice, Buy buy, Sell sell)
                            Log.d("onClick", "onClick: "+totalPrice);
                            checkout(productCarts,productCarts,supplierID,customerID,extras.getString("type"),totalPrice);

                        }
                    }else {
                        if (customerID == -1){
                            Toast.makeText(BuySellActivity.this, "الرجاء اختيار العميل", Toast.LENGTH_SHORT).show();
                        }else {

                            if (totalPrice!=0){
                                totalPrice=0;
                            }
                            for (ProductCart productCart : productCarts){
                                Log.d("onClick", "onClick: "+productCart.getIdp()+" "+productCart.getPrice()+" "+productCart.getQty());
                                totalPrice +=productCart.getPrice()*productCart.getQty();
                            }
                            //checkout(totalPrice,new Buy(),new Sell());
                            checkout(productCarts,productCarts,supplierID,customerID,extras.getString("type"),totalPrice);

                        }
                    }

                }else {
                    Toast.makeText(BuySellActivity.this, "لا توجد منتجات !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    // Handle the result of the barcode scan
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "requestCode "+requestCode, Toast.LENGTH_SHORT).show();
        if (resultCode == RESULT_OK) {

            String scanResult = data.getStringExtra("SCAN_RESULT");
            if (requestCode == 0){
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Product product = smViewModel.getProductByIdp(scanResult);
                        if (product != null) {
                            if (products.isEmpty() ) {
                                if (product.getQuantity()>0 || extras.getString("type").equals("buy")){
                                    products.add(product);
                                    if (extras.getString("type").equals("sell")){
                                        productCarts.add(new ProductCart(product.getId(),product.getIdp(),product.getPriceSell(),1));
                                    }else {
                                        productCarts.add(new ProductCart(product.getId(),product.getIdp(),product.getPriceBuy(),1));
                                    }
                                    idsProducts.add(String.valueOf(product.getIdp()));
                                }else {
                                    if (extras.getString("type").equals("sell")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(BuySellActivity.this, "لا توجد اي كمية من هدا المنتج", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                            } else {
                                boolean productExists = false;

                                for (Product p : products) {
                                    if (p.getIdp().equals(product.getIdp())) {
                                        productExists = true;
                                        break;
                                    }
                                }

                                if (!productExists) {
                                    if (product.getQuantity()>0 || extras.getString("type").equals("buy")){
                                        products.add(product); // Add only if the product doesn't already exist
                                        Log.d("onActivityResult", "onActivityResult: product not null 2");
                                        //productCarts.add(new ProductCart(product.getId(),product.getIdp(),product.getPriceBuy(),1));
                                        if (extras.getString("type").equals("sell")){
                                            productCarts.add(new ProductCart(product.getId(),product.getIdp(),product.getPriceSell(),1));
                                        }else {
                                            productCarts.add(new ProductCart(product.getId(),product.getIdp(),product.getPriceBuy(),1));
                                        }
                                        idsProducts.add(String.valueOf(product.getIdp()));
                                    }else {
                                        if (extras.getString("type").equals("sell")){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(BuySellActivity.this, "لا توجد اي كمية من هدا المنتج", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.d("onActivityResult", "onActivityResult: product null 2");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (extras.getString("type").equals("buy")){
                                        editProduct(new Product(), false, false, new Runnable() {
                                            @Override
                                            public void run() {

                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Product product = smViewModel.getProductByIdp(scanResult);
                                                        if (!(product ==null)) {
                                                            products.add(product);
                                                            Log.d("onActivityResult", "onActivityResult: product not null 3");
                                                            productCarts.add(new ProductCart(product.getId(),product.getIdp(),product.getPriceBuy(),1));
                                                            idsProducts.add(String.valueOf(product.getIdp()));
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    adapterRecyclerView.notifyDataSetChanged();
                                                                }
                                                            });

                                                        }else {
                                                            Log.d("onValidInput", "isEmpty()");
                                                        }

                                                    }
                                                }).start();

                                            }
                                        });
                                        opiration = new Opiration(scanResult);

                                    }else {
                                        Toast.makeText(BuySellActivity.this, "هدا المنتج غير متوغر غي المخزون", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    }
                }).start();







                new Thread(() -> {

                }).start();

                adapterRecyclerView = new AdapterProductCart(products,productCarts, extras.getString("type"), BuySellActivity.this, new OnCPClickListener() {
                    @Override
                    public void onItemCklick(List<ProductCart> productCarts) {
                        for (ProductCart productCart : productCarts){
                            totalPrice +=productCart.getPrice()*productCart.getQty();
                        }
                    }
                });
                recyclerView.setAdapter(adapterRecyclerView);
                adapterRecyclerView.notifyDataSetChanged();

            }else if(requestCode == 1) {
                opiration = new Opiration(scanResult);
            } else if (requestCode == 2){
                Uri imageUri = data.getData();
                Bitmap photo = uriToBitmap(BuySellActivity.this,imageUri);
                opiration = new Opiration(photo);
            }else if (requestCode == 3){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                opiration = new Opiration(photo);
            }

        }

    }


}