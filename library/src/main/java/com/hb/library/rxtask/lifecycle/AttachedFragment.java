package com.hb.library.rxtask.lifecycle;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 附着的fragment
 * Created by HuangBin on 2017/11/15.
 */
public class AttachedFragment extends Fragment
        implements AttFramgent {

    public static final String TAG = AttachedFragment.class.getSimpleName();

    private int key;
    private Map<Integer, AttFramgent> attFramgentMap;
    private List<LifeCycleObserver> observers = new ArrayList<>();

    @Override
    public void setAttachFramgentMap(int id, Map<Integer, AttFramgent> map) {
        key = id;
        attFramgentMap = map;
    }

    @Override
    public List<LifeCycleObserver> obtainObservers() {
        return observers;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, "onCreate");
        LifeCycle.onCreate.perfrom(observers, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.d(TAG, "onStart");
        LifeCycle.onStart.perfrom(observers);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.d(TAG, "onResume");
        LifeCycle.onResume.perfrom(observers);
    }

    @Override
    public void onPause() {
        //Log.d(TAG, "onPause");
        LifeCycle.onPause.perfrom(observers);
        super.onPause();
    }

    @Override
    public void onStop() {
        //Log.d(TAG, "onStop");
        LifeCycle.onStop.perfrom(observers);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        //Log.d(TAG, "onDestroy");
        LifeCycle.onDestroy.perfrom(observers);
        if(attFramgentMap != null){
            attFramgentMap.remove(key);
        }
        attFramgentMap = null;
        super.onDestroy();
    }

}
