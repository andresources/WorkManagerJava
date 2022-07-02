package com.workers;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.workmanager.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class WorkKUpdateNotificationStatusImageDownload extends Worker {
    public WorkKUpdateNotificationStatusImageDownload(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("WM","doWok->>");
        return downloadImage();
    }
    private Result downloadImage(){
        try {

            URL u = new URL("https://www.sakshi.com/sites/default/files/styles/storypage_main/public/article_images/2022/07/1/bagunna1.jpg");
            HttpURLConnection c = (HttpURLConnection)u.openConnection();
            c.connect();
            int lengthOfFile = c.getContentLength();
            Log.i("WM","try->>"+lengthOfFile);
            InputStream in = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1;
            long total = 0;
            while ((len1 = in.read(buffer)) > 0) {
                total += len1; //total = total + len1
                int percent = (int) ((total * 100) / lengthOfFile);
                Log.i("WM",""+percent);
                setForegroundAsync(displayNotification("DownLoading",percent));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("WM",""+e.getMessage());
            return Result.retry();
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
