package com.hb.library.rxtask.comm;

/**
 * 进度接口
 * (work线程)
 * Created by HuangBin on 2017/11/12.
 */
public interface ProgressTask<Progress> {

    void updateProgress(Progress... values);

}
