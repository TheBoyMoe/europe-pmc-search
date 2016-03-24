package com.example.downloaderdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.fragment.ArticleDetailFragmentPhone;
import com.example.downloaderdemo.fragment.ArticleDetailFragmentTablet;
import com.example.downloaderdemo.model.Article;

public class DetailActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Article article = getIntent().getParcelableExtra(ArticleDetailFragmentTablet.ARTICLE_ITEM);

        ArticleDetailFragmentPhone phoneFragment = (ArticleDetailFragmentPhone) getSupportFragmentManager().findFragmentById(R.id.fragment_detail_phone_container);
        if(phoneFragment == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail_phone_container, ArticleDetailFragmentPhone.newInstance(article))
                    .commit();
        }

    }

}
