package com.example.downloaderdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.model.Article;

public class ArticleDetailFragmentPhone extends ArticleDetailFragment{

    public ArticleDetailFragmentPhone() {}

    public static ArticleDetailFragmentPhone newInstance(Article article) {
        ArticleDetailFragmentPhone fragment = new ArticleDetailFragmentPhone();
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
        mView = inflater.inflate(R.layout.fragment_detail_phone_container, container, false);
        cacheFragmentElements();
        populateFragmentElements();

        // set toolbar
        Toolbar toolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            toolbar.setNavigationIcon(R.drawable.ic_action_back_black);
        }

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share_phone, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareArticleIntent());
    }


}
