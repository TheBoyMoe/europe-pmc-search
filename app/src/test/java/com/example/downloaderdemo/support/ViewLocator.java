package com.example.downloaderdemo.support;

import android.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewLocator {

    public static View getView( Fragment fragment, int viewId ) {
        //noinspection ConstantConditions
        return fragment.getView().findViewById( viewId );
    }

    public static TextView getTextView( Fragment fragment, int viewId ) {
        return (TextView) getView( fragment, viewId );
    }

    public static Button getButton( Fragment fragment, int viewId ) {
        return (Button) getView( fragment, viewId );
    }

    public static ImageView getImageView(Fragment fragment, int viewId) {
        return (ImageView) getView(fragment, viewId);
    }

}
