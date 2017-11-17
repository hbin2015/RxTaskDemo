package com.hb.library.rxtask;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.hb.library.rxtask.comm.IObserver;
import com.hb.library.rxtask.comm.ProgressTask;
import com.hb.library.rxtask.comm.Task;
import com.hb.library.rxtask.comm.WorkTask;
import com.hb.library.rxtask.lifecycle.AttFramgent;
import com.hb.library.rxtask.lifecycle.AttachedFragment;
import com.hb.library.rxtask.lifecycle.AttachedFragmentV4;
import com.hb.library.rxtask.lifecycle.LifeCycleObserver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Rx异步任务类
 * Created by HuangBin on 2017/11/12.
 */
public class RxTask {

    public static final int THREAD_CURR = 0x001;//当前线程
    public static final int THREAD_UI = 0x002;//UI主线程
    public static final int THREAD_WORK = 0x003;//工作线程

    public static ScheduleBuilder create() {
        return new ScheduleBuilder();
    }

    public static <Params, Progress, Result> AsyncBuilder<Params, Progress, Result> async() {
        return new AsyncBuilder<>();
    }

    static abstract class RxAsyncTask<Params, Progress, Result>
            extends AsyncTask<Params, Progress, Result>
            implements Task, ProgressTask<Progress>, IObserver {

        static final Executor CACHED_EXECUTOR;
        static final Handler mainHandler;
        static final Map<Integer, AttFramgent> attFramgentMap;

        static {
            CACHED_EXECUTOR = new ThreadPoolExecutor(0, 100,
                    15L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>());
            mainHandler = obtainHandler();
            attFramgentMap = new HashMap<>();
        }

        static void _post(Runnable runnable) {
            if(mainHandler != null) {
                mainHandler.post(runnable);
            }
        }

        static void _execute(Runnable runnable) {
            CACHED_EXECUTOR.execute(runnable);
        }

        static Task _execTask(Runnable runnable) {
            WorkTask workTask = new WorkTask(runnable);
            CACHED_EXECUTOR.execute(workTask);
            return workTask;
        }

        static void bindingLifeCycle(Object context, Task task) {
            if(task == null) { return; }
            if(context instanceof FragmentActivity
                    || context instanceof android.support.v4.app.Fragment) {
                int id = context instanceof android.support.v4.app.Fragment
                        ? ((android.support.v4.app.Fragment) context).getActivity().hashCode()
                        : context.hashCode();
                AttFramgent attFramgent = attFramgentMap.get(id);
                if(attFramgent == null) {
                    attFramgent = new AttachedFragmentV4();
                    attFramgentMap.put(id, attFramgent);
                    android.support.v4.app.FragmentManager fragmentManager = null;
                    if(context instanceof FragmentActivity) { fragmentManager = ((FragmentActivity) context).getSupportFragmentManager(); }
                    if(context instanceof android.support.v4.app.Fragment) { fragmentManager = ((android.support.v4.app.Fragment) context).getActivity().getSupportFragmentManager(); }
                    fragmentManager.beginTransaction().add(((android.support.v4.app.Fragment) attFramgent), AttachedFragmentV4.TAG).commitAllowingStateLoss();
                }
                attFramgent.setAttachFramgentMap(id, attFramgentMap);
                List<LifeCycleObserver> observers = attFramgent.obtainObservers();
                observers.add(((IObserver) task).getObserver());
            }
            else if(context instanceof Activity
                    || context instanceof Fragment) {
                int id = context instanceof Fragment
                        ? ((Fragment) context).getActivity().hashCode()
                        : context.hashCode();
                AttFramgent attFramgent = attFramgentMap.get(id);
                if(attFramgent == null) {
                    attFramgent = new AttachedFragment();
                    attFramgentMap.put(id, attFramgent);
                    FragmentManager fragmentManager = null;
                    if(context instanceof Activity) { fragmentManager = ((Activity) context).getFragmentManager(); }
                    if(context instanceof Fragment) { fragmentManager = ((Fragment) context).getActivity().getFragmentManager(); }
                    fragmentManager.beginTransaction().add(((Fragment) attFramgent), AttachedFragment.TAG).commitAllowingStateLoss();
                }
                attFramgent.setAttachFramgentMap(id, attFramgentMap);
                List<LifeCycleObserver> observers = attFramgent.obtainObservers();
                observers.add(((IObserver) task).getObserver());
            }
        }

        private static Handler obtainHandler() {
            Handler handler = null;

            try {
                Class<?> superCls = AsyncTask.class;
                Field field = superCls.getDeclaredField("sHandler");
                field.setAccessible(true);
                Object obj = field.get(null);
                if (obj == null) {
                    Method method = null;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        method = superCls.getDeclaredMethod("getHandler");
                    } else {
                        method = superCls.getDeclaredMethod("getMainHandler");
                    }
                    method.setAccessible(true);
                    obj = method.invoke(null);
                }
                handler = (Handler) obj;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return handler;
        }


        final LifeCycleObserver observer = new LifeCycle_Observer();

        Task _executeTask(Params... params) {
            return (Task) executeOnExecutor(CACHED_EXECUTOR, params);
        }

        @Override
        public boolean isCancelTask() {
            return isCancelled();
        }

        @Override
        public boolean cancelTask() {
            return cancelTask(true);
        }

        @Override
        public boolean cancelTask(boolean mayInterruptIfRunning) {
            return cancel(mayInterruptIfRunning);
        }

        @Override
        public Status getTaskStatus() {
            return getStatus();
        }

        @Override
        public void updateProgress(Progress... values) {
            publishProgress(values);
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

}
