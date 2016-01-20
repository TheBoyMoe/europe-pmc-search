package com.example.downloaderdemo;

import android.app.Fragment;
import android.os.Build;

import com.example.downloaderdemo.fragment.DownloadFragment;
import com.example.downloaderdemo.fragment.ModelFragment;
import com.example.downloaderdemo.activity.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainActivityTest {


    private MainActivity mActivity;

    @Before
    public void setUp() throws Exception {
        mActivity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertNotNull(mActivity);
    }

    @Test
    public void shouldHaveModelFragment() throws Exception {
        Fragment modelFragment =
            mActivity.getFragmentManager().findFragmentByTag(MainActivity.MODEL_FRAGMENT_TAG);
        assertNotNull(modelFragment);
        assertTrue(modelFragment instanceof ModelFragment);
    }

    @Test
    public void shouldHaveDownloaderFragmentt() throws Exception {
        Fragment downloadFragment = mActivity.getFragmentManager().findFragmentById(R.id.list_fragment_container);
        assertNotNull(downloadFragment);
        assertTrue(downloadFragment instanceof DownloadFragment);
    }
}
