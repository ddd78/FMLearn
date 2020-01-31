package com.a78.com.fmlearn.fragements;


import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.a78.com.fmlearn.R;
import com.a78.com.fmlearn.adapters.RecommendationListAdapter;
import com.a78.com.fmlearn.base.BaseFragement;
import com.a78.com.fmlearn.interfaces.IRecommendationCallBack;
import com.a78.com.fmlearn.presenters.RecommendationPresenter;
import com.a78.com.fmlearn.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecomodationFragment extends BaseFragement implements IRecommendationCallBack {

    private static final String TAG = "RecomodationFragment";
    RecommendationListAdapter recommendationListAdapter;
    private RecommendationPresenter recommendationPresenter;


    @Override
    protected View onSubViewLoad(LayoutInflater layoutInflater, ViewGroup container) {
        View view = layoutInflater.inflate(R.layout.fragment_recomodation,container,false);

        RecyclerView recommendRv = view.findViewById(R.id.recommend_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recommendationListAdapter = new RecommendationListAdapter();
        recommendRv.setLayoutManager(linearLayoutManager);
        recommendRv.setAdapter(recommendationListAdapter);

        recommendationPresenter = RecommendationPresenter.getRecommendationInstance();
        recommendationPresenter.registerViewCallBack(this);
        recommendationPresenter.getRecommendationList();

//        getRecommendationData();

        return view;
    }




    @Override
    public void onRecommendationLoad(List<Album> result) {
        recommendationListAdapter.setAlbumList(result);
    }

    @Override
    public void onLoadMore(List<Album> result) {

    }

    @Override
    public void onRefreshMore(List<Album> result) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recommendationPresenter != null) {
            recommendationPresenter.unRegisterViewCallBack(this);
        }
    }
}
