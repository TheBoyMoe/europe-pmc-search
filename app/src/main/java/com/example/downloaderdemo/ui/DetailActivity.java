package com.example.downloaderdemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.fragment.DetailFragment;
import com.example.downloaderdemo.model.Journal;

public class DetailActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Journal journal = getIntent().getParcelableExtra(DetailFragment.JOURNAL_ITEM);

        DetailFragment detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.detail_fragment_container);
        if(detailFragment == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, DetailFragment.newInstance(journal))
                    .commit();
        }

    }

}
