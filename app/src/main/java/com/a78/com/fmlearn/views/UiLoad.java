package com.a78.com.fmlearn.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.a78.com.fmlearn.R;
import com.a78.com.fmlearn.base.BaseApplication;

/**
 * Created by home on 2020/2/1.
 */

public abstract class UiLoad extends FrameLayout {


    private View loadingView;
    private View successView;
    private View emptyView;
    private View errorView;
    private OnRetryClickListener onRetryClickLisenter = null;

    public enum UiLoadType{
        LOADING, SUCCESS, EMPTY, ERROR, NONE
    }

    public UiLoadType uiLoadType = UiLoadType.NONE;

    public UiLoad(@NonNull Context context) {
        this(context, null);
    }

    public UiLoad(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UiLoad(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void updateUi(UiLoadType uiLoadType){
        this.uiLoadType = uiLoadType;
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUiByType();
            }
        });
    }

    private void init() {
        switchUiByType();
    }

    private void switchUiByType() {

        if (loadingView == null){
            loadingView = getLoadingView();
            addView(loadingView);
        }
        loadingView.setVisibility(uiLoadType == UiLoadType.LOADING ? VISIBLE : GONE);

        if (successView == null){
            successView = getSuccessView(this);
            addView(successView);
        }
        successView.setVisibility(uiLoadType == UiLoadType.SUCCESS ? VISIBLE : GONE);

        if (emptyView == null){
            emptyView = getEmptyView();
            addView(emptyView);
        }
        emptyView.setVisibility(uiLoadType == UiLoadType.EMPTY ? VISIBLE : GONE);

        if (errorView == null){
            errorView = getErrorView();
            addView(errorView);
        }
        errorView.setVisibility(uiLoadType == UiLoadType.ERROR ? VISIBLE : GONE);




    }

    protected View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view,this,false);
    }

    protected View getErrorView() {
        View errorView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_error_view,this,false);

        errorView.findViewById(R.id.net_error_linear).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRetryClickLisenter != null){
                    onRetryClickLisenter.OnRetryClick();
                }
            }
        });


        return errorView;
    }

    protected abstract View getSuccessView(ViewGroup container);
//    private View getSuccessView() {
//        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view,this,false);
//    }


    protected View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view,this,false);
    }

    public void setOnRetryClickListenser(OnRetryClickListener onRetryClickLisenter){
        this.onRetryClickLisenter = onRetryClickLisenter;
    }

    public interface OnRetryClickListener{
        void OnRetryClick();
    }

}
