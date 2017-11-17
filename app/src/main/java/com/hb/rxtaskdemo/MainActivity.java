package com.hb.rxtaskdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hb.library.rxtask.RxTask;
import com.hb.library.rxtask.comm.ProgressTask;
import com.hb.library.rxtask.callback.RxDoInBackground;
import com.hb.library.rxtask.callback.RxPostExecute;
import com.hb.library.rxtask.callback.RxProgressUpdate;
import com.hb.library.rxtask.comm.Task;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private Button startBtn1, startBtn2, startBtn3;
    private ProgressBar bar16, bar17;
    private TextView txtInfo;
    private TaskTest[] taskTests;
    private final int[] ids = new int[]{ R.id.bar1, R.id.bar2, R.id.bar3, R.id.bar4, R.id.bar5,
            R.id.bar6, R.id.bar7, R.id.bar8, R.id.bar9, R.id.bar10,
            R.id.bar11, R.id.bar12, R.id.bar13, R.id.bar14, R.id.bar15 };
    private AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onDestroy() {
        TaskTest taskTest = null;
        for(int n = 0, count = taskTests.length; n < count; n++){
            taskTest = taskTests[n];
            if(taskTest != null) {
                taskTest.progressBar = null;
                taskTest.task = null;
            }
            taskTests[n] = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startBtn1:
                if(startBtn1.isEnabled()){
                    startBtn1.setEnabled(false);
                    txtInfo.setText("");
                    startTask();
                }
                break;
            case R.id.startBtn2:
                if(startBtn2.isEnabled()){
                    startBtn2.setEnabled(false);
                    RxTask.create()
                            .scheduler(RxTask.THREAD_WORK)
                            .post(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                int n = 0;
                                while (n <= 100) {
                                    n++;
                                    bar16.setProgress(n);
                                    Thread.sleep(300);
                                }
                                RxTask.create()
                                        .scheduler(RxTask.THREAD_UI)
                                        .post(new Runnable() {

                                            @Override
                                            public void run() {
                                                txtInfo.append("Task #15执行成功!\n\n");
                                                startBtn2.setEnabled(true);
                                            }

                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
                }
                break;
            case R.id.startBtn3:
                if(startBtn3.isEnabled()){
                    startBtn3.setEnabled(false);
                    RxTask.create()
                            .workThread()
                            .bindLifeCycle(this)
                            .execute(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        int n = 0;
                                        while (n <= 100) {
                                            n++;
                                            bar17.setProgress(n);
                                            Thread.sleep(300);
                                        }
                                        RxTask.create()
                                                .scheduler(RxTask.THREAD_UI)
                                                .post(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        txtInfo.append("Task #16执行成功!\n\n");
                                                        startBtn3.setEnabled(true);
                                                    }

                                                });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            });
                }
                break;
        }
    }

    private void initView() {
        startBtn1 = findViewById(R.id.startBtn1);
        startBtn2 = findViewById(R.id.startBtn2);
        startBtn3 = findViewById(R.id.startBtn3);
        startBtn1.setOnClickListener(this);
        startBtn2.setOnClickListener(this);
        startBtn3.setOnClickListener(this);
        bar16 = findViewById(R.id.bar16);
        bar17 = findViewById(R.id.bar17);
        txtInfo = findViewById(R.id.txtInfo);
        taskTests = new TaskTest[ids.length];
        ProgressBar progressBar = null;
        int index = 0;
        //循环
        for(int id : ids){
            progressBar = findViewById(id);
            taskTests[index] = new TaskTest(index, progressBar);
            index++;
        }
    }

    private void startTask(){
        for(TaskTest taskTest : taskTests){
            taskTest.start();
        }
    }

    private class TaskTest {
        int index = -1;
        ProgressBar progressBar;
        Task task;

        TaskTest(int index, ProgressBar progressBar) {
            this.index = index;
            this.progressBar = progressBar;
        }

        void start() {
            task = RxTask
                    .<Void, Integer, Boolean>async()
                    .bindLifeCycle(MainActivity.this)
                    .doInBackground(new RxDoInBackground<Void, Integer, Boolean>() {

                        @Override
                        public Boolean doInBackground(ProgressTask<Integer> progressTask, Void... params) {
                            boolean result = true;
                            try {
                                int n = 0;
                                while (n <= 100) {
                                    n++;
                                    Thread.sleep(300);
                                    progressTask.updateProgress(n);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                result = false;
                            }
                            return result;
                        }

                    })
                    .progressUpdate(new RxProgressUpdate<Integer>() {

                        @Override
                        public void onProgressUpdate(Integer... values) {
                            int val = values[0];
                            progressBar.setProgress(val);
                        }

                    })
                    .postExecute(new RxPostExecute<Boolean>() {

                        @Override
                        public void onPostExecute(Boolean reuslt) {
                            int count = atomicInteger.incrementAndGet();
                            if(count >= ids.length){
                                startBtn1.setEnabled(true);
                                atomicInteger.set(0);
                            }
                            txtInfo.append(reuslt ?
                                    "Task #" + index + "执行成功!\n\n"
                                    : "Task #" + index + "执行失败!\n\n");
                        }

                    })
                    .execute();
        }

    }

}
