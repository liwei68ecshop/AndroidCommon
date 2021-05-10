package com.szy.common.Util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.szy.common.Interface.EmptyViewListener;
import com.szy.common.R;
import com.szy.common.ViewHolder.EmptyViewHolder;
import com.szy.common.ViewModel.BaseEmptyViewModel;

/**
 * Created by 宗仁 on 2016/12/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class BaseEmptyViewUtils {

    public static void hideEmptyView(ViewGroup viewGroup) {
        for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
            View subview = viewGroup.getChildAt(i);
            if (subview.getId() != R.id.empty_view) {
                Object tag = subview.getTag(R.id.preserve_visibility);
                if (tag != null) {
                    int preserveVisibility = (int) tag;
                    switch (preserveVisibility) {
                        case 0x00000000:
                            subview.setVisibility(View.VISIBLE);
                            break;
                        case 0x00000004:
                            subview.setVisibility(View.INVISIBLE);
                            break;
                        case 0x00000008:
                            subview.setVisibility(View.GONE);
                            break;
                    }
                }
            } else {
                viewGroup.removeViewAt(i);
            }
        }
    }

    public static void showEmptyView(ViewGroup viewGroup, final EmptyViewListener listener, final BaseEmptyViewModel model) {
        View view = null;
        for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
            View subview = viewGroup.getChildAt(i);
            if (subview.getId() == R.id.empty_view) {
                view = subview;
                viewGroup.removeViewAt(i);
                break;
            }
        }

        if (view == null) {
            if (viewGroup instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) viewGroup;
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View subview = linearLayout.getChildAt(i);
                    if (subview.getId() != R.id.empty_view) {
                        subview.setTag(R.id.preserve_visibility, subview.getVisibility());
                        subview.setVisibility(View.GONE);
                    }
                }
            }
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    com.szy.common.R.layout.empty_view, viewGroup, false);
            view.setId(R.id.empty_view);
        }

        EmptyViewHolder binder = new EmptyViewHolder(view);
        binder.bind(model);

        binder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onEmptyViewClicked((int) view.getTag(R.id.tag_empty_view_type));
                }
            }
        });

        binder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onEmptyViewClicked((int) view.getTag(R.id.tag_empty_view_type));
                }
            }
        });

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = viewGroup.getHeight();
        layoutParams.width = viewGroup.getWidth();
        viewGroup.addView(view);
        viewGroup.bringChildToFront(view);
    }
}
