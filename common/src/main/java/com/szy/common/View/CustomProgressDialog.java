package com.szy.common.View;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.szy.common.R;

/**
 * Created by 宗仁 on 16/8/27.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class CustomProgressDialog extends Dialog {
    private ImageView mImageView;
    private TextView mTextView;
    private RotateAnimation rotateAnimation;

    public CustomProgressDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
        setContentView(R.layout.progressdialog_style1);
        mImageView = (ImageView) findViewById(R.id.image_loading_view);
        mTextView = (TextView) findViewById(R.id.text_loading_msg);
        getWindow().getAttributes().gravity = Gravity.CENTER;

        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                getContext(), R.anim.rotate_animation);
    }

    public CustomProgressDialog setMessage(String strMessage) {
        if (mTextView != null) {
            mTextView.setText(strMessage);
        }
        return this;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mImageView.startAnimation(rotateAnimation);
    }
}
