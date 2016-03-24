package com.example.downloaderdemo.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.model.Article;

public class ArticleDetailFragmentTablet extends ArticleDetailFragment{

    public ArticleDetailFragmentTablet() {}

    public static ArticleDetailFragmentTablet newInstance(Article article) {
        ArticleDetailFragmentTablet fragment = new ArticleDetailFragmentTablet();
        Bundle args = new Bundle();
        args.putParcelable(ARTICLE_ITEM, article);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mArticle = getArguments().getParcelable(ARTICLE_ITEM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int orientation = getActivity().getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT)
            mView = inflater.inflate(R.layout.fragment_detail_tablet_port, container, false);
        else
            mView = inflater.inflate(R.layout.fragment_detail_tablet_land, container, false);
        cacheFragmentElements();
        populateFragmentElements();

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share_tablet, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareArticleIntent());
    }


}
