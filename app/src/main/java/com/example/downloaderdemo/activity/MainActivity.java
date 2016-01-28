package com.example.downloaderdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.downloaderdemo.EuroPMCApplication;
import com.example.downloaderdemo.R;
import com.example.downloaderdemo.event.DataModelUpdateEvent;
import com.example.downloaderdemo.event.OnListItemClickEvent;
import com.example.downloaderdemo.event.RefreshAdapterEvent;
import com.example.downloaderdemo.fragment.ArticleListFragment;
import com.example.downloaderdemo.fragment.ArticleDetailFragment;
import com.example.downloaderdemo.fragment.ModelFragment;
import com.example.downloaderdemo.model.Article;
import com.squareup.otto.Subscribe;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final String MODEL_FRAGMENT_TAG = "model_fragment";
    private boolean mDualPane;
    private ModelFragment mModelFragment;
    private ArticleListFragment mArticleListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mModelFragment =
                (ModelFragment) getFragmentManager().findFragmentByTag(MODEL_FRAGMENT_TAG);

        if(mModelFragment == null) {

            mModelFragment = ModelFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(mModelFragment, MODEL_FRAGMENT_TAG)
                    .commit();
        }

        mArticleListFragment =
                (ArticleListFragment) getFragmentManager().findFragmentById(R.id.list_fragment_container);

        if(mArticleListFragment == null) {

            mArticleListFragment = ArticleListFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_container, mArticleListFragment)
                    .commit();
        }

        if(mArticleListFragment != null && mModelFragment != null) {
            mArticleListFragment.setModelDataSet(mModelFragment.getModel());
        }

        // determine if there is a frame in which to embed the detail fragment, eg on tablet
        View detailPane = findViewById(R.id.detail_fragment_container);
        mDualPane = detailPane != null && detailPane.getVisibility() == View.VISIBLE;

    }


    @Override
    protected void onResume() {
        super.onResume();
        EuroPMCApplication.getInstance().getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EuroPMCApplication.getInstance().getBus().unregister(this);
    }

    // handle list item clicks
    @Subscribe
    public void onListItemClickEvent(OnListItemClickEvent event) {
        Article article = event.getArticle();
        if(article != null) {
            // on tablets
            if(mDualPane) {
                // swap the current fragment
                getFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, ArticleDetailFragment.newInstance(article))
                        .commit();
            } else {
                // on phones
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(ArticleDetailFragment.ARTICLE_ITEM, article);
                startActivity(intent);
            }
        }
    }


    // notify the list fragment when the data model has been updated
    @Subscribe
    public void hasDataModelBeenUpdated(DataModelUpdateEvent event) {
        if(event.isDataModelUpToDate()) {
            if(mArticleListFragment != null && mModelFragment != null) {
                Timber.i("Data model updated!");
                // pass a copy of the List of article objects to the ArticleListFragment
                mArticleListFragment.setModelDataSet(mModelFragment.getModel());

                // notify the list fragment to refresh the view
                EuroPMCApplication.postToBus(new RefreshAdapterEvent(true));
            }
        }
    }


}
