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

import com.workmanager.LiveDataHelper;
import com.workmanager.R;

import java.util.Random;

public class WorkLUpdateStatusUsingLiveData extends Worker {
    private LiveDataHelper liveDataHelper;
    public WorkLUpdateStatusUsingLiveData(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        liveDataHelper = LiveDataHelper.getInstance();
    }

    @NonNull
    @Override
    public Result doWork() {
        for(int i=1;i<100;i++){
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){}
            liveDataHelper.updatePercentage(i);
        }
        return Result.success();
    }

}
