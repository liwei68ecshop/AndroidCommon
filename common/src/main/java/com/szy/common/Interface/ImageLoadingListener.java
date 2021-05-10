package com.szy.common.Interface;

import android.graphics.Bitmap;
import android.view.View;

public interface ImageLoadingListener {

    void onLoadingCancelled(String imageUri, View view);

    void onLoadingComplete(String imageUri, View view, Bitmap loadedImage);

    void onLoadingFailed(String imageUri, View view, String failReason);

    void onLoadingStarted(String imageUri, View view);
}
