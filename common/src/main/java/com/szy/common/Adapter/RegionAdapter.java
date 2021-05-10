package com.szy.common.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.szy.common.Constant.ViewType;
import com.szy.common.R;
import com.szy.common.ResponseModel.Region.ResponseRegionItemModel;
import com.szy.common.Util.CommonUtils;
import com.szy.common.ViewHolder.Region.RegionViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 宗仁 on 16/8/4.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class RegionAdapter extends RecyclerView.Adapter<RegionViewHolder> {
    public List<ResponseRegionItemModel> data;
    public View.OnClickListener onClickListener;
    public boolean selectable;

    public RegionAdapter() {
        data = new ArrayList<>();
    }

    public RegionAdapter(List<ResponseRegionItemModel> data) {
        this.data = data;
    }

    @Override
    public RegionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.fragment_region_item, viewGroup, false);
        return new RegionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RegionViewHolder holder, int position) {
        ResponseRegionItemModel item = data.get(position);
        holder.nameTextView.setText(item.region_name);

        holder.nameTextView.setSelected(item.selected);
        if (item.selected) {
            holder.markImageView.setVisibility(View.VISIBLE);
        } else {
            holder.markImageView.setVisibility(View.GONE);
        }
        if (selectable && !item.isAdded) {
            holder.plusButton.setVisibility(View.VISIBLE);
        } else {
            holder.plusButton.setVisibility(View.GONE);
        }
        CommonUtils.setPositionForTag(holder.itemView, position);
        CommonUtils.commonSetViewTypeForTag(holder.itemView, ViewType.VIEW_TYPE_REGION);
        CommonUtils.setPositionForTag(holder.plusButton, position);
        CommonUtils.commonSetViewTypeForTag(holder.plusButton, ViewType.VIEW_TYPE_PLUS);
        holder.itemView.setOnClickListener(onClickListener);
        holder.plusButton.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
