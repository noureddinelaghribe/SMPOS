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
import com.noureddine.stockmanagment.R;

import java.util.List;
import Controlar.AdapterProduct;
import Controlar.OnItemClickListener;
import model.Product;
import model.Transactions;

public class StockActivity extends AppCompatActivity {

    FloatingActionButton fab;
    SMViewModel smViewModel;
    long data ;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    OnItemClickListener onItemClickListener;
    EditText editTextSearch;
    ImageView scanBarCode;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    //EditText barcode;
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



//                for (Product p : products){
//                    onItemClickListener = new OnItemClickListener() {
//                        @Override
//                        public void onItemCklick(Product product,String s) {
//                            Toast.makeText(StockActivity.this, " "+s, Toast.LENGTH_SHORT).show();
//                            if (s.equals("حذف")){
//                                //delete
//                                Toast.makeText(StockActivity.this, "تم حذف المنتج", Toast.LENGTH_SHORT).show();
//                            }else{
//                                editProduct(product,true);
//                                Toast.makeText(StockActivity.this, "edit", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
////                        @Override
////                        public void onItemCklick(int postion) {
////                            editProduct(products.get(postion));
////                        }
//                    };
//                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addProduct();
                //editProduct(new Product(),false);
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

//                        for (Product p : products){

//                            onItemClickListener = new OnItemClickListener() {
//                                @Override
//                                public void onItemCklick(Product product,String s) {
//                                    if (s.equals("حذف")){
//                                        //delete
//                                        Toast.makeText(StockActivity.this, "تم حذف المنتج", Toast.LENGTH_SHORT).show();
//                                    }else{
//                                        editProduct(product,true);
//                                        Toast.makeText(StockActivity.this, "edit", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
////                                public void onItemCklick(int postion) {
////                                    editProduct(products.get(postion));
////                                }
//
////                                @Override
////                                public void onItemCklick(int postion) {
////                                    editProduct(products.get(postion));
////                                }
//                            };

//                        }

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

