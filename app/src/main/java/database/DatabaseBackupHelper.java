
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


public class DatabaseBackupHelper {
    private static final String TAG = "DatabaseRestoreHelper";
    private final Context context;
    private static final String BACKUP_FOLDER_NAME = "SMDatabase_Backups";
    private static final String BACKUP_FILE_NAME = "SMDatabase_backup.db"; // You can also include timestamp if needed.

    public DatabaseBackupHelper(Context context) {
        this.context = context;
    }


    public BackupResult backupDatabase() {

        SMDatabase appDatabase = SMDatabase.getDatabase(context);
        appDatabase.close();
        File dbfile = context.getDatabasePath(DATABASE_NAME);
        File sdir = new File(Environment.getExternalStorageDirectory(), "SMPOS/"+BACKUP_FOLDER_NAME);

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
