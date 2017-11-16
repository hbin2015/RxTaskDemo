package com.hb.library.rxtask.lifecycle;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

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
        LifeCycle.onCreate.perfrom(observers, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        LifeCycle.onStart.perfrom(observers);
    }

    @Override
    public void onResume() {
        super.onResume();
        LifeCycle.onResume.perfrom(observers);
    }

    @Override
    public void onPause() {
        LifeCycle.onPause.perfrom(observers);
        super.onPause();
    }

    @Override
    public void onStop() {
        LifeCycle.onStop.perfrom(observers);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        LifeCycle.onDestroy.perfrom(observers);
        if(attFramgentMap != null){
            attFramgentMap.remove(key);
        }
        attFramgentMap = null;
        super.onDestroy();
    }

}
