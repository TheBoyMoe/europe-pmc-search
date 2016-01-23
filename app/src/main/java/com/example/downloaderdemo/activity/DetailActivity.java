package com.example.downloaderdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.fragment.ArticleDetailFragment;
import com.example.downloaderdemo.model.Article;

public class DetailActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Article article = getIntent().getParcelableExtra(ArticleDetailFragment.ARTICLE_ITEM);

        ArticleDetailFragment articleDetailFragment = (ArticleDetailFragment) getFragmentManager().findFragmentById(R.id.detail_fragment_container);
        if(articleDetailFragment == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, ArticleDetailFragment.newInstance(article))
                    .commit();
        }

    }

}
