package com.noureddine.stockmanagment;

import static com.noureddine.stockmanagment.ImageUtils.saveBitmapToUri;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.Bidi;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Buy;
import model.Customer;
import model.Product;
import model.ProductCart;
import model.Sell;
import model.Supplier;
import model.Transactions;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Opiration {

    static Context context;
    static EditText editTextbarcode;
    static ImageView imgProduct;
    static Bitmap bitmapImgProduct = null;
    static TextView textViewLoading;
    static AlertDialog dialogLoading;

    public Opiration(Context context) {
        this.context = context;
    }

    public Opiration(String barcode) {
        editTextbarcode.setText(barcode);
    }

    public Opiration(Bitmap img) {
        imgProduct.setImageBitmap(img);
        bitmapImgProduct = img;
    }

    public static void updateTextLoading(String text) {
        if (textViewLoading != null) {
            textViewLoading.setText(text);
        }
    }

    @SuppressLint({"LocalSuppress", "MissingInflatedId"})
    public static void editSupplierCustomer(Context context, String type, Supplier supplier, Customer customer, Boolean edit) {
        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.addcs, null);

        SMViewModel smViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SMViewModel.class);

        // Get references to custom views in the dialog
        ImageView catigory = dialogView.findViewById(R.id.imageView_Catigory);
        ImageView close = dialogView.findViewById(R.id.imageView_close);

        TextView titleTextView = dialogView.findViewById(R.id.textView_loading_tital);
        EditText name = dialogView.findViewById(R.id.editText_1);
        EditText address = dialogView.findViewById(R.id.editText_2);
        EditText phoneNumber = dialogView.findViewById(R.id.editText_3);
        EditText note = dialogView.findViewById(R.id.editText_4);
        EditText debet = dialogView.findViewById(R.id.editText_5);

        Button save = dialogView.findViewById(R.id.button_save_and_print);

        if (edit){
            if (type.equals("customers")){
                titleTextView.setText("تحديث عميل");
                name.setText(customer.getName());
                address.setText(customer.getAddress());
                phoneNumber.setText(customer.getPhoneNumber());
                note.setText(customer.getNote());
                debet.setText(String.valueOf(customer.getDebet()));
            }else {
                titleTextView.setText("تحديث مورد");
                name.setText(supplier.getName());
                address.setText(supplier.getAddress());
                phoneNumber.setText(supplier.getPhoneNumber());
                note.setText(supplier.getNote());
                debet.setText(String.valueOf(supplier.getCredit()));
            }
        }else{
            if (type.equals("customers")){
                titleTextView.setText("اضافة عميل");
            }else {
                titleTextView.setText("اضافة مورد");
            }
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();;

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit){

                    if (name.getText().toString().isEmpty()){
                        if (type.equals("customers")){
                            name.setError("الرجاء ادخال اسم العميل");
                        }else {
                            name.setError("الرجاء ادخال اسم المورد");
                        }
                    }else{

                        if (type.equals("customers")){
                            smViewModel.updateCustomer(new Customer(customer.getId(),
                                    name.getText().toString(),
                                    address.getText().toString().isEmpty() ? "" : customer.getAddress(),
                                    phoneNumber.getText().toString().isEmpty() ? "" : customer.getPhoneNumber(),
                                    note.getText().toString().isEmpty() ? "" :customer.getNote(),customer.getDateInsert(),
                                    debet.getText().toString().equals("0") || debet.getText().toString().equals("") ? 0: Integer.parseInt(debet.getText().toString())
//                                    debet.getText().toString().isEmpty() ? 0 : Integer.parseInt(debet.getText().toString())
                            ));
                            smViewModel.inserttransaction(new Transactions(customer.getId(),0,0,0,0,null,
                                    "updateCustomer",System.currentTimeMillis()));
                            Toast.makeText(context, "تم تحديث عميل", Toast.LENGTH_SHORT).show();
                        }else {
                            smViewModel.updateSupplier(new Supplier(supplier.getId(),
                                    name.getText().toString(),
                                    address.getText().toString().isEmpty() ? "" : supplier.getAddress(),
                                    phoneNumber.getText().toString().isEmpty() ? "" : supplier.getPhoneNumber(),
                                    note.getText().toString().isEmpty() ? "" : supplier.getNote(),supplier.getDateInsert(),
                                    debet.getText().toString().equals("0") || debet.getText().toString().equals("") ? 0: Integer.parseInt(debet.getText().toString())
//                                    debet.getText().toString().isEmpty() ? 0 : Integer.parseInt(debet.getText().toString())
                            ));
                            smViewModel.inserttransaction(new Transactions(0,supplier.getId(),0,0,0,null,
                                    "updateSupplier",System.currentTimeMillis()));
                            Toast.makeText(context, "تم تحديث مورد", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();

                    }

                }else {

                    if (name.getText().toString().isEmpty()){
                        if (type.equals("customers")){
                            name.setError("الرجاء ادخال اسم العميل");
                        }else {
                            name.setError("الرجاء ادخال اسم المورد");
                        }
                    }else {

                        if (type.equals("customers")){
                            smViewModel.insertCustomer(new Customer(
                                    name.getText().toString(),
                                    address.getText().toString(),
                                    phoneNumber.getText().toString(),
                                    note.getText().toString(),
                                    System.currentTimeMillis(),
                                    false,
                                    debet.getText().toString().isEmpty() ? 0 : Integer.parseInt(debet.getText().toString())
                            )).observe((LifecycleOwner) context, new Observer<Long>() {
                                @Override
                                public void onChanged(Long aLong) {
                                    smViewModel.inserttransaction(new Transactions(
                                            Integer.parseInt(String.valueOf(aLong)),0,0,0,0,null,"insertCustomer",System.currentTimeMillis()));
                                    Toast.makeText(context, "تم اضافة عميل", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else {
                            smViewModel.insertSupplier(new Supplier(
                                    name.getText().toString(),
                                    address.getText().toString(),
                                    phoneNumber.getText().toString(),
                                    note.getText().toString(),
                                    System.currentTimeMillis(),
                                    false,
                                    debet.getText().toString().isEmpty() ? 0 : Integer.parseInt(debet.getText().toString())
                            )).observe((LifecycleOwner) context, new Observer<Long>() {
                                @Override
                                public void onChanged(Long aLong) {
                                    smViewModel.inserttransaction(new Transactions(
                                            0,Integer.parseInt(String.valueOf(aLong)),0,0,0,null,"insertSupplier",System.currentTimeMillis()));
                                    Toast.makeText(context, "تم اضافة مورد", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        dialog.dismiss();

                    }
                }





            }
        });

        // Show the dialog
        dialog.setCancelable(false);
        dialog.show();
    }



    @SuppressLint({"LocalSuppress", "MissingInflatedId", "SetTextI18n"})
    public static void editProduct(Product product, boolean edit, boolean editQuantity,Runnable onValidInput) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.add, null);

        SMViewModel smViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SMViewModel.class);
        final long[] data = new long[1];
        data[0] = 0;
        final int CAMERA_PERMISSION_REQUEST_CODE = 1;

        ImageView catigory = dialogView.findViewById(R.id.imageView_Catigory);
        imgProduct = dialogView.findViewById(R.id.imageView_product);
        ImageView scanBarcode = dialogView.findViewById(R.id.imageView_scan_barcode);
        ImageView close = dialogView.findViewById(R.id.imageView_close);

        TextView titleTextView = dialogView.findViewById(R.id.textView_loading_tital);
        EditText name = dialogView.findViewById(R.id.editText_2);
        editTextbarcode = dialogView.findViewById(R.id.editText_1);
        EditText quantity = dialogView.findViewById(R.id.editText_3);
        EditText description = dialogView.findViewById(R.id.editText_4);
        EditText pSell = dialogView.findViewById(R.id.editText_5);
        EditText pBuy = dialogView.findViewById(R.id.editText_6);
        EditText limit = dialogView.findViewById(R.id.editText_7);

        Button pickDate = dialogView.findViewById(R.id.button_pick_date);
        Button save = dialogView.findViewById(R.id.button_save_and_print);

        if (!editQuantity){
            quantity.setText("0");
            quantity.setEnabled(false);
            quantity.setFocusable(false);
            quantity.setFocusableInTouchMode(false);
            quantity.setClickable(false);

        }

        if (edit){
            titleTextView.setText("تحديث منتج");

            editTextbarcode.setText(String.valueOf(product.getIdp()));
            name.setText(product.getName());
            description.setText(product.getInfo());
            pBuy.setText(String.valueOf(product.getPriceBuy()));
            pSell.setText(String.valueOf(product.getPriceSell()));
            quantity.setHint("اضف على الكمية المتوفرة : "+product.getQuantity());
            limit.setText(String.valueOf(product.getLimit()));
            data[0] = product.getExpiryDate();
            pickDate.setText(data[0]>0 ? longToDate(data[0]) : "--/--/----");

            if (product.getImagePath().isEmpty() && bitmapImgProduct == null) {
                imgProduct.setImageResource(R.drawable.add_image);
            } else {
                File pathImgProduct = new File(product.getImagePath());
                if (pathImgProduct.exists()) {
                    imgProduct.setImageURI(Uri.parse(product.getImagePath()));
                }else {
                    imgProduct.setImageResource(R.drawable.add_image);
                }
            }

        }else
        {
            titleTextView.setText("اضافة منتج");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the MaterialDatePicker with customizations
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("تاريخ انتهاء :") // Custom title in Arabic
                        .setPositiveButtonText("اختيار")  // Custom positive button text
                        .setNegativeButtonText("إلغاء")  // Custom negative button text
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Default to today's date
                        .build();

                // Show the date picker
                AppCompatActivity appCompatActivity = (AppCompatActivity) context;
                datePicker.show(appCompatActivity.getSupportFragmentManager(), "DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    // Get the selected date and set it to the TextView
//                    String formattedDate = datePicker.getHeaderText();
                    data[0] =datePicker.getSelection();
                    Log.d("TAG", "getExpiryDate : "+ data[0]);
                    pickDate.setText(longToDate(data[0]));  // Display the selected date
                });

                // Optional: Handle negative button click (e.g., dismiss dialog)
                datePicker.addOnNegativeButtonClickListener(dialog -> {
                    // Handle cancellation
                });

            }
        });

        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(context);
            }
        });

        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if the app has camera permission
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request permission if not granted
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    // Start the scanner activity if permission is granted
                    startScanner(1);
                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit){
                    if (editTextbarcode.getText().toString().isEmpty() || name.getText().toString().isEmpty() ||
                            pBuy.getText().toString().isEmpty() || pSell.getText().toString().isEmpty()){

                        if(editTextbarcode.getText().toString().isEmpty()){
                            editTextbarcode.setError("الرجاء ادخال باركود او scan");
                        }else{
                            if(name.getText().toString().isEmpty()){
                                name.setError("الرجاءادخال اسم المنتج");
                            }else{
                                if(pBuy.getText().toString().isEmpty()){
                                    pBuy.setError("الرجاء ادخال سعر البيع");
                                }else{
                                    if(pSell.getText().toString().isEmpty()){
                                        pSell.setError("الرجاء ادخال سعر الشراء");
                                    }else{
                                        Toast.makeText(context, "الرجاء ادخال المعلومات المطلوبة", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    }else {

                        smViewModel.updateProduct(new Product(
                                product.getId(),
                                editTextbarcode.getText().toString(),
                                name.getText().toString(),
                                description.getText().toString(),
                                Integer.parseInt(pBuy.getText().toString()),
                                Integer.parseInt(pSell.getText().toString()),
                                quantity.getText().toString().isEmpty() ? product.getQuantity() : Integer.parseInt(quantity.getText().toString())+product.getQuantity(),
                                Integer.parseInt(limit.getText().toString()),
                                data[0],
                                product.getImagePath().isEmpty() && bitmapImgProduct == null ? "" : bitmapImgProduct != null ? String.valueOf(saveBitmapToUri(context,bitmapImgProduct,editTextbarcode.getText().toString())) : product.getImagePath(),
                                System.currentTimeMillis()
                        ));
                        //   int custmerId, int supplierId, int productId, int buyingId, int sellingId, String type, long timestamp

                        smViewModel.inserttransaction(new Transactions(0,0,product.getId(),0,0,
                                quantity.getText().toString().isEmpty() ? null : List.of(quantity.getText().toString()),
                                "updateProduct",System.currentTimeMillis()));

                        Toast.makeText(context, "تم تحديث المنتج", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        //result[0] = true;

                    }

                }else {

                    if (editTextbarcode.getText().toString().isEmpty() || name.getText().toString().isEmpty() ||
                            pBuy.getText().toString().isEmpty() || pSell.getText().toString().isEmpty() ||
                            quantity.getText().toString().isEmpty()){

                        if(editTextbarcode.getText().toString().isEmpty()){
                            editTextbarcode.setError("الرجاء ادخال باركود او scan");
                        }else{
                            if(name.getText().toString().isEmpty()){
                                name.setError("الرجاءادخال اسم المنتج");
                            }else{
                                if(pSell.getText().toString().isEmpty()){
                                    pSell.setError("الرجاء ادخال سعر البيع");
                                }else{
                                    if(pBuy.getText().toString().isEmpty()){
                                        pBuy.setError("الرجاء ادخال سعر الشراء");
                                    }else{
                                        if(quantity.getText().toString().isEmpty()){
                                            quantity.setError("الرجاء ادخال الكمية");
                                        }else{
                                            Toast.makeText(context, "الرجاء ادخال المعلومات المطلوبة", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        }

                    }else {

                        smViewModel.insertProduct(new Product(
                                description.getText().toString(),
                                Integer.parseInt(quantity.getText().toString()) /*int*/,
                                Integer.parseInt(limit.getText().toString().isEmpty() ? "0" : limit.getText().toString())/*int*/,
                                data[0]>0 ? data[0] : 0 ,
                                bitmapImgProduct == null ? "" : String.valueOf(saveBitmapToUri(context,bitmapImgProduct,editTextbarcode.getText().toString())),
                                Integer.parseInt(pBuy.getText().toString())/*double*/,
                                Integer.parseInt(pSell.getText().toString())/*double*/,
                                editTextbarcode.getText().toString(),
                                name.getText().toString(),System.currentTimeMillis(),false
                        )).observe((LifecycleOwner) context, new Observer<Long>() {
                            @Override
                            public void onChanged(Long aLong) {
                                smViewModel.inserttransaction(new Transactions(0,0,Integer.parseInt(String.valueOf(aLong)),0,0,List.of(quantity.getText().toString()),
                                        "insertProduct",System.currentTimeMillis()));
                                Toast.makeText(context, "تم اضافة المنتج", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                        if (!editQuantity){
                            if (onValidInput != null) {
                                onValidInput.run();
                            }
                        }

                    }

                }

            }
        });

    // Show the dialog
        dialog.setCancelable(false);
        dialog.show();
//        return result[0];
    }


    @SuppressLint({"LocalSuppress", "MissingInflatedId", "SetTextI18n"})
    public static void checkout(List<ProductCart> productCarts,List<ProductCart> idsProducts,int supplierID,int customerID, String type, int totalPrice) {

        LayoutInflater inflater = LayoutInflater.from(context);
        SMViewModel smViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SMViewModel.class);

        View dialogView = inflater.inflate(R.layout.check_in_out, null);

        ImageView catigory = dialogView.findViewById(R.id.imageView_Catigory);
        ImageView close = dialogView.findViewById(R.id.imageView_close);

        TextView titleTextView = dialogView.findViewById(R.id.textView_loading_tital);
        TextView dataTime = dialogView.findViewById(R.id.TextView_data_time);
        TextView textViewTotal = dialogView.findViewById(R.id.textView_total_price);
        RadioGroup radioGroup = dialogView.findViewById(R.id.RadioGroup);
        RadioButton radioButton1 = dialogView.findViewById(R.id.radioButton1);
        RadioButton radioButton2 = dialogView.findViewById(R.id.radioButton2);

        EditText hasPay = dialogView.findViewById(R.id.editText_has_pay);
        EditText discount = dialogView.findViewById(R.id.editText_discount);
        EditText note = dialogView.findViewById(R.id.editText_note);

        Button save = dialogView.findViewById(R.id.button_save);
        Button saveAndPrint = dialogView.findViewById(R.id.button_save_and_print);

        if (type.equals("buy")){
            catigory.setImageResource(R.drawable.checkin);
            titleTextView.setText("بيع");
        }else {
            catigory.setImageResource(R.drawable.checkout);
            titleTextView.setText("شراء");
        }

        textViewTotal.setText(String.valueOf(totalPrice));
        long dateLong = System.currentTimeMillis();
        dataTime.setText(longToDateTime(dateLong));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final String[] typePayment = new String[1];
        typePayment[0] = "";

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == radioButton1.getId()){
                    typePayment[0] ="debt";
                }else if (checkedId == radioButton2.getId()){
                    typePayment[0] ="cash";
                }

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity appCompatActivity = (AppCompatActivity) context;
                Intent intent = new Intent(context,MainActivity.class);

                int hp = hasPay.getText().toString().isEmpty() ? 0 : Integer.parseInt(hasPay.getText().toString());
                int d = discount.getText().toString().isEmpty() ? 0 : Integer.parseInt(discount.getText().toString());
                textViewTotal.setText(totalPrice-(hp+d));

                if (type.equals("buy")){

                    if ( typePayment[0].equals("") ){
                        radioButton1.setError("");
                        radioButton2.setError("");
                    }else {

                        List<String> idpList = new ArrayList<>();
                        for (ProductCart product : idsProducts) {
                            idpList.add(product.getIdp());
                        }

                        smViewModel.insertBuying(new Buy(
                                idpList,supplierID,totalPrice/*totalAmount*/,
                                discount.getText().toString().isEmpty() ? 0: Integer.parseInt(discount.getText().toString())/*discountAmount*/,
                                hasPay.getText().toString().isEmpty() ? 0: Integer.parseInt(hasPay.getText().toString())/*paymentAmount*/,
                                Integer.parseInt(textViewTotal.getText().toString())/*restAmount*/,
                                typePayment[0],
                                note.getText().toString(),System.currentTimeMillis()
                        )).observe((LifecycleOwner) context, new Observer<Long>() {
                            @Override
                            public void onChanged(Long aLong) {
                                //id this aLong buying
                                List<String> qtyList = new ArrayList<>();
                                for (ProductCart product : idsProducts) {
                                    qtyList.add(String.valueOf(product.getQty()));
                                }
                                smViewModel.inserttransaction(new Transactions(0,0,0,Integer.parseInt(String.valueOf(aLong)),0,qtyList,"insertBuying",System.currentTimeMillis()));
                            }
                        });


                        new Thread(() -> {
                            smViewModel.updateCreditSupplier(supplierID,Integer.parseInt(textViewTotal.getText().toString())+smViewModel.getSupplierById(supplierID).getCredit());

                            for (ProductCart productCart : productCarts){
                                int qty = smViewModel.getProductByIdp(productCart.getIdp()).getQuantity() + productCart.getQty();
                                smViewModel.updateQuantity(productCart.getId(),qty);
                            }

                        }).start();

                        Toast.makeText(context, "تم حفظ الشراء", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        appCompatActivity.startActivity(intent);
                        appCompatActivity.finish();

                    }

                }else {

                    if ( typePayment[0].equals("") ){
                        radioButton1.setError("");
                        radioButton2.setError("");
                    }else {

                        List<String> idpList = new ArrayList<>();
                        for (ProductCart product : idsProducts) {
                            idpList.add(product.getIdp());
                        }

                        smViewModel.insertSelling(new Sell(
                                idpList,customerID,totalPrice/*totalAmount*/,
                                discount.getText().toString().isEmpty() ? 0: Integer.parseInt(discount.getText().toString())/*discountAmount*/,
                                hasPay.getText().toString().isEmpty() ? 0: Integer.parseInt(hasPay.getText().toString())/*paymentAmount*/,
                                Integer.parseInt(textViewTotal.getText().toString())/*restAmount*/,
                                typePayment[0],
                                note.getText().toString(),System.currentTimeMillis()
                        )).observe((LifecycleOwner) context, new Observer<Long>() {
                            @Override
                            public void onChanged(Long aLong) {
                                //id this Selling aLong
                                List<String> qtyList = new ArrayList<>();
                                for (ProductCart product : idsProducts) {
                                    qtyList.add(String.valueOf(product.getQty()));
                                }
                                smViewModel.inserttransaction(new Transactions(0,0,0,0,Integer.parseInt(String.valueOf(aLong)),qtyList,"insertSelling",System.currentTimeMillis()));
                            }
                        });

                        new Thread(() -> {
                            smViewModel.updateDebetCustomer(customerID,Integer.parseInt(textViewTotal.getText().toString())+smViewModel.getCustomerById(customerID).getDebet());

                            for (ProductCart productCart : productCarts){
                                int qty = smViewModel.getProductByIdp(productCart.getIdp()).getQuantity() - productCart.getQty();
                                smViewModel.updateQuantity(productCart.getId(),qty);
                            }

                        }).start();

                        Toast.makeText(context, "تم حفظ البيع", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        appCompatActivity.startActivity(intent);
                        appCompatActivity.finish();

                    }

                }

            }

        });

        saveAndPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity appCompatActivity = (AppCompatActivity) context;
                Intent intent = new Intent(context,MainActivity.class);

                int hp = hasPay.getText().toString().isEmpty() ? 0 : Integer.parseInt(hasPay.getText().toString());
                int d = discount.getText().toString().isEmpty() ? 0 : Integer.parseInt(discount.getText().toString());
                textViewTotal.setText(String.valueOf(totalPrice-(hp+d)));

                if (type.equals("buy")){

                    if ( typePayment[0].equals("") ){
                        radioButton1.setError("");
                        radioButton2.setError("");
                    }else {

                        List<String> idpList = new ArrayList<>();
                        for (ProductCart product : idsProducts) {
                            idpList.add(product.getIdp());
                        }

                        Buy buy = new Buy(idpList,supplierID,totalPrice/*totalAmount*/,
                                discount.getText().toString().isEmpty() ? 0: Integer.parseInt(discount.getText().toString())/*discountAmount*/,
                                hasPay.getText().toString().isEmpty() ? 0: Integer.parseInt(hasPay.getText().toString())/*paymentAmount*/,
                                Integer.parseInt(textViewTotal.getText().toString())/*restAmount*/,
                                typePayment[0],
                                note.getText().toString(),System.currentTimeMillis());
                        smViewModel.insertBuying(buy).observe((LifecycleOwner) context, new Observer<Long>() {
                            @Override
                            public void onChanged(Long aLong) {
                                //id this aLong buying
                                List<String> qtyList = new ArrayList<>();
                                for (ProductCart product : idsProducts) {
                                    qtyList.add(String.valueOf(product.getQty()));
                                }
                                smViewModel.inserttransaction(new Transactions(0,0,0,Integer.parseInt(String.valueOf(aLong)),0,qtyList,"insertBuying",System.currentTimeMillis()));
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        smViewModel.updateCreditSupplier(supplierID,Integer.parseInt(textViewTotal.getText().toString())+smViewModel.getSupplierById(supplierID).getCredit());

                                        List<String> idpList = new ArrayList<>();
                                        for (ProductCart product : idsProducts) {
                                            idpList.add(product.getIdp());
                                        }

                                        List<Product> productList = new ArrayList<>();
                                        for (String idpProduct : idpList) {
                                            productList.add(smViewModel.getProductByIdp(idpProduct));
                                        }
                                        creatShowpdf(smViewModel,context,new Sell(),buy,productCarts,productList,aLong);
                                    }
                                }).start();
                            }
                        });

                        new Thread(() -> {

                            for (ProductCart productCart : productCarts){
                                int qty = smViewModel.getProductByIdp(productCart.getIdp()).getQuantity() + productCart.getQty();
                                smViewModel.updateQuantity(productCart.getId(),qty);
                            }

                        }).start();

                        Toast.makeText(context, "تم حفظ الشراء", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        appCompatActivity.startActivity(intent);
                        appCompatActivity.finish();

                    }

                }else {

                    if ( typePayment[0].equals("") ){
                        radioButton1.setError("");
                        radioButton2.setError("");
                    }else {

                        List<String> idpList = new ArrayList<>();
                        for (ProductCart product : idsProducts) {
                            idpList.add(product.getIdp());
                        }

                        Sell sell = new Sell(idpList,customerID,totalPrice/*totalAmount*/,
                                discount.getText().toString().isEmpty() ? 0: Integer.parseInt(discount.getText().toString())/*discountAmount*/,
                                hasPay.getText().toString().isEmpty() ? 0: Integer.parseInt(hasPay.getText().toString())/*paymentAmount*/,
                                Integer.parseInt(textViewTotal.getText().toString())/*restAmount*/,
                                typePayment[0],
                                note.getText().toString(),System.currentTimeMillis());

                        smViewModel.insertSelling(sell).observe((LifecycleOwner) context, new Observer<Long>() {
                            @Override
                            public void onChanged(Long aLong) {
                                //id this Selling aLong
                                List<String> qtyList = new ArrayList<>();
                                for (ProductCart product : idsProducts) {
                                    qtyList.add(String.valueOf(product.getQty()));
                                }
                                smViewModel.inserttransaction(new Transactions(0,0,0,0,Integer.parseInt(String.valueOf(aLong)),qtyList,"insertSelling",System.currentTimeMillis()));

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        smViewModel.updateDebetCustomer(customerID,Integer.parseInt(textViewTotal.getText().toString())+smViewModel.getCustomerById(customerID).getDebet());

                                        List<String> idpList = new ArrayList<>();
                                        for (ProductCart product : idsProducts) {
                                            idpList.add(product.getIdp());
                                        }

                                        List<Product> productList = new ArrayList<>();
                                        for (String idpProduct : idpList) {
                                            productList.add(smViewModel.getProductByIdp(idpProduct));
                                        }
                                        creatShowpdf(smViewModel,context,sell,new Buy(),productCarts,productList,aLong);
                                    }
                                }).start();
                            }
                        });

                        new Thread(() -> {

                            for (ProductCart productCart : productCarts){
                                int qty = smViewModel.getProductByIdp(productCart.getIdp()).getQuantity() - productCart.getQty();
                                smViewModel.updateQuantity(productCart.getId(),qty);
                            }

                        }).start();

                        Toast.makeText(context, "تم حفظ البيع", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        appCompatActivity.startActivity(intent);
                        appCompatActivity.finish();

                    }

                }



            }
        });

        // Show the dialog
        dialog.setCancelable(false);
        dialog.show();

    }


    public static void loading (Context context, String text){

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.loading, null);
        textViewLoading = dialogView.findViewById(R.id.textView_loading_tital);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        dialogLoading = builder.create();

        textViewLoading.setText(text);

        dialogLoading.setCancelable(false);
        dialogLoading.show();

    }

    public static void hideLoading() {
        if (dialogLoading != null && dialogLoading.isShowing()) {
            dialogLoading.dismiss();
            dialogLoading = null; // Reset the dialog reference
        }
    }



    public static void startScanner(int i) {
        Intent intent = new Intent(context, CaptureActivity.class);
        ((Activity) context).startActivityForResult(intent, i);
    }


    public static String longToDate(long date){
        Date d = new Date(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formatterDate = simpleDateFormat.format(d);
        return formatterDate;
    }

    public static String longToDateTime(long date){
        Date d = new Date(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formatterDate = simpleDateFormat.format(d);
        return formatterDate;
    }

    @SuppressLint("MissingInflatedId")
    public static void showBottomSheetDialog(Context context) {

        // Create the BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        // Inflate the layout
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog, null);

        // Find views by ID
        TextView gallery = bottomSheetView.findViewById(R.id.textViewFromGallery);
        TextView camera = bottomSheetView.findViewById(R.id.textViewFromCamera);

        // Set click listeners
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                ((Activity) context).startActivityForResult(intent, 2);
                bottomSheetDialog.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                ((Activity) context).startActivityForResult(intent, 3);
                bottomSheetDialog.dismiss();
            }
        });

        // Set the view for the BottomSheetDialog
        bottomSheetDialog.setContentView(bottomSheetView);

        // Show the dialog
        bottomSheetDialog.show();
    }

    private static void creatShowpdf(SMViewModel smViewModel , Context context , Sell sell , Buy buy , List<ProductCart> productCarts , List<Product> productList , long id){

        String type;
        final String[] name = new String[1];
        String discountAmount;
        String totalAmount;
        String paymentAmount;
        String restAmount;
        if (sell.getCustomerID() != 0){
            type = "مبيعات";

            name[0] = smViewModel.getCustomerById(sell.getCustomerID()).getName();

            discountAmount = String.valueOf(sell.getDiscountAmount());
            totalAmount = String.valueOf(sell.getTotalAmount());
            paymentAmount = String.valueOf(sell.getPaymentAmount());
            restAmount = String.valueOf(sell.getRestAmount());
        }else{
            type = "مشتريات";

            name[0] = smViewModel.getSupplierById(buy.getSupplierID()).getName();

            discountAmount = String.valueOf(buy.getDiscountAmount());
            totalAmount = String.valueOf(buy.getTotalAmount());
            paymentAmount = String.valueOf(buy.getPaymentAmount());
            restAmount = String.valueOf(buy.getRestAmount());
        }
        String date = longToDateTime(System.currentTimeMillis());

        // Define the directory and file name
        File path = new File(Environment.getExternalStorageDirectory(), "SMPOS/SMReports");

        // Create directory if it doesn't exist
        if (!path.exists()) {
            path.mkdirs();
        }

        String fileName = "report_" + date.replace(":", "_").replace("/", "_").replace(" ", "_") + "_.pdf";

        File file = new File(path,fileName);
        Log.d("TAG", "creatShowpdf: "+path.getPath());
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            BaseFont baseFont = BaseFont.createFont("assets/fonts/NotoSansArabic-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 12, Font.NORMAL);

            // إضافة معلومات العميل والتاريخ
            Paragraph clientInfo = new Paragraph(processBidirectionalText("فاتورة"+" : "+type),  new Font(baseFont, 18, Font.BOLD));
            clientInfo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(clientInfo);

            Paragraph newLine = new Paragraph(processBidirectionalText("\n\n"), font);
            newLine.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(newLine);

            // إضافة رقم الكاشير
            Paragraph nomB = new Paragraph(processBidirectionalText("رقم الفاتورة : "+id), font);
            nomB.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(nomB);

            // إضافة رقم الكاشير
            Paragraph caisseInfo = new Paragraph(processBidirectionalText("اسم العميل : "+ name[0]), font);
            caisseInfo.setAlignment(Paragraph.ALIGN_RIGHT);
            caisseInfo.setPaddingTop(5);
            document.add(caisseInfo);

            // إضافة رقم الكاشير
            Paragraph dateInfo = new Paragraph(processBidirectionalText("التاريخ : "+date), font);
            dateInfo.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(dateInfo);

            Paragraph newLine2 = new Paragraph(processBidirectionalText("\n"), font);
            newLine2.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(newLine2);

            // إنشاء جدول للفاتورة
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{10, 10, 10, 10, 2});

            String[] headers = {"المجموع", "السعر", "الكمية", "المنتج","#"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(processBidirectionalText(header), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setPaddingBottom(5);
                cell.setBackgroundColor(new BaseColor(224, 224, 224)); // خلفية رمادية (#e0e0e0)
                table.addCell(cell);
            }


            for (int i = 0; i < productCarts.size(); i++) {

                ProductCart productCart = productCarts.get(i);
                Product product = productList.get(i);
                if (sell.getCustomerID() != 0){
                    //PdfPTable table, String text, Font font
                    addCell(table,String.valueOf(product.getPriceSell()*productCart.getQty()),font);
                    addCell(table,String.valueOf(product.getPriceSell()),font);
                }else {
                    addCell(table,String.valueOf(product.getPriceBuy()*productCart.getQty()),font);
                    addCell(table,String.valueOf(product.getPriceBuy()),font);
                }
                addCell(table,String.valueOf(productCart.getQty()),font);
                addCell(table,String.valueOf(product.getName()),font);
                addCell(table,String.valueOf(i+1),font);
            }

            // إضافة الجدول إلى المستند
            document.add(table);

            Paragraph newLine3 = new Paragraph(processBidirectionalText("\n"), font);
            newLine3.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(newLine3);

            // إضافة المجموع الكلي
            Paragraph total = new Paragraph(processBidirectionalText("المبلغ الاجمالي : "+totalAmount), font);
            total.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(total);

            // إضافة المجموع الكلي
            Paragraph discount = new Paragraph(processBidirectionalText("مبلغ التخفيض : "+discountAmount), font);
            discount.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(discount);

            // إضافة المجموع الكلي
            Paragraph hasPay = new Paragraph(processBidirectionalText("المبلغ المدفوع : "+paymentAmount), font);
            hasPay.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(hasPay);

            // إضافة المجموع الكلي
            Paragraph rest = new Paragraph(processBidirectionalText("المبلغ المتبقي : "+restAmount), font);
            rest.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(rest);

            // إضافة رسالة الشكر
            Paragraph thanks = new Paragraph(processBidirectionalText("وشكرا لكم برنامج SMPOS"),font);
            thanks.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(thanks);

            // إغلاق المستند
            document.close();

            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(file.getPath()), "application/pdf");
                intent = Intent.createChooser(intent, "Open File");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }


    private static String shapeArabicText(String input) {
        try {
            ArabicShaping arabicShaping = new ArabicShaping(ArabicShaping.LETTERS_SHAPE);
            return arabicShaping.shape(input);
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }

    }

    private static String processBidirectionalText (String input){
        String shapedInput = shapeArabicText(input);
        Bidi bidi = new Bidi(shapedInput,Bidi.DIRECTION_DEFAULT_RIGHT_TO_LEFT);

        return  bidi.writeReordered(Bidi.DO_MIRRORING);
    }


    // دالة مساعدة لإضافة خلية مع توسيط النص
    private static void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(processBidirectionalText(text), font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingBottom(5);
        table.addCell(cell);
    }

    public static String lengthText(String s,int n){

        int length = s.length();
        if(length>n){
            s = s.substring(0,n);
            s = s+"...";
            return s;
        }
        return s;
    }

}
