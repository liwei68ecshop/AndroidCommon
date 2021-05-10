package com.szy.common.ViewModel;

import com.szy.common.R;

/**
 * Created by 宗仁 on 2016/12/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class BaseOfflineViewModel extends BaseEmptyViewModel {

    public BaseOfflineViewModel() {
        this.image = R.mipmap.bg_public;
        this.title = R.string.requestFailed;
        this.subtitle = R.string.pleaseCheckYourNetwork;
        this.buttonTitle = R.string.reload;
        this.type = -1;
    }
}
