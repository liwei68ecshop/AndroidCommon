package com.szy.common.sample.Util;

import android.view.ViewGroup;

import com.szy.common.Interface.EmptyViewListener;
import com.szy.common.Util.BaseEmptyViewUtils;
import com.szy.common.sample.ViewModel.EmptyViewModel;
import com.szy.common.sample.ViewModel.OfflineViewModel;

/**
 * Created by 宗仁 on 2016/12/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class EmptyViewUtils extends BaseEmptyViewUtils {

    public static void showEmptyView(ViewGroup viewGroup, EmptyViewListener listener) {
        BaseEmptyViewUtils.showEmptyView(viewGroup, listener, new EmptyViewModel());
    }

    public static void showOfflineView(ViewGroup viewGroup, EmptyViewListener listener) {
        BaseEmptyViewUtils.showEmptyView(viewGroup, listener, new OfflineViewModel());
    }
}
