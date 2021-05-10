package com.szy.common.Interface;

import android.os.Parcelable;

/**
 * Created by zongren on 16/5/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public interface ListViewItem extends Parcelable {
    /**
     * Get the bread crumb.
     * Used as bread crumb title.
     *
     * @return {String} Bread crumb.
     */
    String getListItemBreadCrumb();

    /**
     * Get the title for list view.
     * Used as list view item title.
     *
     * @return {String} Item title.
     */
    String getListItemTitle();
}
