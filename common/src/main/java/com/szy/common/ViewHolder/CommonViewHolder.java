package com.szy.common.ViewHolder;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by zongren on 16/5/4.
 * 秦皇岛商之翼网络科技有限公司 All Rights Reserved.
 */
public abstract class CommonViewHolder {
    public View view;

    public CommonViewHolder() {

    }

    public CommonViewHolder(View view) {
        setView(view);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
        ButterKnife.bind(this, this.view);
    }
}
