package com.hb.library.rxtask.callback;

/**
 * 异步数据更新回调接口
 * (UI线程)
 * Created by HuangBin on 2017/11/12.
 */
public interface RxProgressUpdate<Progress> {

    void onProgressUpdate(Progress... values);

}
