package com.a78.com.fmlearn.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a78.com.fmlearn.R;
import com.a78.com.fmlearn.base.BaseApplication;
import com.a78.com.fmlearn.views.PlayPopView;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2020/2/10.
 */

public class PopPlayAdapter extends RecyclerView.Adapter<PopPlayAdapter.InnerHoler> {

    private List<Track> trackList = new ArrayList<>();

    private int currentPosition = 1;
    private PlayPopView.PlayPopViewClickListener popListener;


    @Override
    public InnerHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pop_play, parent, false);

        return new InnerHoler(view);
    }

    @Override
    public void onBindViewHolder(InnerHoler holder, final int position) {
        holder.itemView.setTag(position);
        TextView titleText = holder.itemView.findViewById(R.id.item_pop_play_title_text);
        ImageView localImage = holder.itemView.findViewById(R.id.item_pop_play_local_image);
        titleText.setText(trackList.get(position).getTrackTitle());
        if (position == currentPosition){
            titleText.setTextColor(BaseApplication.getsContext().getResources().getColor(R.color.maincolor));
            localImage.setVisibility(View.VISIBLE);
        }else {
            titleText.setTextColor(BaseApplication.getsContext().getResources().getColor(R.color.OneWord));
            localImage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popListener != null) {
                    popListener.playPopViewItemClick(position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public void setCurrentPosition(int position) {
        currentPosition = position;
        notifyDataSetChanged();
    }

    public void setPlayPopViewItemClick(PlayPopView.PlayPopViewClickListener popClickLIstener) {
        this.popListener = popClickLIstener;
    }

    public class InnerHoler extends RecyclerView.ViewHolder {
        public InnerHoler(View itemView) {
            super(itemView);
        }
    }

    public void setDate(List<Track> list) {
        trackList.clear();
        trackList.addAll(list);
        notifyDataSetChanged();
    }
}
