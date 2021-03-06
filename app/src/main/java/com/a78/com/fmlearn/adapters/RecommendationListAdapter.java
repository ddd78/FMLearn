package com.a78.com.fmlearn.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a78.com.fmlearn.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2020/1/17.
 */

public class RecommendationListAdapter extends RecyclerView.Adapter<RecommendationListAdapter.InnerHolder> {

    private static final String TAG = "RecommendationListAdapt";

    private OnRecommendationRecycleClickListener mOnRecommendationRecycleClickListener = null;

    List<Album> albumList = new ArrayList<>();
    private OnRecommendationLongClickListener onRecommendationLongClickListener = null;

    public RecommendationListAdapter() {
    }

    public void setAlbumList(List<Album> albumList) {
        if (albumList != null) {
            this.albumList.clear();
            this.albumList.addAll(albumList);
        }
        notifyDataSetChanged();
    }

    @Override
    public InnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommdation_list,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InnerHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setViewData(albumList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d(TAG, "onClick: " + view.getTag());
                if (mOnRecommendationRecycleClickListener != null){
                    int position = (int) view.getTag();
                    mOnRecommendationRecycleClickListener.itemClick(position,albumList.get(position));
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onRecommendationLongClickListener != null) {
                    int position = (int) view.getTag();
                    onRecommendationLongClickListener.itemLongClick(albumList.get(position));
                }
                return false;
            }
        });
    }



    @Override
    public int getItemCount() {
        if (albumList != null){
            return albumList.size();
        }
        return 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(View itemView) {
            super(itemView);
        }

        public void setViewData(Album viewData) {
            ImageView recommodationImageView = (ImageView) itemView.findViewById(R.id.item_recommodation_image);
            TextView recommodationTitleTextView = itemView.findViewById(R.id.item_recommodation_title_text);
            TextView recommodationContextTextView = itemView.findViewById(R.id.item_recommodation_context_text);
            TextView recommodationPlayNumTextView = itemView.findViewById(R.id.item_recommodation_play_num_text);
            TextView recommodationSoundNumTextView = itemView.findViewById(R.id.item_recommodation_sound_num_text);

            recommodationTitleTextView.setText(viewData.getAlbumTitle());
            recommodationContextTextView.setText(viewData.getAlbumIntro());
            recommodationPlayNumTextView.setText(viewData.getPlayCount() + "");
            recommodationSoundNumTextView.setText(viewData.getIncludeTrackCount() + "");
            if (!TextUtils.isEmpty(viewData.getCoverUrlLarge())){
                Picasso.with(itemView.getContext()).load(viewData.getCoverUrlLarge()).into(recommodationImageView);
            }
        }
    }

    public void setonRecommendationRecycleClickListener(OnRecommendationRecycleClickListener clickListener){
        this.mOnRecommendationRecycleClickListener = clickListener;
    }

    public interface OnRecommendationRecycleClickListener{
        void itemClick(int position,Album date);
    }


    public void setonRecommendationLongClickListener(OnRecommendationLongClickListener listener){
        this.onRecommendationLongClickListener = listener;
    }

    public interface OnRecommendationLongClickListener{
        void itemLongClick(Album album);
    }
}
