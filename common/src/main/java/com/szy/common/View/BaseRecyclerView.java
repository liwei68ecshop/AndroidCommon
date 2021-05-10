package com.szy.common.View;

import android.content.Context;
import android.util.AttributeSet;

import me.zongren.pullablelayout.View.PullableRecyclerView;

/**
 * Created by 宗仁 on 2016/12/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class BaseRecyclerView extends PullableRecyclerView {
    public BaseRecyclerView(Context context) {
        this(context, null, 0);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
