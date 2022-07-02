package com.workers;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.workmanager.R;

import java.util.Random;

public class WorkJUpdateNotificationStatus extends Worker {
    public WorkJUpdateNotificationStatus(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        for(int i=1;i<100;i++){
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){}
            setForegroundAsync(displayNotification("6PM Job",i));

        }
        return Result.success();
    }
    private ForegroundInfo displayNotification(String keyword,int progress) {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle("Status " + keyword)
                .setTicker(keyword)
                .setContentText("This is contenttext")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(100,progress,false)
                .setSmallIcon(R.drawable.ic_launcher_background);

        assert notificationManager != null;
        int cnt=new Random().nextInt(1000);

        //notificationManager.notify(cnt, builder.build());
        //return ForegroundInfo(notificationId, notification)
        android.app.Notification nn=builder.build();
        return new ForegroundInfo(100,nn);
    }
}
