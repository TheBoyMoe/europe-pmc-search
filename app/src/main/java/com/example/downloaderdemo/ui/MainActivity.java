package com.example.downloaderdemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.fragment.DownloadFragment;
import com.example.downloaderdemo.fragment.ModelFragment;

public class MainActivity extends AppCompatActivity {

    public static final String MODEL_FRAGMENT_TAG = "model_fragment";

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

    }
}
