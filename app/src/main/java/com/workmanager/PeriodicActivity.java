package com.workmanager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.workers.WorkA;
import com.workers.WorkHWithRepeat;
import com.workers.WorkIWithSpecificTimeEveryDay;
import com.workers.WorkJUpdateNotificationStatus;
import com.workers.WorkKUpdateNotificationStatusImageDownload;
import com.workers.WorkLUpdateStatusUsingLiveData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PeriodicActivity extends AppCompatActivity {
    TimePicker tp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodic);
        tp=(TimePicker)findViewById(R.id.tp);
        tp.setIs24HourView(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void scheduleWork(View view) {
        Calendar cal=Calendar.getInstance();
        cal.set(2022,05,30,tp.getHour(),tp.getMinute(),00);
        long now=System.currentTimeMillis();
        long fut=cal.getTimeInMillis();
        long diff=fut-now;
        if(fut>now) {
            OneTimeWorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkHWithRepeat.class).addTag("MyWorkA").setInitialDelay(diff, TimeUnit.MILLISECONDS).build();
            WorkManager wm=WorkManager.getInstance(PeriodicActivity.this);
            wm.enqueueUniqueWork("MyWorkA", ExistingWorkPolicy.APPEND,workRequest1);
            Toast.makeText(getApplicationContext(), "" + diff, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Exp" , Toast.LENGTH_SHORT).show();
        }
    }

    public void repeatWork(View view) {
        PeriodicWorkRequest workRequest=new PeriodicWorkRequest.Builder(WorkHWithRepeat.class,16,TimeUnit.MINUTES).build();
        WorkManager wm=WorkManager.getInstance(PeriodicActivity.this);
        wm.enqueueUniquePeriodicWork("My_Work", ExistingPeriodicWorkPolicy.KEEP,workRequest);
    }

    public void specificTimeInADay(View view) {
        Calendar currentDate=Calendar.getInstance();
        Calendar dueDate=Calendar.getInstance();
        dueDate.set(Calendar.HOUR_OF_DAY, 18);
        dueDate.set(Calendar.MINUTE, 0);
        dueDate.set(Calendar.SECOND, 0);
        if(dueDate.before(currentDate)){
            dueDate.add(Calendar.HOUR_OF_DAY, 24);
        }
        long diff=dueDate.getTimeInMillis()-currentDate.getTimeInMillis();
        PeriodicWorkRequest workRequest=new PeriodicWorkRequest.Builder(WorkIWithSpecificTimeEveryDay.class,24,TimeUnit.HOURS).setInitialDelay(diff,TimeUnit.MILLISECONDS).build();
        WorkManager wm=WorkManager.getInstance(PeriodicActivity.this);
        wm.enqueueUniquePeriodicWork("My_Work_95", ExistingPeriodicWorkPolicy.KEEP,workRequest);
    }

    public void updateNotificationProgress(View view) {
        OneTimeWorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkJUpdateNotificationStatus.class).build();
        WorkManager wm=WorkManager.getInstance(PeriodicActivity.this);
        wm.enqueueUniqueWork("MyWorkA", ExistingWorkPolicy.APPEND,workRequest1);
    }

    public void updateNotificationProgressDownLoadImage(View view) {
        WorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkKUpdateNotificationStatusImageDownload.class).build();
        WorkManager wm=WorkManager.getInstance(PeriodicActivity.this);
        wm.enqueue(workRequest1);
    }

    public void updateLiveData(View view) {
        WorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkLUpdateStatusUsingLiveData.class).build();
        WorkManager wm=WorkManager.getInstance(PeriodicActivity.this);
        wm.enqueue(workRequest1);
        LiveDataHelper.getInstance().observePercentage()
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer integer) {
                        // update your progressBar here.
                        Log.i("WM",integer.toString());
                    }
                });
    }
}