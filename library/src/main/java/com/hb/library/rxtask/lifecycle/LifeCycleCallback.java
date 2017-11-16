package com.hb.library.rxtask.lifecycle;

import android.os.Bundle;

/**
 * 生命周期回调接口
 * Created by HuangBin on 2017/11/15.
 */
public interface LifeCycleCallback {

    void onCreate(Bundle savedInstanceState);
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();

}
