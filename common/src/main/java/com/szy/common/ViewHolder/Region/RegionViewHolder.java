package com.szy.common.ViewHolder.Region;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.szy.common.Constant.MainColor;
import com.szy.common.R;
import com.szy.common.Util.CommonUtils;

/**
 * Created by 宗仁 on 16/8/4.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class RegionViewHolder extends RecyclerView.ViewHolder {
    public TextView nameTextView;
    public ImageView markImageView;
    public Button plusButton;

    public RegionViewHolder(View view) {
        super(view);
        nameTextView = (TextView) view.findViewById(R.id.activity_region_item_nameTextView);
        markImageView = (ImageView) view.findViewById(R.id.activity_region_item_markImageView);
        plusButton = (Button) view.findViewById(R.id.fragment_region_item_plusButton);

        markImageView.setImageDrawable(CommonUtils.getDrawableUp(view.getContext(), R.mipmap.ic_check_red, true));
        int[] colors = new int[]{MainColor.getInstance().getColorPrimary(), view.getResources().getColor(R.color.colorTwo)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};
        nameTextView.setTextColor(new ColorStateList(states, colors));

        plusButton.setBackgroundColor(MainColor.getInstance().getColorPrimary());
    }
}
