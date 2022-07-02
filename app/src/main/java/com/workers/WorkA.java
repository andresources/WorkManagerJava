package com.workers;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.workmanager.R;

public class WorkA extends Worker {
    public WorkA(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("WM","WorkA Started");
        //Log.i("WM","doWork() : Thread-> "+Thread.currentThread().getName());
        for(int i=1;i<=5;i++){
            Log.i("WM", "WorkA Value - " + i);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Result.failure() ,Result.retry(),Result.success()
        Log.i("WM","WorkA Ended");
        return Result.success();
    }
}
