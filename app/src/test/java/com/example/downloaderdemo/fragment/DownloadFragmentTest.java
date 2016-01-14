package com.example.downloaderdemo.fragment;


import android.app.Fragment;
import android.os.Build;
import android.widget.ListView;

import com.example.downloaderdemo.BuildConfig;
import com.example.downloaderdemo.R;
import com.example.downloaderdemo.support.ViewLocator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.example.downloaderdemo.support.Assert.assertViewIsVisible;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.util.FragmentTestUtil.startFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class DownloadFragmentTest {


    private Fragment mFragment;

    @Before
    public void setUp() throws Exception {
        mFragment = DownloadFragment.newInstance();
        startFragment(mFragment);
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertNotNull(mFragment);
    }

    @Test
    public void shouldHaveDisplay() throws Exception {
        ListView listView = (ListView) ViewLocator.getView(mFragment, R.id.list_view);
        assertViewIsVisible(listView);
    }


}
