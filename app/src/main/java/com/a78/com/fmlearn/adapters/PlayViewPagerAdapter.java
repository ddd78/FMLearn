package com.a78.com.fmlearn.adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.a78.com.fmlearn.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2020/2/8.
 */

public class PlayViewPagerAdapter extends PagerAdapter {

    private List<Track> trackList = new ArrayList<>();

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_track_viewpager, container, false);
        container.addView(itemView);
        ImageView image = itemView.findViewById(R.id.item_track_image);
        if (!trackList.get(position).getCoverUrlLarge().isEmpty()){
            Picasso.with(container.getContext()).load(trackList.get(position).getCoverUrlLarge()).into(image);
        }
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return trackList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setDate(List<Track> list) {
        trackList.clear();
        trackList.addAll(list);
        notifyDataSetChanged();
    }
}
