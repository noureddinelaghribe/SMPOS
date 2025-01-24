package database;

import static Utles.Utel.DATABASE_NAME;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class DatabaseRestoreHelper {
    private static final String TAG = "DatabaseRestoreHelper";
    private static final String BACKUP_FOLDER_NAME = "SMDatabase_Backups";
    private static final String BACKUP_FILE_NAME = "SMDatabase_backup.db";
    private final Context context;

    public DatabaseRestoreHelper(Context context) {
        this.context = context;
    }

    public boolean restoreDatabase() {
        try {

            //File backupFolder = new File(context.getExternalFilesDir(null), BACKUP_FOLDER_NAME);
            File backupFolder = new File(Environment.getExternalStorageDirectory(), "SMPOS/"+BACKUP_FOLDER_NAME);
            //File backupFolder = new File("/storage/emulated/0/SMDatabase_Backups");
            if (!backupFolder.exists()) {
                Log.e(TAG, "Restore failed file is not exists "+BACKUP_FOLDER_NAME);
            }

            // Get backup and current database files
            File backupFile = new File(backupFolder, BACKUP_FILE_NAME);
            File currentDB = context.getDatabasePath(DATABASE_NAME);

            // Check if backup exists
            if (!backupFile.exists()) {
                Log.e(TAG, "Backup file not found");
                return false;
            }

            // Close existing database connection
            context.deleteDatabase(DATABASE_NAME);

            // Perform the restore
            try (FileChannel src = new FileInputStream(backupFile).getChannel();
                 FileChannel dst = new FileOutputStream(currentDB).getChannel()) {
                dst.transferFrom(src, 0, src.size());
                Log.d(TAG, "Database restored successfully");
                return true;
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to restore database", e);
            return false;
        }
    }
}
