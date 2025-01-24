package com.noureddine.stockmanagment;

import static com.noureddine.stockmanagment.Opiration.longToDate;
import static com.noureddine.stockmanagment.Opiration.longToDateTime;
import static com.google.common.io.Resources.getResource;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.noureddine.stockmanagment.R;


import java.util.ArrayList;
import java.util.List;

import Controlar.AdapterReport;
import model.Buy;
import model.ReportT1;
import model.ReportT2;
import model.Sell;
import model.Transactions;

public class ReportsActivity extends AppCompatActivity {

    TextView textViewTotalItems,textViewTotalCustomers,textViewTotalSuppliers,
            textViewTotalDebtsCustomers,textViewTotalCheckouts,
            textViewTotalCheckins,textViewTotalDebtsSuppliers,
    button_from,button_to,textViewMoreLess;

    Button button;

    RecyclerView recyclerView;
    AdapterReport adapterReport;

    List<Object> reportList = new ArrayList<>();

    SMViewModel smViewModel;

    long timeStare = 0 , timeEnd = 0 ;

    LinearLayout more,linear_less,linear_more;

    boolean viewMore = false;

    //add liners layouts to show every one in recycler view

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewTotalItems = findViewById(R.id.textView_total_items);
        textViewTotalCustomers = findViewById(R.id.textView_total_customers);
        textViewTotalSuppliers = findViewById(R.id.textView_total_suppliers);
        textViewTotalDebtsCustomers = findViewById(R.id.textView_total_debts_customers);
        textViewTotalDebtsSuppliers = findViewById(R.id.textView_total_debts_suppliers);
        textViewTotalCheckouts = findViewById(R.id.textView_total_checkouts);
        textViewTotalCheckins = findViewById(R.id.textView_total_checkins);
        button_to = findViewById(R.id.button_to);
        button_from = findViewById(R.id.button_from);
        button = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recyclerView);
        more = findViewById(R.id.LinearLayout6);
        linear_more = findViewById(R.id.linear_more);
        linear_less = findViewById(R.id.linear_less);
        textViewMoreLess = findViewById(R.id.textView13);

        button_from.setText("--/--/----");
        button_to.setText("--/--/----");

        smViewModel = new ViewModelProvider(this).get(SMViewModel.class);

        adapterReport = new AdapterReport();
        recyclerView.setAdapter(adapterReport);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        linear_more.setVisibility(View.GONE);
        linear_less.setVisibility(View.VISIBLE);
        textViewMoreLess.setText("عرض المزيد ...");

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View v) {

                if ( timeEnd == 0 || timeStare == 0 ){
                    Toast.makeText(ReportsActivity.this, "الرجاء اختيار تاريخ الاستعلام", Toast.LENGTH_SHORT).show();
                }else {
                    loadReport();
                }

//                smViewModel.getAllProduct().observe(ReportsActivity.this, new Observer<List<Product>>() {
//                    @Override
//                    public void onChanged(List<Product> products) {
//                        String text = "This is a sample text that will be converted to a PDF.";
//                        String fileName = "sample.pdf";
//                        PdfCreator.createPdf(ReportsActivity.this, products, text, fileName);
//                    }
//                });

                //file:///android_asset/NotoNaskhArabic-Regular.ttf

//                File path = new File(getExternalFilesDir(null), REPORTS_PATH);
//
//                if (!path.exists()) {
//                    path.mkdir(); // Create the directory and its parent directories if they don't exist
//                }
//
//                File file = new File(path, "try.pdf");
//                createPdfWithArabicText(String.valueOf(file),getAssets());
//                String fontPath = "file:///android_asset/NotoNaskhArabic-Regular.ttf"; // Font file name in assets folder
//




                //AssetManager assetManager = getAssets();
                //createPdfWithArabicText(String.valueOf(file),assetManager);
                //createPdf(String.valueOf(file),assetManager);

//
//                try {
//                    // إنشاء المستند و PdfWriter
//                    Document document = new Document();
//                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
//
//                    // تعيين اتجاه النص إلى اليمين لليسار
//                    writer.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//
//                    // فتح المستند
//                    document.open();
//
//                    // تحميل الخط العربي
//                    BaseFont arabicFont = BaseFont.createFont("assets/fonts/NotoNaskhArabic-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//
//                    // إنشاء فقرة مع نص باللغة العربية
//                    String arabicText = processBidirectionalText("مرحبًا بالعالم");
//                    Paragraph paragraph = new Paragraph(arabicText, new com.itextpdf.text.Font(arabicFont, 14));
//                    paragraph.setAlignment(Element.ALIGN_RIGHT);
//
//                    // إضافة الفقرة إلى المستند
//                    document.add(paragraph);
//
//                    // إغلاق المستند
//                    document.close();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//




//                File path = new File(getExternalFilesDir(null), REPORTS_PATH);
//
//                if (!path.exists()) {
//                    path.mkdir(); // Create the directory and its parent directories if they don't exist
//                }
//
//                File file = new File(path, "try.pdf");
//                Document document = new Document();
//
//                try {
//                    PdfWriter.getInstance(document, new FileOutputStream(file));
//                    document.open();
//
//                    BaseFont baseFont = BaseFont.createFont("assets/fonts/NotoSansArabic-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//                    Font font = new Font(baseFont, 12, Font.NORMAL);
//
//                    // إضافة معلومات العميل والتاريخ
//                    Paragraph clientInfo = new Paragraph(processBidirectionalText("فاتورة مبيعات نقدا : "),  new Font(baseFont, 18, Font.BOLD));
//                    clientInfo.setAlignment(Paragraph.ALIGN_CENTER);
//                    document.add(clientInfo);
//
//                    Paragraph newLine = new Paragraph(processBidirectionalText("\n\n"), font);
//                    newLine.setAlignment(Paragraph.ALIGN_RIGHT);
//                    document.add(newLine);
//
//                    // إضافة رقم الكاشير
//                    Paragraph nomB = new Paragraph(processBidirectionalText("رقم الفاتورة : 2415"), font);
//                    nomB.setAlignment(Paragraph.ALIGN_RIGHT);
//                    document.add(nomB);
//
//                    // إضافة رقم الكاشير
//                    Paragraph caisseInfo = new Paragraph(processBidirectionalText("اسم العميل : نور الدين لغريب"), font);
//                    caisseInfo.setAlignment(Paragraph.ALIGN_RIGHT);
//                    caisseInfo.setPaddingTop(5);
//                    document.add(caisseInfo);
//
//                    // إضافة رقم الكاشير
//                    Paragraph dateInfo = new Paragraph(processBidirectionalText("التاريخ : 21/11/2025 11:30 "), font);
//                    dateInfo.setAlignment(Paragraph.ALIGN_LEFT);
//                    document.add(dateInfo);
//
//                    Paragraph newLine2 = new Paragraph(processBidirectionalText("\n"), font);
//                    newLine2.setAlignment(Paragraph.ALIGN_RIGHT);
//                    document.add(newLine2);
//
//                    // إنشاء جدول للفاتورة
//                    PdfPTable table = new PdfPTable(5);
//                    table.setWidthPercentage(100);
//                    table.setWidths(new float[]{10, 10, 10, 10, 2});
//
//                    String[] headers = {"المجموع", "السعر", "الكمية", "المنتج","#"};
//                    for (String header : headers) {
//                        PdfPCell cell = new PdfPCell(new Paragraph(processBidirectionalText(header), font));
//                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        cell.setVerticalAlignment(Element.ALIGN_CENTER);
//                        cell.setPaddingBottom(5);
//                        cell.setBackgroundColor(new BaseColor(224, 224, 224)); // خلفية رمادية (#e0e0e0)
//                        table.addCell(cell);
//                    }
//
//                    // إضافة بيانات الجدول
//                    String[][] data = {
//                            {"500", "500", "1","زيت زيتون","1"},
//                            {"1500", "1500", "1","بيمو","2"},
//                            {"155.00", "15.00", "11", "كاشير","3"},
//                            {"900", "900", "1", "ماء","4"},
//                            {"1800", "200", "9", "حليب كونديا","5"}
//                    };
//
//                    for (String[] row : data) {
//                        for (String cellData : row) {
//                            PdfPCell cell = new PdfPCell(new Paragraph(processBidirectionalText(cellData), font));
//                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
//                            cell.setPaddingBottom(5);
//                            table.addCell(cell);
//                        }
//                    }
//
//                    document.add(table);
//
//                    Paragraph newLine3 = new Paragraph(processBidirectionalText("\n"), font);
//                    newLine3.setAlignment(Paragraph.ALIGN_RIGHT);
//                    document.add(newLine3);
//
//                    // إضافة المجموع الكلي
//                    Paragraph total = new Paragraph(processBidirectionalText("المبلغ الاجمالي : 5500"), font);
//                    total.setAlignment(Paragraph.ALIGN_RIGHT);
//                    document.add(total);
//
//                    // إضافة المجموع الكلي
//                    Paragraph discount = new Paragraph(processBidirectionalText("مبلغ التخفيض : 000"), font);
//                    discount.setAlignment(Paragraph.ALIGN_RIGHT);
//                    document.add(discount);
//
//                    // إضافة المجموع الكلي
//                    Paragraph hasPay = new Paragraph(processBidirectionalText("المبلغ المدفوع : 1500"), font);
//                    hasPay.setAlignment(Paragraph.ALIGN_RIGHT);
//                    document.add(hasPay);
//
//                    // إضافة المجموع الكلي
//                    Paragraph rest = new Paragraph(processBidirectionalText("المبلغ المتبقي : 4000"), font);
//                    rest.setAlignment(Paragraph.ALIGN_RIGHT);
//                    document.add(rest);
//
//                    // إضافة رسالة الشكر
//                    Paragraph thanks = new Paragraph(processBidirectionalText("وشكرا لكم برنامج SMPOS"),font);
//                    thanks.setAlignment(Paragraph.ALIGN_CENTER);
//                    document.add(thanks);
//
//                    // إغلاق المستند
//                    document.close();
//
//                    Intent intent;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        Uri uri = FileProvider.getUriForFile(ReportsActivity.this, getPackageName() + ".provider", file);
//                        intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(uri);
//                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        startActivity(intent);
//                    } else {
//                        intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setDataAndType(Uri.parse(file.getPath()), "application/pdf");
//                        intent = Intent.createChooser(intent, "Open File");
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//
//                } catch (DocumentException | IOException e) {
//                    e.printStackTrace();
//                }

            }
        });

        button_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final long[] data = new long[1];

                // Create the MaterialDatePicker with customizations
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("تاريخ البدء :") // Custom title in Arabic
                        .setPositiveButtonText("اختيار")  // Custom positive button text
                        .setNegativeButtonText("إلغاء")  // Custom negative button text
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Default to today's date
                        .build();

                // Show the date picker
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    // Get the selected date and set it to the TextView
                    //String formattedDate = datePicker.getHeaderText();
                    timeStare =datePicker.getSelection();
                    Log.d("datePicker", "System.currentTimeMillis() : "+ System.currentTimeMillis());
                    Log.d("datePicker", "timeStare : "+ timeStare);
                    button_from.setText(longToDate(timeStare));  // Display the selected date
                    //loadReport();
                });

                // Optional: Handle negative button click (e.g., dismiss dialog)
                datePicker.addOnNegativeButtonClickListener(dialog -> {
                    // Handle cancellation
                });

            }
        });

        button_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final long[] data = new long[1];

                // Create the MaterialDatePicker with customizations
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("تاريخ انتهاء :") // Custom title in Arabic
                        .setPositiveButtonText("اختيار")  // Custom positive button text
                        .setNegativeButtonText("إلغاء")  // Custom negative button text
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Default to today's date
                        .build();

                // Show the date picker
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    // Get the selected date and set it to the TextView
                    //String formattedDate = datePicker.getHeaderText();
                    timeEnd =datePicker.getSelection();
                    //Log.d("TAG", "getExpiryDate : "+ data);
                    Log.d("datePicker", "timeEnd : "+ timeEnd);
                    button_to.setText(longToDate(timeEnd));  // Display the selected date
                    //loadReport();
                });

                // Optional: Handle negative button click (e.g., dismiss dialog)
                datePicker.addOnNegativeButtonClickListener(dialog -> {
                    // Handle cancellation
                });

            }
        });


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( timeStare == 0 || timeEnd == 0 ){
                    Toast.makeText(ReportsActivity.this, "اخر الوقت اولا", Toast.LENGTH_SHORT).show();
                }else {
                    if (!viewMore){
                        linear_less.setVisibility(View.VISIBLE);
                        linear_more.setVisibility(View.GONE);
                        textViewMoreLess.setText("عرض المزيد ...");
                        viewMore = true;
                    }else {
                        linear_less.setVisibility(View.GONE);
                        linear_more.setVisibility(View.VISIBLE);
                        textViewMoreLess.setText("عرض اقل ...");
                        viewMore = false;
                    }

                }

            }
        });








    }





