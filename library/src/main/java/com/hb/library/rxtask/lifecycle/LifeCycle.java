package com.hb.library.rxtask.lifecycle;

import android.os.Bundle;

import java.util.List;

/**
 * 生命周期枚举类
 * Created by HuangBin on 2017/11/15.
 */
public enum LifeCycle {

    onCreate(0x011),
    onStart(0x012),
    onResume(0x013),
    onPause(0x014),
    onStop(0x015),
    onDestroy(0x016);

    private int type;

    LifeCycle(int type) {
        this.type = type;
    }

    public void perfrom(List<LifeCycleObserver> observers, Object...params) {
        if(observers == null || observers.size() == 0) { return; }
        for(LifeCycleObserver observer : observers) {
            switch (type) {
                case 0x011:
                    observer.onCreate(params == null ? null : (Bundle) params[0]);
                    break;
                case 0x012:
                    observer.onStart();
                    break;
                case 0x013:
                    observer.onResume();
                    break;
                case 0x014:
                    observer.onPause();
                    break;
                case 0x015:
                    observer.onStop();
                    break;
                case 0x016:
                    observer.onDestroy();
                    break;
            }
        }
    }

}
