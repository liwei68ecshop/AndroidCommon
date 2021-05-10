package com.szy.common.ViewHolder.Region;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.szy.common.Constant.MainColor;
import com.szy.common.R;
import com.szy.common.Util.CommonUtils;

/**
 * Created by 宗仁 on 16/8/4.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class SelectedRegionViewHolder extends RecyclerView.ViewHolder {
    public TextView nameTextView;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public SelectedRegionViewHolder(View view) {
        super(view);
        nameTextView = (TextView) view.findViewById(R.id.activity_region_selected_item_textView);

        int[] colors = new int[]{MainColor.getInstance().getColorPrimary(), view.getResources().getColor(R.color.colorTwo)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{-android.R.attr.state_selected};
        ColorStateList colorStateList = new ColorStateList(states, colors);

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(MainColor.getInstance().getColorPrimary());

        GradientDrawable gd2 = new GradientDrawable();
        gd2.setColor(view.getResources().getColor(android.R.color.white));

        Drawable[] layers = {gd1, gd2};
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.setLayerInset(1, 0, 0, 0, CommonUtils.dpToPx(view.getContext(), 1));

        Drawable unEnabled = view.getResources().getDrawable(R.drawable.activity_region_selected_item_background_normal);
        StateListDrawable stateList = new StateListDrawable();
        int stateEnabled = android.R.attr.state_selected;
        stateList.addState(new int[]{stateEnabled}, layerDrawable);
        stateList.addState(new int[]{-stateEnabled}, unEnabled);
        stateList.addState(new int[]{}, unEnabled);

        nameTextView.setTextColor(colorStateList);
        nameTextView.setBackground(stateList);
    }
}