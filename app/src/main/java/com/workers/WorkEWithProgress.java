package com.workers;

import static com.workmanager.OneTimeWorksActivity.PROGRESS;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkEWithProgress extends Worker {
    public WorkEWithProgress(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        //setProgressAsync(new Data.Builder().putInt(PROGRESS,0).build());
    }

    @NonNull
    @Override
    public Result doWork() {
        for (int i=1;i<=10;i++){
            setProgressAsync(new Data.Builder().putInt(PROGRESS,i).build());
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){}

        }
        return Result.success();
    }
}
