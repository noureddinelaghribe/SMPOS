//package database;
//
//import android.content.Context;
//import android.os.Build;
//import android.os.Environment;
//import androidx.documentfile.provider.DocumentFile;
//import android.util.Log;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.nio.channels.FileChannel;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import Utles.Utel;
//
//public class SimpleBackupRestore {
//    private static final String TAG = "DatabaseRestoreHelper";
//    private final Context context;
//    private static final String BACKUP_FOLDER_NAME = "SMDatabase_Backups";
//    private static final String BACKUP_FILE_NAME = "SMDatabase_backup.db";
//
//    public SimpleBackupRestore(Context context) {
//        this.context = context;
//    }
//
//    public BackupResult backupDatabase() {
//        try {
//            // Create backup folder if it doesn't exist
//            File backupFolder = new File(context.getExternalFilesDir(null), BACKUP_FOLDER_NAME);
//            if (!backupFolder.exists()) {
//                boolean created = backupFolder.mkdirs();
//                if (!created) {
//                    return new BackupResult(false, "Failed to create backup directory");
//                }
//            }
//
//            // Generate backup file name with timestamp
////            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
////            String backupFileName = "SMDatabase_" + timeStamp + ".db";
//            //File backupFile = new File(backupFolder, backupFileName);
//            File backupFile = new File(backupFolder, BACKUP_FILE_NAME);
//
//            // Get current database file
//            File currentDB = context.getDatabasePath(Utel.DATABASE_NAME);
//            if (!currentDB.exists()) {
//                return new BackupResult(false, "Database file not found");
//            }
//
//            // Perform backup using FileChannel for efficient copying
//            try (FileChannel src = new FileInputStream(currentDB).getChannel();
//                 FileChannel dst = new FileOutputStream(backupFile).getChannel()) {
//
//                dst.transferFrom(src, 0, src.size());
//                return new BackupResult(true, "Backup successful: " + backupFile.getAbsolutePath());
//            }
//
//        } catch (Exception e) {
//            Log.e(TAG, "Backup failed", e);
//            return new BackupResult(false, "Backup failed: " + e.getMessage());
//        }
//    }
//
////    public BackupResult restoreDatabase(File backupFile) {
////        try {
////            // Verify backup file exists
////            if (!backupFile.exists()) {
////                return new BackupResult(false, "Backup file not found");
////            }
////
////            // Get current database file
////            File currentDB = context.getDatabasePath("SMDatabase");
////
////            // Close the database connection before restore
////            context.deleteDatabase("SMDatabase");
////
////            // Perform restore using FileChannel
////            try (FileChannel src = new FileInputStream(backupFile).getChannel();
////                 FileChannel dst = new FileOutputStream(currentDB).getChannel()) {
////
////                dst.transferFrom(src, 0, src.size());
////                return new BackupResult(true, "Restore successful");
////            }
////
////        } catch (Exception e) {
////            Log.e(TAG, "Restore failed", e);
////            return new BackupResult(false, "Restore failed: " + e.getMessage());
////        }
////    }
//
//    // Helper class to handle backup/restore results
//    public static class BackupResult {
//        public final boolean success;
//        public final String message;
//
//        public BackupResult(boolean success, String message) {
//            this.success = success;
//            this.message = message;
//        }
//    }
//
//    // Get list of available backups
////    public File[] getAvailableBackups() {
////        File backupFolder = new File(context.getExternalFilesDir(null), BACKUP_FOLDER_NAME);
////        if (backupFolder.exists()) {
////            return backupFolder.listFiles((dir, name) -> name.endsWith(".db"));
////        }
////        return new File[0];
////    }
//
//
//
//
//}


package database;

