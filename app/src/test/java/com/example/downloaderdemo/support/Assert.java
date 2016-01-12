package com.example.downloaderdemo.support;

import android.view.View;
import android.widget.Button;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class Assert {

    public static void assertViewIsVisible( View view ) {
        assertNotNull( view );
        assertThat( view.getVisibility(), equalTo( View.VISIBLE ) );
    }

    public static void assertButtonIsVisibleAndHasLabel(Button button, int id) {
        assertViewIsVisible(button);
        assertThat(button.getText().toString(),
                equalTo(ResourceLocator.getString(id)));
    }

}
