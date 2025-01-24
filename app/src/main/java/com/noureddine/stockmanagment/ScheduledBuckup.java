package com.noureddine.stockmanagment;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import database.DatabaseBackupHelper;


public class ScheduledBuckup extends Worker {

    public ScheduledBuckup(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Do the work here--in this case, upload the images.
        Log.d("Worker", "doWork: Do the work "+Result.success());

        Context context = getApplicationContext();
        DatabaseBackupHelper backupHelper = new DatabaseBackupHelper(context);
        backupHelper.backupDatabase();

        return Result.success();
    }

}
