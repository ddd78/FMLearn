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
import com.a78.com.fmlearn.views.UiLoad;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecomodationFragment extends BaseFragement implements IRecommendationCallBack, UiLoad.OnRetryClickListener {

    private static final String TAG = "RecomodationFragment";
    RecommendationListAdapter recommendationListAdapter;
    private RecommendationPresenter recommendationPresenter;
    private UiLoad uiLoad;
    private View rootview;


    @Override
    protected View onSubViewLoad(final LayoutInflater layoutInflater, ViewGroup container) {

        uiLoad = new UiLoad(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                return creteSuccessView(layoutInflater,container);
            }
        };

        uiLoad.setOnRetryClickListenser(this);
//
//        uiLoad = new UiLoad(this.getContext()) {
//        };

//        getRecommendationData();

        recommendationPresenter = RecommendationPresenter.getRecommendationInstance();
        recommendationPresenter.registerViewCallBack(this);
        recommendationPresenter.getRecommendationList();

        //
        if (uiLoad.getParent() instanceof ViewGroup){
            ((ViewGroup) uiLoad.getParent()).removeView(uiLoad);
        }


        return uiLoad;
    }

    private View creteSuccessView(LayoutInflater layoutInflater, ViewGroup container) {
        rootview = layoutInflater.inflate(R.layout.fragment_recomodation,container,false);
        RecyclerView recommendRv = rootview.findViewById(R.id.recommend_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recommendationListAdapter = new RecommendationListAdapter();
        recommendRv.setLayoutManager(linearLayoutManager);
        recommendRv.setAdapter(recommendationListAdapter);



        return rootview;

    }


    @Override
    public void onRecommendationLoad(List<Album> result) {
        recommendationListAdapter.setAlbumList(result);
        uiLoad.updateUi(UiLoad.UiLoadType.SUCCESS);
    }

    @Override
    public void onNetError() {
        uiLoad.updateUi(UiLoad.UiLoadType.ERROR);
    }

    @Override
    public void onEmpty() {
        uiLoad.updateUi(UiLoad.UiLoadType.EMPTY);
    }

    @Override
    public void onLoading() {
        uiLoad.updateUi(UiLoad.UiLoadType.LOADING);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recommendationPresenter != null) {
            recommendationPresenter.unRegisterViewCallBack(this);
        }
    }

    @Override
    public void OnRetryClick() {
        if (recommendationPresenter != null){
            recommendationPresenter.getRecommendationList();
        }
    }
}
