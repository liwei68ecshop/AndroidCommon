package com.szy.common.Interface;

import android.graphics.Bitmap;
import android.view.View;

public class SimpleImageLoadingListener implements ImageLoadingListener {
    @Override
    public void onLoadingCancelled(String imageUri, View view) {
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, String failReason) {
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
    }
}
