package com.a78.com.fmlearn.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by home on 2020/1/17.
 */

public abstract class BaseFragement extends Fragment {

    private View view;

//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        view = onSubViewLoad(layoutInflater);
//        return view;
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return onSubViewLoad(inflater,container);
    }

    protected abstract View onSubViewLoad(LayoutInflater layoutInflater,ViewGroup container);
}
