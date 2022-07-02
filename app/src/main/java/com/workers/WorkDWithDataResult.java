package com.workers;

import static com.workmanager.OneTimeWorksActivity.KEY_IN_MESSAGE;
import static com.workmanager.OneTimeWorksActivity.KEY_OUT_MESSAGE;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkDWithDataResult extends Worker {
    Data in_data;
    public WorkDWithDataResult(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        in_data=workerParams.getInputData();
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.i("WM","WorkD Started");
        //Log.i("WM","doWork() : Thread-> "+Thread.currentThread().getName());
        for(int i=31;i<=35;i++){
            Log.i("WM", "WorkD Value - " + i);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Result.failure() ,Result.retry(),Result.success()
        Log.i("WM","WorkD Ended");
        int res = in_data.getInt(KEY_IN_MESSAGE,0)+10;
        Data outputData = new Data.Builder()
                .putString(KEY_OUT_MESSAGE, "Result : "+res)
                .build();
        return Result.success(outputData);
    }
}