//    private void createPdfWithArabicText(String outputPath, AssetManager assetManager) {
//
//        try {
//            // Create a PDF writer
//            PdfWriter writer = new PdfWriter(outputPath);
//            writer.setSmartMode(true);
//            PdfDocument pdfDocument = new PdfDocument(writer);
//            Document document = new Document(pdfDocument);
//
//            // Load the Arabic font from the assets folder
//            //String fontPath = "NotoNaskhArabic-Regular.ttf"; // Font file name in assets folder
//            String fontPath = "fonts/NotoNaskhArabic-Bold.ttf"; // Font file name in assets folder
//            InputStream fontInputStream = assetManager.open(fontPath);
//            byte[] fontBytes = new byte[fontInputStream.available()];
//            fontInputStream.read(fontBytes);
//            fontInputStream.close();
//
//            // Create the font using the byte array
//            PdfFont arabicFont = PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H);
//
//            // Add Arabic text to the PDF
//            String arabicText = "مرحبًا بالعالم"; // Arabic text
//            Paragraph paragraph = new Paragraph(arabicText)
//                    .setFont(arabicFont) // Set the custom Arabic font
//                    .setTextAlignment(TextAlignment.RIGHT) // Align text to the right for RTL
//                    .setFontSize(14);
//            document.add(paragraph);
//
//            // Close the document
//            document.close();
//
//            System.out.println("PDF created at: " + outputPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void createPdf(String outputPath, AssetManager assetManager) {
//        try {
//            // Initialize PdfWriter and PdfDocument
//            PdfWriter writer = new PdfWriter(outputPath);
//            PdfDocument pdfDocument = new PdfDocument(writer);
//            Document document = new Document(pdfDocument);
//
//            // Load Arabic font from assets
//            String fontPath = "fonts/NotoNaskhArabic-Bold.ttf"; // Font file name in assets folder
//            InputStream fontInputStream = assetManager.open(fontPath);
//            byte[] fontBytes = new byte[fontInputStream.available()];
//            fontInputStream.read(fontBytes);
//            fontInputStream.close();
//
//            // Create the font using the byte array
//            PdfFont arabicFont = PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H);
//
//            // Add Arabic text to the PDF
//            String arabicText = "مرحبًا بالعالم"; // Arabic text
//            Paragraph paragraph = new Paragraph(arabicText)
//                    .setFont(arabicFont) // Set the custom Arabic font
//                    .setFontSize(14) // Set font size
//                    .setBaseDirection(BaseDirection.RIGHT_TO_LEFT) // Ensure RTL direction
//                    .setTextAlignment(TextAlignment.RIGHT); // Align text to the right for RTL
//
//            // Add the paragraph to the document
//            document.add(paragraph);
//
//            // Close the document
//            document.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//


    public void loadReport(){

        adapterReport.clearData();

        if (timeStare == timeEnd){
            timeEnd = timeStare + 86400000 - 1 ;
        }

        smViewModel.timeTransactions( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<List<Transactions>>() {
            @Override
            public void onChanged(List<Transactions> transactions) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (Transactions t : transactions){
                            switch (t.getType()){
                                case "insertProduct":

                                case "updateProduct":

                                case "deleteProduct":

                                    reportList.add(new ReportT1(t.getType(), smViewModel.getProductById(t.getProductId()).getName(), longToDateTime(t.getTimestamp())));

                                    break;

                                case "insertCustomer":

                                case "updateCustomer":

                                case "deleteCustomer":

                                    reportList.add(new ReportT1(t.getType(), smViewModel.getCustomerById(t.getCustmerId()).getName(), longToDateTime(t.getTimestamp())));

                                    break;

                                case "insertSupplier":

                                case "updateSupplier":

                                case "deleteSupplier":

                                    reportList.add(new ReportT1(t.getType(), smViewModel.getSupplierById(t.getSupplierId()).getName(), longToDateTime(t.getTimestamp())));

                                    break;

                                case "insertSelling":

                                    String namesProductsSelling = "";
                                    int i = 0;
                                    for (String item : smViewModel.getSelleById(t.getSellingId()).getIdsProducts() ){
                                        if (namesProductsSelling.isEmpty()){
                                            namesProductsSelling = smViewModel.getProductByIdp(item).getName()+"  :  "+t.getQuantity().get(i);
                                            i++;
                                        }else {
                                            namesProductsSelling = namesProductsSelling + "\n" + smViewModel.getProductByIdp(item).getName()+"  :  "+t.getQuantity().get(i);
                                            i++;
                                        }
                                    }
                                    //List<String> idsProducts, int customerID, double totalAmount,double discountAmount, double paymentAmount,double restAmount, String typePayment, String note
                                    Sell sell = new Sell(smViewModel.getSelleById(t.getSellingId()).getTotalAmount(),
                                            smViewModel.getSelleById(t.getSellingId()).getDiscountAmount(),
                                            smViewModel.getSelleById(t.getSellingId()).getpaymentAmount(),
                                            smViewModel.getSelleById(t.getSellingId()).getRestAmount(),
                                            smViewModel.getSelleById(t.getSellingId()).getTypePayment(),
                                            smViewModel.getSelleById(t.getSellingId()).getNote());

                                    //String type, String name, List<String> namesProducts, Sell sell, String date
                                    reportList.add(new ReportT2(t.getSellingId(),
                                            t.getType(),
                                            smViewModel.getCustomerById(smViewModel.getSelleById(t.getSellingId()).getCustomerID()).getName(),
                                            namesProductsSelling,
                                            sell,
                                            longToDateTime(t.getTimestamp()),
                                            t.getQuantity()
                                    ));

                                    break;

                                case "insertBuying":

                                    String namesProductsBuying = "";
                                    int n = 0;
                                    for (String item : smViewModel.getBayeingById(t.getBuyingId()).getIdsProducts() ){
                                        if (namesProductsBuying.isEmpty()){
                                            namesProductsBuying = smViewModel.getProductByIdp(item).getName()+"  :  "+t.getQuantity().get(n);
                                            n++;
                                        }else {
                                            namesProductsBuying = namesProductsBuying + "\n" + smViewModel.getProductByIdp(item).getName()+"  :  "+t.getQuantity().get(n);
                                            n++;
                                        }
                                    }
                                    //double discountAmount, double totalAmount, double paymentAmount, double restAmount, String typePayment, String note
                                    Buy buy =new Buy(
                                            smViewModel.getBayeingById(t.getBuyingId()).getTotalAmount(),
                                            smViewModel.getBayeingById(t.getBuyingId()).getDiscountAmount(),
                                            smViewModel.getBayeingById(t.getBuyingId()).getpaymentAmount(),
                                            smViewModel.getBayeingById(t.getBuyingId()).getRestAmount(),
                                            smViewModel.getBayeingById(t.getBuyingId()).getTypePayment(),
                                            smViewModel.getBayeingById(t.getBuyingId()).getNote()
                                    );
                                    //String type, String name, List<String> namesProducts, Buy buy, String date
                                    reportList.add(new ReportT2(t.getBuyingId(),
                                            t.getType(),
                                            smViewModel.getSupplierById(smViewModel.getBayeingById(t.getBuyingId()).getSupplierID()).getName(),
                                            namesProductsBuying,
                                            buy,
                                            longToDateTime(t.getTimestamp()),
                                            t.getQuantity()
                                    ));

                                    break;

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapterReport.addReport(reportList);


                                }
                            });

                        }

                    }
                }).start();
            }
        });


        smViewModel.getCountSuppliers( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewTotalSuppliers.setText(String.valueOf(integer));
            }
        });

        smViewModel.getCountCustomers( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewTotalCustomers.setText(String.valueOf(integer));
            }
        });

        smViewModel.getCountProducts( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewTotalItems.setText(String.valueOf(integer));
            }
        });

        smViewModel.getCountSelles( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewTotalCheckouts.setText(String.valueOf(integer));
            }
        });

        smViewModel.getCountBayersByTime( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewTotalCheckins.setText(String.valueOf(integer));
            }
        });




        smViewModel.getCountSellesDebts( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<List<Sell>>() {
            @Override
            public void onChanged(List<Sell> sells) {
                int total = 0;
                for (Sell s : sells){
                    total+=s.getRestAmount();
                }
                textViewTotalDebtsCustomers.setText("("+sells.size()+") "+total);
            }
        });

        smViewModel.getCountBuyersDebts( timeStare, timeEnd).observe(ReportsActivity.this, new Observer<List<Buy>>() {
            @Override
            public void onChanged(List<Buy> buys) {
                int total = 0;
                for (Buy b : buys){
                    total+=b.getRestAmount();
                }
                textViewTotalDebtsSuppliers.setText("("+buys.size()+") "+total);
            }
        });



    }



}