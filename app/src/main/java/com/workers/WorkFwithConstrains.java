package com.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkFwithConstrains extends Worker {
    public WorkFwithConstrains(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("WM","WorkF Started");
        //Log.i("WM","doWork() : Thread-> "+Thread.currentThread().getName());
        for(int i=1;i<=100;i++){
            Log.i("WM", "WorkF Value - " + i);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Result.failure() ,Result.retry(),Result.success()
        Log.i("WM","WorkF Ended");
        return Result.success();
    }
}
