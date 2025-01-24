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
import com.noureddine.stockmanagment.R;

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
    //TextView date;
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


    // Start the barcode scanner activity
//    private void startScanner(int i) {
//        Intent intent = new Intent(this, CaptureActivity.class);
//        startActivityForResult(intent, i);
//    }

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


//    public void addProductToCart(String scanResult){
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                Product product = smViewModel.getProductByIdp(scanResult);
//                if (product != null) {
//                    if (products.isEmpty()) {
//                        products.add(product);
//                        productCarts.add(new ProductCart(product.getId(),product.getIdp(),product.getPriceSell(),1));
//                        //Log.d("onActivityResult", "onActivityResult: "+product.getIdp()+" "+product.getIdp().length());
//                        idsProducts.add(String.valueOf(product.getIdp()));
//                    } else {
//                        boolean productExists = false;
//
//                        for (Product p : products) {
//                            if (p.getIdp().equals(product.getIdp())) {
//                                productExists = true;
//                                break;
//                            }
//                        }
//
//                        if (!productExists) {
//                            products.add(product); // Add only if the product doesn't already exist
//                            productCarts.add(new ProductCart(product.getId(),product.getIdp(),product.getPriceSell(),1));
//                            idsProducts.add(String.valueOf(product.getIdp()));
//                        }
//                    }
//                } else {
//                    //Log.d("TAGAddProduct", "onActivityResult: null");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (extras.getString("type").equals("buy")){
//                                editProduct(new Product(), false, false, new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        new Thread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Log.d("onValidInput", "User entered valid input. Proceeding...");
//
//                                                new Thread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//
//                                                        Product product = smViewModel.getProductByIdp(scanResult);
//                                                        if (!products.isEmpty()) {
//                                                            Toast.makeText(BuySellActivity.this, "true 2", Toast.LENGTH_SHORT).show();
//                                                            products.add(product);
//                                                            productCarts.add(new ProductCart(product.getId(),product.getIdp(),product.getPriceSell(),1));
//                                                            idsProducts.add(String.valueOf(product.getIdp()));
//                                                        }
//
//                                                    }
//                                                }).start();
//
//                                            }
//                                        }).start();
//                                    }
//                                });
//                                opiration = new Opiration(scanResult);
//
//                            }else {
//                                Toast.makeText(BuySellActivity.this, "هدا المنتج غير متوغر غي المخزون", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                }
//
//            }
//        }).start();
//
//    }


