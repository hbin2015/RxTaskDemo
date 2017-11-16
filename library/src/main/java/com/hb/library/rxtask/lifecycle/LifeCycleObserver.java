package com.hb.library.rxtask.lifecycle;

import android.os.Bundle;

/**
 * 生命周期观察者
 * Created by HuangBin on 2017/11/15.
 */
public abstract class LifeCycleObserver implements LifeCycleCallback {

    @Override
    public void onCreate(Bundle savedInstanceState) { }

    @Override
    public void onStart() { }

    @Override
    public void onResume() { }

    @Override
    public void onPause() { }

    @Override
    public void onStop() { }

    @Override
    public void onDestroy() { }

}
