package com.a78.com.fmlearn.fragements;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a78.com.fmlearn.R;
import com.a78.com.fmlearn.RecommendationContentActivity;
import com.a78.com.fmlearn.adapters.RecommendationListAdapter;
import com.a78.com.fmlearn.base.BaseFragement;
import com.a78.com.fmlearn.interfaces.ISubCallBack;
import com.a78.com.fmlearn.presenters.RecommendationContentPresenter;
import com.a78.com.fmlearn.presenters.SubPresenter;
import com.a78.com.fmlearn.views.ConfirmDialog;
import com.a78.com.fmlearn.views.UiLoad;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;


public class SubscribeFragment extends BaseFragement implements ISubCallBack, RecommendationListAdapter.OnRecommendationRecycleClickListener, RecommendationListAdapter.OnRecommendationLongClickListener, ConfirmDialog.ConfirmDialogLisenter {


    private RecommendationListAdapter listAdapter;
    private SubPresenter subPresenter;
    private ConfirmDialog confirmDialog;
    private Album mCurrentAlbum = null;
    private UiLoad uiLoad;

    @Override
    protected View onSubViewLoad(LayoutInflater layoutInflater, ViewGroup container) {
        FrameLayout view = (FrameLayout) layoutInflater.inflate(R.layout.fragment_subscribe,container,false);

        if (uiLoad == null) {
            uiLoad = new UiLoad(this.getContext()) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView();
                }

                @Override
                protected View getEmptyView() {
                    View view = super.getEmptyView();
                    TextView emptyText = view.findViewById(R.id.empty_view_text);
                    emptyText.setText("暂无订阅内容,快去订阅吧!" );
                    return view;
                }
            };
            if(uiLoad.getParent() instanceof ViewGroup){
                ((ViewGroup)uiLoad.getParent()).removeView(uiLoad);
            }
            view.addView(uiLoad);
        }


        subPresenter = SubPresenter.getInstance();
        subPresenter.registerViewCallBack(this);
        initView(view);
        return view;
    }

    private View createSuccessView() {
        View successView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_sub_success, null);
        TwinklingRefreshLayout refreshLayout = successView.findViewById(R.id.sub_frage_list_refresh);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);
        RecyclerView subRecyclerView = successView.findViewById(R.id.sub_frage_list);
        subRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        listAdapter = new RecommendationListAdapter();
        listAdapter.setonRecommendationRecycleClickListener(this);
        listAdapter.setonRecommendationLongClickListener(this);
        subRecyclerView.setAdapter(listAdapter);
        return successView;
    }

    private void initView(View view) {

        confirmDialog = new ConfirmDialog(this.getContext());
        confirmDialog.create();
        confirmDialog.setConfirmDialogLisenter(this);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subPresenter.unregisterViewCallback(this);
    }

    @Override
    public void onAddResult(boolean result) {

    }

    @Override
    public void onDeleteResult(boolean result) {
        if (subPresenter != null) {
            subPresenter.getSubscription();
        }
        if (result){
            Toast.makeText(this.getContext(), "取消订阅成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this.getContext(), "取消订阅失败,请稍后再试", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onSubscriptionLoad(List<Album> albums) {
        if (albums.size() > 0){
            uiLoad.updateUi(UiLoad.UiLoadType.SUCCESS);
        }else {
            uiLoad.updateUi(UiLoad.UiLoadType.EMPTY);
        }
        if (listAdapter != null) {
            listAdapter.setAlbumList(albums);
        }
    }

    @Override
    public void onSunFull() {

    }

    @Override
    public void itemClick(int position, Album date) {
        RecommendationContentPresenter.getInstance().setTagAlbun(date);
        Intent intent = new Intent(this.getContext(), RecommendationContentActivity.class);
        startActivity(intent);
    }

    @Override
    public void itemLongClick(Album album) {
        if (confirmDialog != null) {
            confirmDialog.show();
        }
        this.mCurrentAlbum = album;
    }

    @Override
    public void onRethinkClick() {
        //在想想
//        Toast.makeText(this.getContext(), "再想想", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGiveUpClick() {
        //取消订阅
//        Toast.makeText(this.getContext(), "取消", Toast.LENGTH_SHORT).show();
        subPresenter.deleteSubscription(mCurrentAlbum);
    }
}
