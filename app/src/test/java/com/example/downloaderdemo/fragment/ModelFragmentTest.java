package com.example.downloaderdemo.fragment;


import android.app.Fragment;
import android.os.Build;

import com.example.downloaderdemo.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.util.FragmentTestUtil.startFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ModelFragmentTest {

    private Fragment mModelFragment;

    @Before
    public void setUp() throws Exception {
        mModelFragment = ModelFragment.newInstance();
        startFragment(mModelFragment);
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertNotNull(mModelFragment);

    }
}