package com.hb.library.rxtask.callback;

import com.hb.library.rxtask.comm.ProgressTask;

/**
 * 异步在后台执行任务回调接口
 * (work线程)
 * Created by HuangBin on 2017/11/12.
 */
public interface RxDoInBackground<Params, Progress, Result> {

    Result doInBackground(ProgressTask<Progress> progressTask, Params... params);

}
