package com.hb.library.rxtask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;

import com.hb.library.rxtask.callback.RxCancelled;
import com.hb.library.rxtask.callback.RxDoInBackground;
import com.hb.library.rxtask.callback.RxPostExecute;
import com.hb.library.rxtask.callback.RxPreExecute;
import com.hb.library.rxtask.callback.RxProgressUpdate;
import com.hb.library.rxtask.comm.RxBuilder;
import com.hb.library.rxtask.comm.Task;

import java.lang.ref.WeakReference;

/**
 * 构造器
 * Created by HuangBin on 2017/11/12.
 */
public class AsyncBuilder<Params, Progress, Result> implements RxBuilder {

    private WeakReference<Object> rxContext;
    private WeakReference<RxPreExecute> rxPreExecute;
    private WeakReference<RxDoInBackground<Params, Progress, Result>> rxDoInBackground;
    private WeakReference<RxProgressUpdate<Progress>> rxProgressUpdate;
    private WeakReference<RxPostExecute<Result>> rxPostExecute;
    private WeakReference<RxCancelled> rxCancelled;

    AsyncBuilder() {  }

    @Override
    public AsyncBuilder<Params, Progress, Result> bindLifeCycle(Object context) {
        if(context instanceof Activity
                || context instanceof Fragment
                || context instanceof android.support.v4.app.Fragment) {
            rxContext = new WeakReference<>(context);
        } else {
            throw new RuntimeException("bindLifeCycle of context must is Activity or Fragment!");
        }
        return this;
    }

    public AsyncBuilder<Params, Progress, Result> preExecute(RxPreExecute rxPreExecute) {
        this.rxPreExecute = new WeakReference<>(rxPreExecute);
        return this;
    }

    public AsyncBuilder<Params, Progress, Result> doInBackground(
            RxDoInBackground<Params, Progress, Result> rxDoInBackground) {
        this.rxDoInBackground = new WeakReference<>(rxDoInBackground);
        return this;
    }

    public AsyncBuilder<Params, Progress, Result> progressUpdate(
            RxProgressUpdate<Progress> rxProgressUpdate) {
        this.rxProgressUpdate = new WeakReference<>(rxProgressUpdate);
        return this;
    }

    public AsyncBuilder<Params, Progress, Result> postExecute(
            RxPostExecute<Result> rxPostExecute) {
        this.rxPostExecute = new WeakReference<>(rxPostExecute);
        return this;
    }

    public AsyncBuilder<Params, Progress, Result> cancelled(RxCancelled rxCancelled) {
        this.rxCancelled = new WeakReference<>(rxCancelled);
        return this;
    }

    public Task execute(Params... params) {
        Task task = perfromTask(params);
        Object context = rxContext == null ? null : rxContext.get();
        if(context != null) {
            RxTask.RxAsyncTask.bindingLifeCycle(context, task);
        }
        return task;
    }

    private Task perfromTask(Params... params) {
        @SuppressLint("StaticFieldLeak")
        RxTask.RxAsyncTask<Params, Progress, Result> asyncTask =
                new RxTask.RxAsyncTask<Params, Progress, Result>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        RxPreExecute _preExecute = rxPreExecute == null ? null : rxPreExecute.get();
                        if(_preExecute != null){
                            _preExecute.onPreExecute();
                        }
                    }

                    @Override
                    protected Result doInBackground(Params[] params) {
                        RxDoInBackground<Params, Progress, Result> _doInBackground =
                                rxDoInBackground == null ? null : rxDoInBackground.get();
                        if(_doInBackground != null){
                            return _doInBackground.doInBackground(this, params);
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Progress[] values) {
                        super.onProgressUpdate(values);
                        RxProgressUpdate<Progress> _progressUpdate = rxProgressUpdate == null ? null : rxProgressUpdate.get();
                        if(_progressUpdate != null){
                            _progressUpdate.onProgressUpdate(values);
                        }
                    }

                    @Override
                    protected void onPostExecute(Result result) {
                        super.onPostExecute(result);
                        RxPostExecute<Result> _postExecute = rxPostExecute == null ? null : rxPostExecute.get();
                        if(_postExecute != null){
                            _postExecute.onPostExecute(result);
                        }
                    }

                    @Override
                    protected void onCancelled() {
                        super.onCancelled();
                        RxCancelled _cancelled = rxCancelled == null ? null : rxCancelled.get();
                        if(_cancelled != null){
                            _cancelled.onCancelled();
                        }
                    }

                    @Override
                    protected void finalize() throws Throwable {
                        release();
                        super.finalize();
                    }

                    private void release() {
                        if(rxPreExecute != null){
                            rxPreExecute.clear();
                            rxPreExecute = null;
                        }
                        if(rxDoInBackground != null){
                            rxDoInBackground.clear();
                            rxDoInBackground = null;
                        }
                        if(rxProgressUpdate != null){
                            rxProgressUpdate.clear();
                            rxProgressUpdate = null;
                        }
                        if(rxPostExecute != null){
                            rxPostExecute.clear();
                            rxPostExecute = null;
                        }
                        if(rxCancelled != null){
                            rxCancelled.clear();
                            rxCancelled = null;
                        }
                    }

                };
        return asyncTask._executeTask(params);
    }

}
