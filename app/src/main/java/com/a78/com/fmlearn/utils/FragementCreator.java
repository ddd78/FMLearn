package com.a78.com.fmlearn.utils;

import com.a78.com.fmlearn.base.BaseFragement;
import com.a78.com.fmlearn.fragements.HistoryFragment;
import com.a78.com.fmlearn.fragements.RecomodationFragment;
import com.a78.com.fmlearn.fragements.SubscribeFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by home on 2020/1/17.
 */

public class FragementCreator {
    public final static int INDEX_RECOMMED = 0;
    public final static int INDEX_SUBSCRIBE = 1;
    public final static int INDEX_HISTORY = 2;

    public final static int PAGE_COUNT = 3;

    private static Map<Integer, BaseFragement> sCache = new HashMap<>();

    public static BaseFragement getFragement(int index){
        BaseFragement baseFragement = sCache.get(index);
        if (baseFragement != null){
            return baseFragement;
        }
        switch (index){
            case INDEX_RECOMMED:
                baseFragement = new RecomodationFragment();
                break;
            case INDEX_SUBSCRIBE:
                baseFragement = new SubscribeFragment();
                break;
            case INDEX_HISTORY:
                baseFragement = new HistoryFragment();
                break;
        }
        sCache.put(index,baseFragement);
        return baseFragement;
    }
}
