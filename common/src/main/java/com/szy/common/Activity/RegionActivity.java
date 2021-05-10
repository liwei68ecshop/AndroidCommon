package com.szy.common.Activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.szy.common.Fragment.RegionFragment;
import com.szy.common.R;

/**
 * Created by 宗仁 on 16/7/18.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class RegionActivity extends CommonActivity {
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;
    View view;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @Override
    protected RegionFragment createFragment() {
        return new RegionFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutId = R.layout.activity_region;
        super.onCreate(savedInstanceState);

        view = (View)findViewById(R.id.closeView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        TypedArray activityStyle = getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId,
                new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }
}





