package com.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkC extends Worker {
    public WorkC(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("WM","WorkC Started");
        //Log.i("WM","doWork() : Thread-> "+Thread.currentThread().getName());
        for(int i=41;i<=45;i++){
            Log.i("WM", "WorkC Value - " + i);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Result.failure() ,Result.retry(),Result.success()
        Log.i("WM","WorkC Ended");
        return Result.success();
    }
}
