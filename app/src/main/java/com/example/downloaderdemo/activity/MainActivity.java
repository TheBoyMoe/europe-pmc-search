package com.example.downloaderdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.downloaderdemo.EuroPMCApplication;
import com.example.downloaderdemo.R;
import com.example.downloaderdemo.event.DataModelUpdateEvent;
import com.example.downloaderdemo.event.OnListItemClickEvent;
import com.example.downloaderdemo.event.QueryCompletionEvent;
import com.example.downloaderdemo.fragment.ArticleDetailFragment;
import com.example.downloaderdemo.fragment.ArticleListFragment;
import com.example.downloaderdemo.fragment.ModelFragment;
import com.example.downloaderdemo.model.Article;
import com.example.downloaderdemo.util.Utils;
import com.squareup.otto.Subscribe;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final String MODEL_FRAGMENT_TAG = "model_fragment";
    private boolean mDualPane;
    private ModelFragment mModelFragment;
    private ArticleListFragment mArticleListFragment;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mModelFragment = (ModelFragment) getSupportFragmentManager().findFragmentByTag(MODEL_FRAGMENT_TAG);
        if(mModelFragment == null) {
            mModelFragment = ModelFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(mModelFragment, MODEL_FRAGMENT_TAG)
                    .commit();
        }

        // cache a reference to the list fragment inflated via xml layout
        mArticleListFragment =
                (ArticleListFragment) getSupportFragmentManager().findFragmentById(R.id.article_list_fragment);

        // if both fragments exist, bind a copy of the datacache to the list adapter & update the display
        if(savedInstanceState != null) {
            if(mArticleListFragment != null && mModelFragment != null) {
                mArticleListFragment.setModelDataSet(mModelFragment.getModel());
            }
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
                getSupportFragmentManager().beginTransaction()
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


    // update the list fragments adapter when ever the data model is updated
    @Subscribe
    public void hasDataModelBeenUpdated(DataModelUpdateEvent event) {
        if(event.isDataModelUpToDate()) {
            if(mArticleListFragment != null && mModelFragment != null) {
                Timber.i("Data model updated!");
                // pass a copy of the List of article objects to the ArticleListFragment
                mArticleListFragment.setModelDataSet(mModelFragment.getModel());
            }
        }
    }


    @Subscribe
    public void checkThreadCompletionEvent(QueryCompletionEvent event) {
        if(event.getResultQuery() != null && mModelFragment.getSize() > 20) {
            Utils.showSnackbar(mCoordinatorLayout, String.format("Downloading more articles, %d so far", mModelFragment.getSize() + 20));
        }
    }
}
