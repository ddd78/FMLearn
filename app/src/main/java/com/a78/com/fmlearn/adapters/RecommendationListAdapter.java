package com.a78.com.fmlearn.adapters;

import android.support.v7.widget.RecyclerView;
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

    List<Album> albumList = new ArrayList<>();

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
            Picasso.with(itemView.getContext()).load(viewData.getCoverUrlLarge()).into(recommodationImageView);
        }
    }
}