//    @SuppressLint({"LocalSuppress", "MissingInflatedId", "SetTextI18n"})
//    public void checkout(double totalPrice, Buy buy, Sell sell) {
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.check_in_out, null);
//
//        ImageView catigory = dialogView.findViewById(R.id.imageView_Catigory);
//        ImageView close = dialogView.findViewById(R.id.imageView_close);
//
//        TextView titleTextView = dialogView.findViewById(R.id.textView_tital);
//        TextView dataTime = dialogView.findViewById(R.id.TextView_data_time);
//        TextView textViewTotal = dialogView.findViewById(R.id.textView_total_price);
//        RadioGroup radioGroup = dialogView.findViewById(R.id.RadioGroup);
//        RadioButton radioButton1 = dialogView.findViewById(R.id.radioButton1);
//        RadioButton radioButton2 = dialogView.findViewById(R.id.radioButton2);
//
//        EditText hasPay = dialogView.findViewById(R.id.editText_has_pay);
//        EditText discount = dialogView.findViewById(R.id.editText_discount);
//        EditText note = dialogView.findViewById(R.id.editText_note);
//
//        Button save = dialogView.findViewById(R.id.button_save);
//        Button saveAndPrint = dialogView.findViewById(R.id.button_save_and_print);
//
//        if (extras.getString("type").equals("buy")){
//            catigory.setImageResource(R.drawable.checkin);
//            titleTextView.setText("بيع");
//        }else {
//            catigory.setImageResource(R.drawable.checkout);
//            titleTextView.setText("شراء");
//        }
//
//        textViewTotal.setText(String.valueOf(totalPrice));
//        long dateLong = System.currentTimeMillis();
//        dataTime.setText(longToDateTime(dateLong));
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(dialogView);
//        AlertDialog dialog = builder.create();;
//
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        final String[] typePayment = new String[1];
//        typePayment[0] = "";
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                if (checkedId == radioButton1.getId()){
//                    typePayment[0] ="debt";
//                }else if (checkedId == radioButton2.getId()){
//                    typePayment[0] ="cash";
//                }
//
//            }
//        });
//
//        hasPay.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence bhasPay, int hstart, int hcount, int hafter) {
//                if (hcount>=1){
//                    double hasPayPrice = Double.parseDouble(bhasPay.toString());
//                    textViewTotal.setText(String.valueOf(Double.parseDouble(textViewTotal.getText().toString())+hasPayPrice));
//                }
//            }
//            @Override
//            public void onTextChanged(CharSequence hasPay, int start, int before, int count) {
//                if (hasPay != null && !hasPay.equals("") && hasPay.length()>0){
//                    double hasPayPrice = Double.parseDouble(hasPay.toString());
//                    textViewTotal.setText(String.valueOf(Double.parseDouble(textViewTotal.getText().toString())-hasPayPrice));
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        discount.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence sdiscount, int dstart, int dcount, int dafter) {
//                if (dcount>=1){
//                    double discountPrice = Double.parseDouble(sdiscount.toString());
//                    textViewTotal.setText(String.valueOf(Double.parseDouble(textViewTotal.getText().toString())+discountPrice));
//                }
//            }
//            @Override
//            public void onTextChanged(CharSequence sdiscount, int start, int before, int count) {
//                if (sdiscount != null && !sdiscount.equals("") && sdiscount.length()>0){
//                    double discountPrice = Double.parseDouble(sdiscount.toString());
//                    textViewTotal.setText(String.valueOf(Double.parseDouble(textViewTotal.getText().toString())-discountPrice));
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(BuySellActivity.this,MainActivity.class);
//
//                if (extras.getString("type").equals("buy")){
//
//                    if ( typePayment[0].equals("") ){
//                        radioButton1.setError("");
//                        radioButton2.setError("");
//                    }else {
//
//                        new Thread(() -> {
//
//                            smViewModel.insertBuying(new Buy(
//                                    idsProducts,supplierID,totalPrice/*totalAmount*/,
//                                    discount.getText().toString().isEmpty() ? 0 : Double.parseDouble(discount.getText().toString())/*discountAmount*/,
//                                    hasPay.getText().toString().isEmpty() ? 0 : Double.parseDouble(hasPay.getText().toString())/*paymentAmount*/,
//                                    Double.parseDouble(textViewTotal.getText().toString())/*restAmount*/,
//                                    typePayment[0],
//                                    note.getText().toString(),
//                                    dateLong
//                            ));
//
//                            for (ProductCart productCart : productCarts){
//                                int qty = smViewModel.getProductByIdp(productCart.getIdp()).getQuantity() + productCart.getQty();
//                                smViewModel.updateQuantity(productCart.getId(),qty);
//                            }
//
//                        }).start();
//
//                        Toast.makeText(BuySellActivity.this, "تم حفظ الشراء", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//
//                        startActivity(intent);
//                        finish();
//
//                    }
//
//                }else {
//
//                    if ( typePayment[0].equals("") ){
//                        radioButton1.setError("");
//                        radioButton2.setError("");
//                    }else {
//
//                        new Thread(() -> {
//
//                            smViewModel.insertSelling(new Sell(
//                                    idsProducts,supplierID,totalPrice/*totalAmount*/,
//                                    discount.getText().toString().isEmpty() ? 0 : Double.parseDouble(discount.getText().toString())/*discountAmount*/,
//                                    hasPay.getText().toString().isEmpty() ? 0 : Double.parseDouble(hasPay.getText().toString())/*paymentAmount*/,
//                                    Double.parseDouble(textViewTotal.getText().toString())/*restAmount*/,
//                                    typePayment[0],
//                                    note.getText().toString(),
//                                    dateLong
//                            ));
//
//                            for (ProductCart productCart : productCarts){
//                                int qty = smViewModel.getProductByIdp(productCart.getIdp()).getQuantity() - productCart.getQty();
//                                smViewModel.updateQuantity(productCart.getId(),qty);
//                            }
//
//                        }).start();
//
//                        Toast.makeText(BuySellActivity.this, "تم حفظ البيع", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//
//                        startActivity(intent);
//                        finish();
//
//                    }
//
//                }
//
//            }
//
//        });
//
//        saveAndPrint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                /**/
//
//            }
//        });
//
//        // Show the dialog
//        dialog.setCancelable(false);
//        dialog.show();
//
//    }



//    public static String longToDate(long date){
//        Date d = new Date(date);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//        String formatterDate = simpleDateFormat.format(d);
//        return formatterDate;
//    }

}