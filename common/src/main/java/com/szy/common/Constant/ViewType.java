package com.szy.common.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 宗仁 on 16/8/1.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public enum ViewType {
    VIEW_TYPE_DIALOG_CANCEL,
    VIEW_TYPE_DIALOG_CONFIRM,
    VIEW_TYPE_REGION,
    VIEW_TYPE_PLUS,
    VIEW_TYPE_SELECTED_REGION,
    VIEW_TYPE_CLOSE,
    VIEW_TYPE_EMPTY,
    VIEW_TYPE_OFFLINE,;

    private static Map<Integer, ViewType> mMap = new HashMap<>();

    static {
        for (ViewType viewType : ViewType.values()) {
            mMap.put(viewType.ordinal(), viewType);
        }
    }

    public int getValue() {
        return this.ordinal();
    }

    public static ViewType valueOf(int value) {
        return mMap.get(value);
    }
}
