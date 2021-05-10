package com.szy.common.sample.Util;

import android.view.View;

import com.szy.common.Constant.ViewType;
import com.szy.common.Util.CommonUtils;

/**
 * Created by 宗仁 on 2016/12/16.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class Utils extends CommonUtils {
    public static ViewType getViewTypeOfTag(View view) {
        return ViewType.valueOf(CommonUtils.getViewTypeIntegerOfTag(view));
    }

    public static void setViewTypeForTag(View view, ViewType viewType) {
        CommonUtils.setViewTypeIntegerForTag(view, viewType.ordinal());
    }

}
