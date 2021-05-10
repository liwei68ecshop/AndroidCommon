package com.szy.common.Adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.szy.common.Interface.EmptyViewListener;
import com.szy.common.ViewModel.BaseEmptyViewModel;

/**
 * Created by 宗仁 on 2016/12/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter {
    int BASE_EMPTY_VIEW_TYPE = -10;

    public abstract void hideEmptyView();

    public abstract void showEmptyView(BaseEmptyViewModel model, EmptyViewListener listener);
}
