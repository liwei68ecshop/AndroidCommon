package com.szy.common.Interface;

/**
 * Created by 宗仁 on 2016/12/8.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public interface OnConfirmDialogClickListener {
    void onConfirmDialogCanceled(int viewType, int position, int extraInfo);

    void onConfirmDialogConfirmed(int viewType, int position, int extraInfo);
}