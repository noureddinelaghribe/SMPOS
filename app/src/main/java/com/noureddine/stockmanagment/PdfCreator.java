package com.noureddine.stockmanagment;

public class PdfCreator {

//    public static void createPdf(Context context, List<Product> productList, String text, String fileName) {
//        // Get the directory for the user's public documents directory.
//        //File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//        File path = new File(context.getExternalFilesDir(null), REPORTS_PATH);
//
//        if (!path.exists()) {
//            path.mkdir(); // Create the directory and its parent directories if they don't exist
//        }
//
//        File file = new File(path, fileName);
//
//        try {
//            // Create the PDF writer
//            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
//
//            // Initialize PDF document
//            PdfDocument pdf = new PdfDocument(writer);
//
//            // Initialize document
//            Document document = new Document(pdf);
//
//            // Add a title to the document
//            document.add(new Paragraph("Product List"));
//
//            // Create a table with 3 columns (ID, Name, Email)
//            Table table = new Table(3);
//
//            // Add table headers
//            table.addCell("اسم المنتج");
//            table.addCell("سعر البيع");
//            table.addCell("الكمية المتوفرة");
//
//            // Add user data to the table
//
//            for (Product p : productList) {
//                table.addCell(String.valueOf(p.getName()));
//                table.addCell(String.valueOf(p.getPriceBuy()));
//                table.addCell(String.valueOf(p.getQuantity()));
//            }
//
//            // Add the table to the document
//            document.add(table);
//
//            // Close the document
//            document.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}