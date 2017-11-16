package com.hb.library.rxtask.comm;

import android.os.AsyncTask;

/**
 * 任务接口
 * Created by HuangBin on 2017/11/12.
 */
public interface Task {

    boolean isCancelTask();
    boolean cancelTask();
    boolean cancelTask(boolean mayInterruptIfRunning);
    AsyncTask.Status getTaskStatus();

}
