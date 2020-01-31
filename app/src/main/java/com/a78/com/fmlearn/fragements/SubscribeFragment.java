package com.a78.com.fmlearn.fragements;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a78.com.fmlearn.R;
import com.a78.com.fmlearn.base.BaseFragement;


public class SubscribeFragment extends BaseFragement {


    @Override
    protected View onSubViewLoad(LayoutInflater layoutInflater, ViewGroup container) {
        View view = layoutInflater.inflate(R.layout.fragment_subscribe,container,false);
        return view;
    }
}
