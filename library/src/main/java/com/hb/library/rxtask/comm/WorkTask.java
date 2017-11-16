package com.hb.library.rxtask.comm;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.hb.library.rxtask.lifecycle.LifeCycleObserver;

import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 工作任务
 * Created by HuangBin on 2017/11/16.
 */
public class WorkTask extends FutureTask<Void>
        implements Task, IObserver {

    private AsyncTask.Status mState = AsyncTask.Status.PENDING;
    private final AtomicBoolean mCancelled = new AtomicBoolean();

    final LifeCycleObserver observer = new LifeCycle_Observer();

    public WorkTask(@NonNull Runnable runnable) {
        super(runnable, null);
    }

    @Override
    public void run() {
        mState = AsyncTask.Status.RUNNING;
        super.run();
    }

    @Override
    protected void done() {
        super.done();
        mState = AsyncTask.Status.FINISHED;
    }

    @Override
    public boolean isCancelTask() {
        return mCancelled.get();
    }

    @Override
    public boolean cancelTask() {
        return cancelTask(true);
    }

    @Override
    public boolean cancelTask(boolean mayInterruptIfRunning) {
        mCancelled.set(true);
        return cancel(mayInterruptIfRunning);
    }

    @Override
    public AsyncTask.Status getTaskStatus() {
        return mState;
    }

    @Override
    public LifeCycleObserver getObserver() {
        return observer;
    }

    private class LifeCycle_Observer extends LifeCycleObserver {

        @Override
        public void onDestroy() {
            super.onDestroy();
            cancelTask();
        }
    }

}
