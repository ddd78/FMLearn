package com.a78.com.fmlearn.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a78.com.fmlearn.R;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2020/2/15.
 */

public class SearchRecomedAdapter extends RecyclerView.Adapter<SearchRecomedAdapter.InnerHolder> {

    private static final String TAG = "SearchRecomedAdapter";

    private List<QueryResult> results = new ArrayList<>();
    private ItemClick itemClick;

    @Override
    public InnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_recomend, parent, false);

        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(InnerHolder holder, final int position) {
        TextView titleText = holder.itemView.findViewById(R.id.item_search_recomend_title_text);
        titleText.setText(results.get(position).getKeyword());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemClick(results.get(position).getKeyword());
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void upDate(List<QueryResult> keyWordList) {
        results.clear();
        results.addAll(keyWordList);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(View itemView) {
            super(itemView);
        }
    }

    public void setItemClickLisenter(ItemClick click){
        this.itemClick = click;
    }

    public interface ItemClick{
        void onItemClick(String keyword);
    }
}