    // Start the barcode scanner activity
//    private void startScanner(int i) {
//        Intent intent = new Intent(this, CaptureActivity.class);
//        startActivityForResult(intent, i);
//    }

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




//
//    @SuppressLint({"LocalSuppress", "MissingInflatedId"})
//    private void addProduct() {
//        // Inflate the custom layout
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.add, null);
//
//        // Get references to custom views in the dialog
//        ImageView catigory = dialogView.findViewById(R.id.imageView_Catigory);
//        ImageView imgProduct = dialogView.findViewById(R.id.imageView_product);
//        ImageView scanBarcode = dialogView.findViewById(R.id.imageView_scan_barcode);
//        ImageView close = dialogView.findViewById(R.id.imageView_close);
//
//        TextView titleTextView = dialogView.findViewById(R.id.textView_tital);
//        EditText name = dialogView.findViewById(R.id.editText_2);
//        EditText barcode = dialogView.findViewById(R.id.editText_1);
//        EditText quantity = dialogView.findViewById(R.id.editText_3);
//        EditText description = dialogView.findViewById(R.id.editText_4);
//        EditText pBuy = dialogView.findViewById(R.id.editText_5);
//        EditText pSell = dialogView.findViewById(R.id.editText_6);
//        EditText limit = dialogView.findViewById(R.id.editText_7);
//
//        Button pickDate = dialogView.findViewById(R.id.button_pick_date);
//        Button save = dialogView.findViewById(R.id.button_save);
//
//        titleTextView.setText("اضافة منتج");
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
//        pickDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Create the MaterialDatePicker with customizations
//                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
//                        .setTitleText("تاريخ انتهاء :") // Custom title in Arabic
//                        .setPositiveButtonText("اختيار")  // Custom positive button text
//                        .setNegativeButtonText("إلغاء")  // Custom negative button text
//                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Default to today's date
//                        .build();
//
//                // Show the date picker
//                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
//
//                // Handle date selection
//                datePicker.addOnPositiveButtonClickListener(selection -> {
//                    // Get the selected date and set it to the TextView
//                    String formattedDate = datePicker.getHeaderText();
//                    data=datePicker.getSelection();
//                    pickDate.setText(formattedDate);  // Display the selected date
//                });
//
//                // Optional: Handle negative button click (e.g., dismiss dialog)
//                datePicker.addOnNegativeButtonClickListener(dialog -> {
//                    // Handle cancellation
//                });
//
//            }
//        });
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                smViewModel.insertProduct(new Product(
//                        description.getText().toString(),
//                        Integer.parseInt(quantity.getText().toString()) /*int*/,
//                        Integer.parseInt(limit.getText().toString())/*int*/,
//                        data/*double*/,
//                        "",
//                        Double.parseDouble(pBuy.getText().toString())/*double*/,
//                        Double.parseDouble(pSell.getText().toString())/*double*/,
//                        Integer.parseInt(barcode.getText().toString()),
//                        name.getText().toString()));
//
//                Toast.makeText(StockActivity.this, "تم اضافة المنتج", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//
//            }
//        });
//
//        // Show the dialog
//        dialog.setCancelable(false);
//        dialog.show();
//    }

//    @SuppressLint({"LocalSuppress", "MissingInflatedId", "SetTextI18n"})
//    private void editProduct(Product product,Boolean edit) {
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.add, null);
//
//        ImageView catigory = dialogView.findViewById(R.id.imageView_Catigory);
//        ImageView imgProduct = dialogView.findViewById(R.id.imageView_product);
//        ImageView scanBarcode = dialogView.findViewById(R.id.imageView_scan_barcode);
//        ImageView close = dialogView.findViewById(R.id.imageView_close);
//
//        TextView titleTextView = dialogView.findViewById(R.id.textView_tital);
//        EditText name = dialogView.findViewById(R.id.editText_2);
//        EditText barcode = dialogView.findViewById(R.id.editText_1);
//        EditText quantity = dialogView.findViewById(R.id.editText_3);
//        EditText description = dialogView.findViewById(R.id.editText_4);
//        EditText pBuy = dialogView.findViewById(R.id.editText_5);
//        EditText pSell = dialogView.findViewById(R.id.editText_6);
//        EditText limit = dialogView.findViewById(R.id.editText_7);
//
//        Button pickDate = dialogView.findViewById(R.id.button_pick_date);
//        Button save = dialogView.findViewById(R.id.button_save_and_print);
//
//        if (edit){
//            titleTextView.setText("تحديث منتج");
//
//            barcode.setText(String.valueOf(product.getIdp()));
//            name.setText(product.getName());
//            description.setText(product.getInfo());
//            pBuy.setText(product.getPriceBuy().toString());
//            pSell.setText(product.getPriceSell().toString());
//            //quantity.setText(String.valueOf(product.getQuantity()));
//            quantity.setHint("اضف على الكمية المتوفرة : "+product.getQuantity());
//            limit.setText(String.valueOf(product.getLimit()));
//            data = product.getExpiryDate();
//            pickDate.setText(longToDate(data));
////            Log.d("TAG", "getExpiryDate : "+data);
//
//            if (product.getImagePath().equals("") || product.getImagePath().equals(null)){
//                File file =new File(product.getImagePath());
//                if (file.exists()){
//                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                    imgProduct.setImageBitmap(bitmap);
//                }
//            }
//        }else
//        {
//            titleTextView.setText("اضافة منتج");
//        }
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
//        pickDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Create the MaterialDatePicker with customizations
//                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
//                        .setTitleText("تاريخ انتهاء :") // Custom title in Arabic
//                        .setPositiveButtonText("اختيار")  // Custom positive button text
//                        .setNegativeButtonText("إلغاء")  // Custom negative button text
//                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Default to today's date
//                        .build();
//
//                // Show the date picker
//                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
//
//                // Handle date selection
//                datePicker.addOnPositiveButtonClickListener(selection -> {
//                    // Get the selected date and set it to the TextView
////                    String formattedDate = datePicker.getHeaderText();
//                    data=datePicker.getSelection();
//                    Log.d("TAG", "getExpiryDate : "+data);
//                    pickDate.setText(longToDate(data));  // Display the selected date
//                });
//
//                // Optional: Handle negative button click (e.g., dismiss dialog)
//                datePicker.addOnNegativeButtonClickListener(dialog -> {
//                    // Handle cancellation
//                });
//
//            }
//        });
//
//        scanBarcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Check if the app has camera permission
//                if (ContextCompat.checkSelfPermission(StockActivity.this, android.Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    // Request permission if not granted
//                    ActivityCompat.requestPermissions(StockActivity.this, new String[]{android.Manifest.permission.CAMERA},
//                            CAMERA_PERMISSION_REQUEST_CODE);
//                } else {
//                    // Start the scanner activity if permission is granted
//                    startScanner(1);
//                }
//
//            }
//        });
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//                    public void onClick(View v) {
//
//                        if (edit){
//                            if (barcode.getText().toString().isEmpty() || name.getText().toString().isEmpty() ||
//                                    pBuy.getText().toString().isEmpty() || pSell.getText().toString().isEmpty()){
//
//                                if(barcode.getText().toString().isEmpty()){
//                                    barcode.setError("الرجاء ادخال باركود او scan");
//                                }else{
//                                    if(name.getText().toString().isEmpty()){
//                                        name.setError("الرجاءادخال اسم المنتج");
//                                    }else{
//                                        if(pBuy.getText().toString().isEmpty()){
//                                            pBuy.setError("الرجاء ادخال سعر البيع");
//                                        }else{
//                                            if(pSell.getText().toString().isEmpty()){
//                                                pSell.setError("الرجاء ادخال سعر الشراء");
//                                            }else{
//                                                Toast.makeText(StockActivity.this, "الرجاء ادخال المعلومات المطلوبة", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    }
//                                }
//
//                            }else {
//
//                                smViewModel.updateProduct(new Product(
//                                        product.getId(),
//                                        barcode.getText().toString(),
//                                        name.getText().toString(),
//                                        description.getText().toString(),
//                                        Double.parseDouble(pBuy.getText().toString()),
//                                        Double.parseDouble(pSell.getText().toString()),
//                                        quantity.getText().toString().isEmpty() ? product.getQuantity() : Integer.parseInt(quantity.getText().toString())+product.getQuantity(),
//                                        Integer.parseInt(limit.getText().toString()),
//                                        data,
//                                        ""));
//
//                                Toast.makeText(StockActivity.this, "تم تحديث المنتج", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//
//                            }
//
//                        }else {
//
//                            if (barcode.getText().toString().isEmpty() || name.getText().toString().isEmpty() ||
//                                    pBuy.getText().toString().isEmpty() || pSell.getText().toString().isEmpty() ||
//                                    quantity.getText().toString().isEmpty() || data==0){
//
//                                if(barcode.getText().toString().isEmpty()){
//                                    barcode.setError("الرجاء ادخال باركود او scan");
//                                }else{
//                                    if(name.getText().toString().isEmpty()){
//                                        name.setError("الرجاءادخال اسم المنتج");
//                                    }else{
//                                        if(quantity.getText().toString().isEmpty()){
//                                            quantity.setError("الرجاء ادخال سعر البيع");
//                                        }else{
//                                            if(pBuy.getText().toString().isEmpty()){
//                                                pBuy.setError("الرجاء ادخال سعر الشراء");
//                                            }else{
//                                                if(pSell.getText().toString().isEmpty()){
//                                                    pSell.setError("الرجاء ادخال الكمية");
//                                                }else{
//                                                    if(data == 0){
//                                                        Toast.makeText(StockActivity.this, "الرجاء ادخال تاريخ انتهاء الصلاحية", Toast.LENGTH_SHORT).show();
//                                                    }else{
//                                                        Toast.makeText(StockActivity.this, "الرجاء ادخال المعلومات المطلوبة", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//
//                            }else {
//
//                                smViewModel.insertProduct(new Product(
//                                        description.getText().toString(),
//                                        Integer.parseInt(quantity.getText().toString()) /*int*/,
//                                        Integer.parseInt(limit.getText().toString().isEmpty() ? "0" : limit.getText().toString())/*int*/,
//                                        data/*long*/,
//                                        "",
//                                        Double.parseDouble(pBuy.getText().toString())/*double*/,
//                                        Double.parseDouble(pSell.getText().toString())/*double*/,
//                                        barcode.getText().toString(),
//                                        name.getText().toString()));
//
//                                Toast.makeText(StockActivity.this, "تم اضافة المنتج", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//
//                            }
//                        }
//
//            }
//        });
//
//        // Show the dialog
//        dialog.setCancelable(false);
//        dialog.show();
//    }

//    public static String longToDate(long date){
//        Date d = new Date(date);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        String formatterDate = simpleDateFormat.format(d);
//        return formatterDate;
//    }

}