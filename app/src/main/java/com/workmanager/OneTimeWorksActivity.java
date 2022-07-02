package com.workmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.workers.WorkA;
import com.workers.WorkB;
import com.workers.WorkC;
import com.workers.WorkDWithDataResult;
import com.workers.WorkEWithProgress;
import com.workers.WorkFwithConstrains;
import com.workers.WorkGWithNotification;
import com.workers.WorkHWithRepeat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OneTimeWorksActivity extends AppCompatActivity {
    public static final String KEY_OUT_MESSAGE = "out_message";
    public static final String KEY_IN_MESSAGE = "in_message";
    public static final String PROGRESS = "progress";
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time);
        tv =(TextView)findViewById(R.id.tv);
        //Step 1: Define the work
        //By using Worker class, Worked is defined to run, and override doWork() of Worker class
        //Step 2: Create a WorkRequest(Once your work is defined, it must be scheduled.)
        //WorkRequest (and its subclasses) define how and when it should be run
        //OneTimeWorkRequest,PeriodicWorkRequest
        //You can schedule it to run periodically over an interval of time, or you can schedule it to run only one time.
        //Step 3 : Finally, you need to submit your WorkRequest to WorkManager using the enqueue() method.

        //Use full URLs
        //https://medium.com/androiddevelopers/workmanager-periodicity-ff35185ff006
        //https://medium.com/@sumon.v0.0/android-jetpack-workmanager-onetime-and-periodic-work-request-94ace224ff7d
        //https://medium.com/@ramkumarv3/work-manager-onetimeworkrequest-d8111df52501(for download image)
    }


    public void startSingleWork(View view) {
        WorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkA.class).build();
        WorkManager wm=WorkManager.getInstance(OneTimeWorksActivity.this);
        wm.enqueue(workRequest1);
    }

    public void startTwoWorks(View view) { //By Default, WorkA and WorkB are executed parallelly.
        ArrayList<WorkRequest> list=new ArrayList<>();
        WorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkA.class).build();
        WorkRequest workRequest2=new OneTimeWorkRequest.Builder(WorkB.class).build();
        list.add(workRequest1);
        list.add(workRequest2);
        WorkManager wm=WorkManager.getInstance(OneTimeWorksActivity.this);
        wm.enqueue(list);
    }

    public void startTwoWorksSequentially(View view) {
        OneTimeWorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkA.class).build();
        OneTimeWorkRequest workRequest2=new OneTimeWorkRequest.Builder(WorkB.class).build();
        WorkManager wm=WorkManager.getInstance(OneTimeWorksActivity.this);
        wm.beginWith(workRequest1).then(workRequest2).enqueue();
    }

    public void startTwoWorksParallelly(View view) {
        List<OneTimeWorkRequest> list=new ArrayList<>();
        OneTimeWorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkA.class).build();
        OneTimeWorkRequest workRequest2=new OneTimeWorkRequest.Builder(WorkB.class).build();
        OneTimeWorkRequest workRequest3=new OneTimeWorkRequest.Builder(WorkC.class).build();
        list.add(workRequest2);
        list.add(workRequest3);
        WorkManager wm=WorkManager.getInstance(OneTimeWorksActivity.this);
        wm.beginWith(workRequest1).then(list).enqueue();
    }

    public void startUniqueWork(View view) {
        OneTimeWorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkA.class).addTag("MyWorkA").build();
        WorkManager wm=WorkManager.getInstance(OneTimeWorksActivity.this);
        wm.enqueueUniqueWork("MyWorkA", ExistingWorkPolicy.KEEP,workRequest1);
    }
    OneTimeWorkRequest workRequest1;
    WorkManager wm;
    public void startWorkWithCallback(View view) {
        workRequest1=new OneTimeWorkRequest.Builder(WorkA.class).addTag("MyWorkA").build();
        wm=WorkManager.getInstance(OneTimeWorksActivity.this);
        wm.enqueue(workRequest1);
        wm.getWorkInfoByIdLiveData(workRequest1.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        tv.setText(workInfo.getState().toString());
                        if (workInfo != null) {
                            Log.i("WMStatus", "WorkInfo received: state: " + workInfo.getState());
                        }
                    }
                });
    }
    public void cancelledWork(View view) {
        wm.cancelWorkById(workRequest1.getId());
    }

    public void startWorkWithResultData(View view) {
        Data inputData = new Data.Builder()
                .putInt(KEY_IN_MESSAGE, 10)
                .build();
        WorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkDWithDataResult.class).setInputData(inputData).build();

        WorkManager WM=WorkManager.getInstance(OneTimeWorksActivity.this);
        WM.enqueue(workRequest1);

        WM.getWorkInfoByIdLiveData(workRequest1.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            Log.i("WM", "WorkInfo received: state: " + workInfo.getState());
                            if(workInfo.getState()==WorkInfo.State.SUCCEEDED) {
                                String message = workInfo.getOutputData().getString(KEY_OUT_MESSAGE);
                                Log.i("WM", "message: " + message);
                            }

                        }
                    }
        });
    }

    public void startWorkWithProgress(View view) {
        WorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkEWithProgress.class).build();
        WorkManager WM=WorkManager.getInstance(OneTimeWorksActivity.this);
        WM.enqueue(workRequest1);
        WM.getWorkInfoByIdLiveData(workRequest1.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            Data progress = workInfo.getProgress();
                            int value = progress.getInt(PROGRESS, 0);
                            if(value>0) {
                                tv.setText(value + "");
                            }
                            if(workInfo.getState()==WorkInfo.State.SUCCEEDED) {
                                tv.setText("SUCCEEDED");
                            }

                        }
                    }
                });
    }
    public void startWorkWithConstraints(View view) {
        Constraints constraints=new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        workRequest1=new OneTimeWorkRequest.Builder(WorkFwithConstrains.class).setConstraints(constraints).build();
        wm=WorkManager.getInstance(OneTimeWorksActivity.this);
        wm.enqueueUniqueWork("MyW",ExistingWorkPolicy.KEEP,workRequest1);
        wm.getWorkInfoByIdLiveData(workRequest1.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        tv.setText(workInfo.getState().toString());
                        if (workInfo != null) {
                            Log.i("WMStatus", "WorkInfo received: state: " + workInfo.getState());
                        }
                    }
                });
    }

    public void startWorkWithNotification(View view) {
        WorkRequest workRequest1 = OneTimeWorkRequest.from(WorkGWithNotification.class);
        //WorkRequest workRequest1=new OneTimeWorkRequest.Builder(WorkGWithNotification.class).build();
        WorkManager wm=WorkManager.getInstance(OneTimeWorksActivity.this);
        wm.enqueue(workRequest1);
    }

    public void startWorkWithRepeat(View view) {
         workRequest1=new OneTimeWorkRequest.Builder(WorkHWithRepeat.class).setInitialDelay(5, TimeUnit.SECONDS).build();
         wm=WorkManager.getInstance(OneTimeWorksActivity.this);
         wm.enqueue(workRequest1);
    }
}