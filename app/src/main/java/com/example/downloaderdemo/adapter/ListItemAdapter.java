package com.example.downloaderdemo.adapter;

import android.content.Context;
import android.os.Bundle;
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
    private ItemChoiceManager mItemChoiceManager;

    public ListItemAdapter(Context context, List<Article> list, int choiceMode) {
        mContext = context;
        mList = list;
        mItemChoiceManager = new ItemChoiceManager(this);
        mItemChoiceManager.setChoiceMode(choiceMode);
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ListItemViewHolder(view, mItemChoiceManager);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        holder.bindArticleItem(mList.get(position), position);
        mItemChoiceManager.onBindViewHolder(holder, position);
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

    // helper methods which forward on the calls to the ItemChoiceManager
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mItemChoiceManager.onRestoreInstanceState(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mItemChoiceManager.onSaveInstanceState(outState);
    }

    public int getSelectedItemPosition() {
        return mItemChoiceManager.getSelectedItemPosition();
    }

    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof ListItemViewHolder) {
            ListItemViewHolder gvh = (ListItemViewHolder) viewHolder;
            gvh.onClick(gvh.itemView);
        }
    }

    public void setInitialView(int position) {
        mItemChoiceManager.setInitialCheckedState(position);
    }

}
