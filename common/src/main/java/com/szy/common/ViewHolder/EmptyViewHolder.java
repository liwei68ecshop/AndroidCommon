package com.szy.common.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.szy.common.R;
import com.szy.common.ViewModel.BaseEmptyViewModel;

/**
 * Created by 宗仁 on 2016/12/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class EmptyViewHolder {
    public ImageView imageView;
    public TextView titleTextView;
    public TextView subtitleTextView;
    public Button button;
    private View itemView;

    public EmptyViewHolder(View view) {
        this.itemView = view;
        this.imageView = (ImageView) view.findViewById(R.id.empty_view_imageView);
        this.titleTextView = (TextView) view.findViewById(R.id.empty_view_titleTextView);
        this.subtitleTextView = (TextView) view.findViewById(R.id.empty_view_subtitleTextView);
        this.button = (Button) view.findViewById(R.id.empty_view_button);
    }

    public void bind(BaseEmptyViewModel model) {
        if (model.image > 0) {
            imageView.setImageResource(model.image);
            imageView.setVisibility(View.VISIBLE);
            this.imageView.setTag(R.id.tag_empty_view_type, model.type);
        } else {
            imageView.setVisibility(View.GONE);
        }
        if (model.title > 0) {
            titleTextView.setText(model.title);
            titleTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.GONE);
        }
        if (model.subtitle > 0) {
            subtitleTextView.setText(model.subtitle);
            subtitleTextView.setVisibility(View.VISIBLE);
        } else {
            subtitleTextView.setVisibility(View.GONE);
        }
        if (model.buttonTitle > 0) {
            button.setText(model.buttonTitle);
            button.setVisibility(View.VISIBLE);
            this.button.setTag(R.id.tag_empty_view_type, model.type);
        } else {
            button.setVisibility(View.GONE);
        }
    }
}
