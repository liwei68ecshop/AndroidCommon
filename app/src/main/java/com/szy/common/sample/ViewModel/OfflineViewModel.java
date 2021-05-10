package com.szy.common.sample.ViewModel;

import com.szy.common.Constant.ViewType;
import com.szy.common.ViewModel.BaseOfflineViewModel;

/**
 * Created by 宗仁 on 2016/12/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class OfflineViewModel extends BaseOfflineViewModel {
    public OfflineViewModel() {
        super();
        this.type = ViewType.VIEW_TYPE_OFFLINE.ordinal();
    }
}
