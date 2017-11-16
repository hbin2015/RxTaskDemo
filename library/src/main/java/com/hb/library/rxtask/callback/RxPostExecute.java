package com.hb.library.rxtask.callback;

/**
 * 异步执行后回调接口
 * (UI线程)
 * Created by HuangBin on 2017/11/12.
 */
public interface RxPostExecute<Result> {

    void onPostExecute(Result result);

}
