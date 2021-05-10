package com.szy.common.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.szy.common.ViewHolder.CommonViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zongren on 16/5/4.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public abstract class CommonAdapter<CM, VH extends CommonViewHolder> extends BaseAdapter {
    private static final String TAG = "CommonAdapter";
    protected List<CM> mData;
    protected int mItemLayoutId;
    protected Map<Integer, VH> mViewHolderMap;

    public CommonAdapter() {
        this(new ArrayList<CM>(), 0);
    }

    public CommonAdapter(List<CM> data) {
        this(data, 0);
    }

    public CommonAdapter(int itemLayoutId) {
        this(new ArrayList<CM>(), itemLayoutId);
    }

    public CommonAdapter(int itemLayoutId, List<CM> data) {
        this(data, itemLayoutId);
    }

    public CommonAdapter(List<CM> data, int itemLayoutId) {
        super();
        mData = data;
        mItemLayoutId = itemLayoutId;
        mViewHolderMap = new HashMap<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CM getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        int viewType = getItemViewType(position);
        if (convertView == null) {
            convertView = getView(position, viewType, container);
        }
        onBindViewHolder(position, viewType, onCreateViewHolder(position, convertView, viewType),
                getItem(position));
        return convertView;
    }

    /**
     * Get list data.
     *
     * @return List data.
     */
    public List<CM> getData() {
        return mData;
    }

    /**
     * Set list data
     *
     * @param data List data.
     */
    public void setData(List<CM> data) {
        mData = data;
        notifyDataSetChanged();
    }

    /**
     * Get item view of the specific position.
     * Called when convertView is null.
     * Override this method to support multiple item layout.
     *
     * @param position  The position.
     * @param viewType  The item view type.
     * @param container The container.
     * @return The item view.
     */
    public View getView(int position, int viewType, ViewGroup container) {
        if (mItemLayoutId == 0) {
            throw new RuntimeException("Neither provide item layout id or override getView method");
        }
        return LayoutInflater.from(container.getContext()).inflate(mItemLayoutId, container, false);
    }

    /**
     * Bind the view to viewHolder.
     *
     * @param position The item position.
     * @param viewType The item view type.
     */
    public abstract void onBindViewHolder(int position, int viewType, VH viewHolder, CM item);

    /**
     * Get view holder of certain position,override this to support multiple view holder.
     *
     * @param position The position.
     * @param itemView The item view.
     * @param viewType The view type.
     * @return The view holder.
     */
    public abstract VH onCreateViewHolder(int position, View itemView, int viewType);

}
