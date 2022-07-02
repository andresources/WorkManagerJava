package com.workers;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.workmanager.OneTimeWorksActivity;
import com.workmanager.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WorkHWithRepeat extends Worker {
    public WorkHWithRepeat(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        displayNotification("Hello");

        return Result.success();
    }
    private void displayNotification(String keyword) {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle("Articles sur : " + keyword)
                .setContentText("This is my message")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_background);

        assert notificationManager != null;
        int cnt=new Random().nextInt(1000);
        notificationManager.notify(cnt, builder.build());

    }
}
