package com.szy.common.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.szy.common.Constant.ViewType;
import com.szy.common.R;
import com.szy.common.ResponseModel.Region.ResponseRegionItemModel;
import com.szy.common.Util.CommonUtils;
import com.szy.common.ViewHolder.Region.SelectedRegionViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 宗仁 on 2016/5/25 0025.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class SelectedRegionAdapter extends RecyclerView.Adapter<SelectedRegionViewHolder> {

    public List<ResponseRegionItemModel> data;
    public View.OnClickListener onClickListener;

    public SelectedRegionAdapter() {
        this.data = new ArrayList<>();
    }

    public SelectedRegionAdapter(List<ResponseRegionItemModel> data) {
        this.data = data;
    }

    @Override
    public SelectedRegionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.fragment_region_selected_item, viewGroup, false);
        return new SelectedRegionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectedRegionViewHolder holder, int position) {
        ResponseRegionItemModel item = data.get(position);
        holder.nameTextView.setText(item.region_name);
        if (position == data.size() - 1) {
            holder.nameTextView.setSelected(true);
        } else {
            holder.nameTextView.setSelected(false);
        }
        CommonUtils.setPositionForTag(holder.itemView, position);
        CommonUtils.commonSetViewTypeForTag(holder.itemView, ViewType.VIEW_TYPE_SELECTED_REGION);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
