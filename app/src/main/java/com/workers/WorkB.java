package com.workers;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkB extends Worker {
    public WorkB(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("WM","WorkB Started");
        //Log.i("WM","doWork() : Thread-> "+Thread.currentThread().getName());
        for(int i=11;i<=15;i++){
            Log.i("WM", "WorkB Value - " + i);
            try {
                Thread.sleep(1500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Result.failure() ,Result.retry(),Result.success()
        Log.i("WM","WorkB Ended");
        return Result.success();
    }
}

