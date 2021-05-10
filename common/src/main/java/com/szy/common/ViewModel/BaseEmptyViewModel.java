package com.szy.common.ViewModel;

import com.szy.common.R;

/**
 * Created by 宗仁 on 2016/12/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class BaseEmptyViewModel {
    public int title;
    public int subtitle;
    public int buttonTitle;
    public int image;
    public int type;

    public BaseEmptyViewModel() {
        this.title = R.string.emptyList;
        this.image = R.mipmap.bg_public;
    }
}
