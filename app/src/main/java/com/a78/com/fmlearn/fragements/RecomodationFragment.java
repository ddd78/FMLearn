package com.a78.com.fmlearn.fragements;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.a78.com.fmlearn.R;
import com.a78.com.fmlearn.RecommendationContentActivity;
import com.a78.com.fmlearn.adapters.RecommendationListAdapter;
import com.a78.com.fmlearn.base.BaseFragement;
import com.a78.com.fmlearn.interfaces.IRecommendationCallBack;
import com.a78.com.fmlearn.presenters.RecommendationContentPresenter;
import com.a78.com.fmlearn.presenters.RecommendationPresenter;
import com.a78.com.fmlearn.views.UiLoad;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public class RecomodationFragment extends BaseFragement implements IRecommendationCallBack, UiLoad.OnRetryClickListener, RecommendationListAdapter.OnRecommendationRecycleClickListener {

    private static final String TAG = "RecomodationFragment";
    RecommendationListAdapter recommendationListAdapter;
    private RecommendationPresenter recommendationPresenter;
    private UiLoad uiLoad;
    private View rootview;


    @Override
    protected View onSubViewLoad(final LayoutInflater layoutInflater, ViewGroup container) {


        if (uiLoad == null) {
            uiLoad = new UiLoad(getContext()) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return creteSuccessView(layoutInflater,container);
                }
            };
        }

        uiLoad.setOnRetryClickListenser(this);
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

        recommendationListAdapter.setonRecommendationRecycleClickListener(this);

        TwinklingRefreshLayout refreshLayout = rootview.findViewById(R.id.recommendation_list_refresh);
//        refreshLayout.setPureScrollModeOn();
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);


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

    @Override
    public void itemClick(int position,Album date) {
        RecommendationContentPresenter.getInstance().setTagAlbun(date);
        Intent intent = new Intent(this.getContext(), RecommendationContentActivity.class);
        startActivity(intent);
    }
}
