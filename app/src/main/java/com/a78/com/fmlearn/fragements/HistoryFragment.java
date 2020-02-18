package com.a78.com.fmlearn.fragements;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a78.com.fmlearn.PlayActivity;
import com.a78.com.fmlearn.R;
import com.a78.com.fmlearn.adapters.RecommendationContentAdapter;
import com.a78.com.fmlearn.base.BaseFragement;
import com.a78.com.fmlearn.interfaces.IHistoryCallBack;
import com.a78.com.fmlearn.presenters.HistoryPresenter;
import com.a78.com.fmlearn.presenters.PlayPresenter;
import com.a78.com.fmlearn.views.ConfirmCheckDialog;
import com.a78.com.fmlearn.views.ConfirmDialog;
import com.a78.com.fmlearn.views.UiLoad;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;


public class HistoryFragment extends BaseFragement implements IHistoryCallBack, RecommendationContentAdapter.ContentItemClick, RecommendationContentAdapter.ContentItemLongClickListener, ConfirmCheckDialog.ConfirmCheckDialogAction {

    private static final String TAG = "HistoryFragment";
    private UiLoad uiLoad = null;
    private RecommendationContentAdapter adapter = null;
    private HistoryPresenter historyPresenter = null;
    private Track mCurrentTrack = null;

    @Override
    protected View onSubViewLoad(LayoutInflater layoutInflater, ViewGroup container) {
        FrameLayout view = (FrameLayout) layoutInflater.inflate(R.layout.fragment_history,container,false);
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
                    emptyText.setText("历史记录为空!");
                    return view;
                }
            };
        }else {
            if (uiLoad.getParent() instanceof ViewGroup) {
                ((ViewGroup) uiLoad.getParent()).removeView(uiLoad);
            }
        }

        view.addView(uiLoad);

        historyPresenter = HistoryPresenter.getInstance();
        historyPresenter.registerViewCallBack(this);
        historyPresenter.lishHistory();
        return view;
    }

    private View createSuccessView() {
        View successView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_history, null);
        TwinklingRefreshLayout refreshLayout = successView.findViewById(R.id.history_frage_list_refresh);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);

        RecyclerView historyRecyclerView = successView.findViewById(R.id.history_frage_list);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new RecommendationContentAdapter();
        historyRecyclerView.setAdapter(adapter);
        adapter.setContentItemClick(this);
        adapter.setContentItemLongClickListener(this);
        return successView;
    }

    @Override
    public void onHistoriesLoad(List<Track> tracks) {
        if (tracks == null || tracks.size() == 0) {
            uiLoad.updateUi(UiLoad.UiLoadType.EMPTY);
        }else{
            uiLoad.updateUi(UiLoad.UiLoadType.SUCCESS);
        }
        adapter.update(tracks);
    }

    @Override
    public void onContentItemClick(List<Track> trackList, int position) {
        PlayPresenter playPresenter = PlayPresenter.getPlayPresenter();
        playPresenter.setPlayList(trackList,position);

        Intent intent = new Intent(this.getContext(),PlayActivity.class);
        startActivity(intent);
    }

    @Override
    public void onContentItemLongClick(Track track) {
        this.mCurrentTrack = track;
        ConfirmCheckDialog dialog = new ConfirmCheckDialog(this.getContext());
        dialog.setConfirmCheckDialogAction(this);
        dialog.show();
    }

    @Override
    public void onCancelClick() {

    }

    @Override
    public void onConfirClick(boolean isCheck) {
        if (isCheck){
            if (historyPresenter != null) {
                historyPresenter.clearHistories();
            }
        }else {
            if (historyPresenter != null) {
                if (mCurrentTrack != null) {
                    historyPresenter.deleteHistory(mCurrentTrack);
                }
            }
        }
    }
}
