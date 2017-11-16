package com.hb.library.rxtask;

import android.app.Activity;
import android.app.Fragment;

import com.hb.library.rxtask.comm.RxBuilder;
import com.hb.library.rxtask.comm.Task;

import java.lang.ref.WeakReference;

/**
 * 构造器
 * Created by HuangBin on 2017/11/12.
 */
public class ScheduleBuilder {

    private int type = RxTask.THREAD_CURR;

    ScheduleBuilder() {  }

    public _Builder workThread() {
        return new _Builder();
    }

    public ScheduleBuilder scheduler(int threadType) {
        type = threadType;
        return this;
    }

    public void post(Runnable r) {
        if(r == null) { return; }
        switch (type) {
            case RxTask.THREAD_CURR:
                r.run();
                break;
            case RxTask.THREAD_UI:
                RxTask.RxAsyncTask._post(r);
                break;
            case RxTask.THREAD_WORK:
                RxTask.RxAsyncTask._execute(r);
                break;
        }
    }

    public static class _Builder implements RxBuilder {

        private WeakReference<Object> rxContext;

        _Builder() {  }

        @Override
        public _Builder bindLifeCycle(Object context) {
            if(context instanceof Activity
                    || context instanceof Fragment
                    || context instanceof android.support.v4.app.Fragment) {
                rxContext = new WeakReference<>(context);
            } else {
                throw new RuntimeException("bindLifeCycle of context must is Activity or Fragment!");
            }
            return this;
        }

        public Task execute(Runnable r) {
            if(r == null) { return null; }
            Task task = RxTask.RxAsyncTask._execTask(r);
            Object context = rxContext == null ? null : rxContext.get();
            if(context != null) {
                RxTask.RxAsyncTask.bindingLifeCycle(context, task);
            }
            return task;
        }

    }

}