import static Utles.Utel.DATABASE_NAME;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseBackupHelper {
    private static final String TAG = "DatabaseRestoreHelper";
    private final Context context;
    private static final String BACKUP_FOLDER_NAME = "SMDatabase_Backups";
    private static final String BACKUP_FILE_NAME = "SMDatabase_backup.db"; // You can also include timestamp if needed.

    public DatabaseBackupHelper(Context context) {
        this.context = context;
    }

//    public BackupResult backupDatabase() {
//        try {
//            // Create backup folder if it doesn't exist
//            File backupFolder = new File(context.getExternalFilesDir(null), BACKUP_FOLDER_NAME);
//            if (!backupFolder.exists()) {
//                boolean created = backupFolder.mkdirs();
//                if (!created) {
//                    return new BackupResult(false, "Failed to create backup directory");
//                }
//            }
//
//            // Generate backup file name with timestamp (if needed)
////            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
////            String backupFileName = "SMDatabase_" + timeStamp + ".db";
//            File backupFile = new File(backupFolder, BACKUP_FILE_NAME);
//
//            // Get current database file
//            File currentDB = context.getDatabasePath(Utel.DATABASE_NAME);
//            if (!currentDB.exists()) {
//                return new BackupResult(false, "Database file not found");
//            }
//
//            // Perform backup using FileChannel for efficient copying
//            try (FileChannel src = new FileInputStream(currentDB).getChannel();
//                 FileChannel dst = new FileOutputStream(backupFile).getChannel()) {
//
//                dst.transferFrom(src, 0, src.size());
//                return new BackupResult(true, "Backup successful: " + backupFile.getAbsolutePath());
//            }
//
//        } catch (Exception e) {
//            Log.e(TAG, "Backup failed", e);
//            return new BackupResult(false, "Backup failed: " + e.getMessage());
//        }
//    }

    public BackupResult backupDatabase() {
        //String BACKUP_FOLDER_NAME = "SMDatabase_Backups";
        //String BACKUP_FILE_NAME = "SMDatabase_backup.db";
        SMDatabase appDatabase = SMDatabase.getDatabase(context);
        appDatabase.close();
        File dbfile = context.getDatabasePath(DATABASE_NAME);
        //File sdir = new File(context.getExternalFilesDir(null), BACKUP_FOLDER_NAME);
        File sdir = new File(Environment.getExternalStorageDirectory(), "SMPOS/"+BACKUP_FOLDER_NAME);

//        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
//        String backupFileName = "SMDatabase_" + timeStamp + ".db";
//        String sfpath = sdir.getPath() + File.separator + backupFileName;

        String sfpath = sdir.getPath() + File.separator + BACKUP_FILE_NAME;

        if (!sdir.exists()) {
            boolean created = sdir.mkdirs();
            if (!created) {
                return new BackupResult(false, "Failed to create backup directory");
            }
        } else {
            checkAndDeleteBackupFile(sdir, sfpath);
        }
        File savefile = new File(sfpath);
        if (savefile.exists()) {
            Log.d("LOGGER", "File exists. Deleting it and then creating new file.");
            savefile.delete();
        }
        try {
            if (savefile.createNewFile()) {
                int buffersize = 8 * 1024;
                byte[] buffer = new byte[buffersize];
                int bytes_read = buffersize;
                OutputStream savedb = new FileOutputStream(sfpath);
                InputStream indb = new FileInputStream(dbfile);
                while ((bytes_read = indb.read(buffer, 0, buffersize)) > 0) {
                    savedb.write(buffer, 0, bytes_read);
                }
                savedb.flush();
                indb.close();
                savedb.close();
                return new BackupResult(true, "Backup successful: " + savefile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("LOGGER", "ex: " + e);
            return new BackupResult(false, "Backup failed: " + e.getMessage());
        }
        return null;
    }

    public void checkAndDeleteBackupFile(File directory, String path) {
        //This is to prevent deleting extra file being deleted which is mentioned in previous comment lines.
        int MAXIMUM_DATABASE_FILE = 10000;
        File currentDateFile = new File(path);
        int fileIndex = -1;
        long lastModifiedTime = System.currentTimeMillis();

        if (!currentDateFile.exists()) {
            File[] files = directory.listFiles();
            if (files != null && files.length >= MAXIMUM_DATABASE_FILE) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    long fileLastModifiedTime = file.lastModified();
                    if (fileLastModifiedTime < lastModifiedTime) {
                        lastModifiedTime = fileLastModifiedTime;
                        fileIndex = i;
                    }
                }

                if (fileIndex != -1) {
                    File deletingFile = files[fileIndex];
                    if (deletingFile.exists()) {
                        deletingFile.delete();
                    }
                }
            }
        }
    }

    // Helper class to handle backup/restore results
    public static class BackupResult {
        public final boolean success;
        public final String message;

        public BackupResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }



}
