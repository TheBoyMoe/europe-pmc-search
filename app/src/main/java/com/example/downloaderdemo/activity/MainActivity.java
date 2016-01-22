package com.example.downloaderdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.downloaderdemo.EuroPMCApplication;
import com.example.downloaderdemo.R;
import com.example.downloaderdemo.activity.DetailActivity;
import com.example.downloaderdemo.event.OnListItemClickEvent;
import com.example.downloaderdemo.fragment.DetailFragment;
import com.example.downloaderdemo.fragment.DownloadFragment;
import com.example.downloaderdemo.fragment.ModelFragment;
import com.example.downloaderdemo.model.Article;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {

    public static final String MODEL_FRAGMENT_TAG = "model_fragment";
    private boolean mDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ModelFragment modelFragment =
                (ModelFragment) getFragmentManager().findFragmentByTag(MODEL_FRAGMENT_TAG);
        if(modelFragment == null) {
            getFragmentManager().beginTransaction()
                    .add(ModelFragment.newInstance(), MODEL_FRAGMENT_TAG)
                    .commit();
        }

        DownloadFragment downloadFragment =
                (DownloadFragment) getFragmentManager().findFragmentById(R.id.list_fragment_container);
        if(downloadFragment == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_container, DownloadFragment.newInstance())
                    .commit();
        }

        if(downloadFragment != null && modelFragment != null)
            downloadFragment.setModelDataSet(modelFragment.getModel());

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
                        .replace(R.id.detail_fragment_container, DetailFragment.newInstance(article))
                        .commit();
            } else {
                // on phones
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailFragment.ARTICLE_ITEM, article);
                startActivity(intent);
            }
        }
    }

}