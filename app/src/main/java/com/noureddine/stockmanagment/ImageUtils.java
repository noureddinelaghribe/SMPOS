package com.noureddine.stockmanagment;

import static Utles.Utel.IMAGE_PATH;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtils {

    // Convert Bitmap to URI and save
    public static String saveBitmapToUri(Context context, Bitmap bitmap, String fileName) {
        try {
            // Create directory if it doesn't exist
            File directory = new File(context.getExternalFilesDir(null), IMAGE_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create file
            File file = new File(directory, fileName + ".jpg");
            //storage/emulated/0/Android/data/com.example.stockmanagment/files/SMIMGS/tryyooo.jpg

            // Convert bitmap to file
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // Get URI from file
            return String.valueOf(file);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Save image from camera/gallery
//    public static Uri saveImage(Context context, Bitmap bitmap) {
//        try {
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                    Locale.getDefault()).format(new Date());
//            String imageName = "IMG_" + timeStamp;
//
//            Log.d("TAG", "saveImage: "+imageName);
//
//
//            return saveBitmapToUri(context, bitmap, imageName);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static Bitmap uriToBitmap(Context context, Uri uri) {
        try {
            ContentResolver resolver = context.getContentResolver();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // Delete image
    public static boolean deleteImage(Context context, String fileName) {
        File file = new File(context.getFilesDir()+IMAGE_PATH+"/" + fileName);
        return file.exists() && file.delete();
    }

    // Get all saved images
    public static List<Uri> getAllSavedImages(Context context) {
        List<Uri> uris = new ArrayList<>();
        File directory = new File(context.getFilesDir(), IMAGE_PATH);

        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                uris.add(Uri.fromFile(file));
            }
        }

        return uris;
    }
}
