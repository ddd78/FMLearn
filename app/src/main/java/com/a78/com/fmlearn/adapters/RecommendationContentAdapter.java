package com.a78.com.fmlearn.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a78.com.fmlearn.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2020/2/5.
 */

public class RecommendationContentAdapter extends RecyclerView.Adapter<RecommendationContentAdapter.InnerHolder> {

    private SimpleDateFormat upDateSimpleDateFormate = new SimpleDateFormat("yyy-MM-dd");

    private List<Track> trackList = new ArrayList<>();
    private ContentItemClick itemClick = null;
    private ContentItemLongClickListener contentItemLongClickListener = null;

    @Override
    public InnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommodation_content,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InnerHolder holder, int position) {
        View itemView = holder.itemView;

        initView(itemView, position);
    }

    private void initView(View itemView, final int position) {
        TextView listOrderText = itemView.findViewById(R.id.recommendation_content_list_order);
        TextView upDataText = itemView.findViewById(R.id.recommendation_content_date_text);
        TextView titleText = itemView.findViewById(R.id.item_recommendation_content_title_text);
        TextView readcountText = itemView.findViewById(R.id.item_recommendation_content_readcount_text);

        listOrderText.setText((position+1) + "");
        titleText.setText(trackList.get(position).getTrackTitle());
        readcountText.setText(trackList.get(position).getPlayCount() + "");
        String updateTime = upDateSimpleDateFormate.format(trackList.get(position).getUpdatedAt());
        upDataText.setText(updateTime);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onContentItemClick(trackList,position);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                contentItemLongClickListener.onContentItemLongClick(trackList.get(position));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public void update(List<Track> tracks) {
        trackList.clear();
        trackList.addAll(tracks);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(View itemView) {
            super(itemView);
        }
    }

    public void setContentItemClick(ContentItemClick contentItemClick){
        this.itemClick = contentItemClick;
    }

    public interface ContentItemClick{
        void onContentItemClick(List<Track> trackList, int position);
    }

    public void setContentItemLongClickListener(ContentItemLongClickListener longClickListener){
        this.contentItemLongClickListener = longClickListener;
    }

    public interface ContentItemLongClickListener{
        void onContentItemLongClick(Track  track);
    }
}
