package com.szy.common.Interface;

/**
 * Created by zongren on 16/5/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public interface ListViewFragmentChange<T> {
    /**
     * Reach the end of list and calls this.
     *
     * @param item Selected data.
     */
    void onFinishFragment(T item);

    /**
     * Open next fragment.
     *
     * @param item Selected data.
     */
    void openNextFragment(T item);
}

