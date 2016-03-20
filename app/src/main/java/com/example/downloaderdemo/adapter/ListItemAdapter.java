package com.example.downloaderdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.model.Article;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder>{

    private List<Article> mList;
    private Context mContext;

    public ListItemAdapter(Context context, List<Article> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        holder.bindArticleItem(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clearAll() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Article> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

}
