package com.example.downloaderdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.fragment.ArticleDetailFragment;
import com.example.downloaderdemo.model.Article;

public class DetailActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        Article article = getIntent().getParcelableExtra(ArticleDetailFragment.ARTICLE_ITEM);

        ArticleDetailFragment articleDetailFragment = (ArticleDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
        if(articleDetailFragment == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, ArticleDetailFragment.newInstance(article))
                    .commit();
        }

    }

}
