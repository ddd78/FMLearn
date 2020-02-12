package com.a78.com.fmlearn.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.a78.com.fmlearn.R;
import com.a78.com.fmlearn.adapters.PopPlayAdapter;
import com.a78.com.fmlearn.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

/**
 * Created by home on 2020/2/9.
 */

public class PlayPopView extends PopupWindow {

    private static final String TAG = "PlayPopView";

    private final View view;
    private TextView closeButton;
    private PopPlayAdapter popPlayAdapter;
    private RecyclerView playRecyclerView;
    private PlayPopViewClickListener popClickLIstener;
    private LinearLayout switchLiear;
    private PlayPopViewActionListener playPopViewActionListener;
    private TextView switchText;
    private ImageView switchButton;
    private TextView orderText;
    private ImageView orderImage;
    private LinearLayout orderLinear;

    public PlayPopView() {
        super(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);

        view = LayoutInflater.from(BaseApplication.getsContext()).inflate(R.layout.pop_play_list, null, false);
        setContentView(view);

        setAnimationStyle(R.style.pop_animation);

        initView();
        initEvent();
    }

    private void initEvent() {
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        switchLiear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPopViewActionListener.onPlayPopViewSwitchClick();
            }
        });
        orderLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPopViewActionListener.onPlayPopViewOrderClick();
            }
        });
    }

    private void initView() {
        closeButton = view.findViewById(R.id.pop_play_list_close_text);
        playRecyclerView = view.findViewById(R.id.pop_play_list_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getsContext());
        playRecyclerView.setLayoutManager(linearLayoutManager);
        popPlayAdapter = new PopPlayAdapter();
        playRecyclerView.setAdapter(popPlayAdapter);
        switchLiear = view.findViewById(R.id.pop_play_list_switch_linear);
        switchButton = view.findViewById(R.id.pop_play_list_switch_button);
        switchText = view.findViewById(R.id.pop_play_list_switch_text);
        orderLinear = view.findViewById(R.id.pop_play_list_order_linear);
        orderImage = view.findViewById(R.id.pop_play_list_order_image);
        orderText = view.findViewById(R.id.pop_play_list_order_text);

    }

    public void setPopPlayAdapterDate(List<Track> list){
        if (popPlayAdapter != null) {
            popPlayAdapter.setDate(list);
        }
    }

    public void setPopPlayAdapterCurrentPosition(int position){
        if (popPlayAdapter != null) {
            popPlayAdapter.setCurrentPosition(position);
        }
        playRecyclerView.scrollToPosition(position);
    }



    public void setPlayPopViewClick(PlayPopViewClickListener popClickLIstener){
        popPlayAdapter.setPlayPopViewItemClick(popClickLIstener);
    }

    public void upDatePlayMode(XmPlayListControl.PlayMode playMode) {
        int resid = R.mipmap.play_list;
        String sText = "顺序播放";
        switch (playMode){
            case PLAY_MODEL_LIST:
                sText = "顺序播放";
                resid = R.mipmap.play_list;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                sText = "单曲循环";
                resid = R.mipmap.play_simgel_loop;
                break;
            case PLAY_MODEL_RANDOM:
                sText = "随机播放";
                resid = R.mipmap.play_random;
                break;
            case PLAY_MODEL_LIST_LOOP:
                sText = "列表循环";
                resid = R.mipmap.play_loop;
                break;
        }
        if (switchButton != null){
            switchButton.setImageResource(resid);
        }
        if (switchText != null){
            switchText.setText(sText);
        }

    }

    public void updatePlayOrder(boolean isOrder) {
        if (isOrder){
            orderImage.setImageResource(R.mipmap.up_order_list);
            orderText.setText("正序");
        }else {
            orderImage.setImageResource(R.mipmap.down_order_list);
            orderText.setText("逆序");
        }
    }

    public interface PlayPopViewClickListener{
        void playPopViewItemClick(int position);
    }

    public void setPlayPopViewActionListener(PlayPopViewActionListener listener){
        this.playPopViewActionListener = listener;
    }

    public interface PlayPopViewActionListener {
        void onPlayPopViewSwitchClick();

        void onPlayPopViewOrderClick();
    }
}
