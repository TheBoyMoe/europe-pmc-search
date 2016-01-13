package com.example.downloaderdemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.fragment.ModelFragment;

public class MainActivity extends AppCompatActivity {

    public static final String MODEL_FRAGMENT_TAG = "model_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getFragmentManager().findFragmentByTag(MODEL_FRAGMENT_TAG) == null) {
            getFragmentManager().beginTransaction()
                    .add(ModelFragment.newInstance(), MODEL_FRAGMENT_TAG)
                    .commit();
        }

    }
}
