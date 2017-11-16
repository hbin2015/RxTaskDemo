package com.hb.library.rxtask.lifecycle;

import java.util.List;
import java.util.Map;

/**
 * 附着接口
 * Created by HuangBin on 2017/11/15.
 */
public interface AttFramgent {

    void setAttachFramgentMap(int id, Map<Integer, AttFramgent> map);
    List<LifeCycleObserver> obtainObservers();

}
