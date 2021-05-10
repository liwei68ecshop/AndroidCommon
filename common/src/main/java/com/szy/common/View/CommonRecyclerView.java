package com.szy.common.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.szy.common.Constant.MainColor;
import com.szy.common.Interface.OnEmptyViewClickListener;
import com.szy.common.R;
import com.szy.common.Util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import me.zongren.pullablelayout.View.PullableRecyclerView;

/**
 * Created by 宗仁 on 16/8/9.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class CommonRecyclerView extends PullableRecyclerView {
    private static final int VIEW_TYPE_EMPTY = -2;
    private static final int VIEW_TYPE_OFFLINE = -4;
    private EmptyAdapter mEmptyAdapter;
    private EmptyModel mEmptyModel;
    private OfflineModel mOfflineModel;
    private GridLayoutManager.SpanSizeLookup mSpanSizeLookup;
    private Adapter mAdapter;
    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        public void onChanged() {
            mEmptyAdapter.notifyDataSetChanged();
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            mEmptyAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            mEmptyAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mEmptyAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (itemCount == 1) {
                mEmptyAdapter.notifyItemMoved(fromPosition, toPosition);
            } else {
                Log.i(getClass().getSimpleName(), "Not support this");
            }
        }
    };

    public CommonRecyclerView(Context context) {
        this(context, null, 0);
    }

    public CommonRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mEmptyAdapter = new EmptyAdapter();
        mEmptyModel = new EmptyModel();
        mOfflineModel = new OfflineModel();

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CommonRecyclerView, 0, 0);
        mEmptyModel.imageResourceId = array.getResourceId(R.styleable.CommonRecyclerView_emptyImage,
                0);
        mEmptyModel.title = array.getResourceId(R.styleable.CommonRecyclerView_emptyTitle, 0);
        mEmptyModel.subtitle = array.getResourceId(R.styleable.CommonRecyclerView_emptySubtitle, 0);
        mEmptyModel.buttonTitle = array.getResourceId(
                R.styleable.CommonRecyclerView_emptyButtonTitle, 0);

        mOfflineModel.imageResourceId = array.getResourceId(
                R.styleable.CommonRecyclerView_offlineImage, R.mipmap.bg_public);
        mOfflineModel.title = array.getResourceId(R.styleable.CommonRecyclerView_offlineTitle,
                R.string.requestFailed);
        mOfflineModel.subtitle = array.getResourceId(R.styleable.CommonRecyclerView_offlineSubtitle,
                R.string.pleaseCheckYourNetwork);
        mOfflineModel.buttonTitle = array.getResourceId(
                R.styleable.CommonRecyclerView_offlineButtonTitle, R.string.reload);
    }

    public void hideEmptyView() {
        mEmptyAdapter.data.clear();
        mEmptyAdapter.notifyDataSetChanged();
    }

    public void setEmptyButton(int button) {
        mEmptyModel.buttonTitle = button;
        notifyEmptyViewAdapterIfNeeded();
    }

    public void setEmptyImage(int image) {
        mEmptyModel.imageResourceId = image;
        notifyEmptyViewAdapterIfNeeded();
    }

    public void setEmptySubtitle(int subtitle) {
        mEmptyModel.subtitle = subtitle;
        notifyEmptyViewAdapterIfNeeded();
    }

    public void setEmptyTitle(int title) {
        mEmptyModel.title = title;
        notifyEmptyViewAdapterIfNeeded();
    }

    public void setEmptyViewClickListener(OnEmptyViewClickListener onClickListener) {
        mEmptyAdapter.onClickListener = onClickListener;
    }

    public void showEmptyView() {
        mEmptyAdapter.data.clear();
        mEmptyAdapter.data.add(mEmptyModel);
        mEmptyAdapter.notifyDataSetChanged();
    }

    public void showOfflineView() {
        mEmptyAdapter.data.clear();
        mEmptyAdapter.data.add(mOfflineModel);
        mEmptyAdapter.notifyDataSetChanged();
    }

    private void notifyEmptyViewAdapterIfNeeded() {
        if (mAdapter.getItemCount() == 0 && mEmptyAdapter.data.size() > 0) {
            mEmptyAdapter.notifyDataSetChanged();
        }
    }

    private class EmptyViewHolder extends ViewHolder {
        public TextView titleTextView;
        public ImageView imageView;
        public TextView subTitleTextView;
        public Button button;

        public EmptyViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.empty_view_titleTextView);
            imageView = (ImageView) view.findViewById(R.id.empty_view_imageView);
            subTitleTextView = (TextView) view.findViewById(R.id.empty_view_subtitleTextView);
            button = (Button) view.findViewById(R.id.empty_view_button);
        }
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    private class EmptyAdapter extends Adapter {
        public OnEmptyViewClickListener onClickListener;
        public List<Object> data;

        public EmptyAdapter() {
            data = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mAdapter.getItemCount() == 0 && mEmptyAdapter.data.size() > 0) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view,
                        parent, false);
                return new EmptyViewHolder(view);
            } else {
                return mAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (mAdapter.getItemCount() == 0 && mEmptyAdapter.data.size() > 0) {
                int viewType = getItemViewType(position);
                bindEmptyViewHolder((EmptyViewHolder) holder, (EmptyModel) data.get(position),
                        viewType);
            } else {
                mAdapter.onBindViewHolder(holder, position);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mAdapter.getItemCount() == 0 && mEmptyAdapter.data.size() > 0) {
                return getEmptyViewType(position);
            } else {
                return mAdapter.getItemViewType(position);
            }
        }

        @Override
        public int getItemCount() {
            if (mAdapter.getItemCount() == 0 && mEmptyAdapter.data.size() > 0) {
                return data.size();
            } else {
                return mAdapter.getItemCount();
            }
        }

        private void bindEmptyViewHolder(final EmptyViewHolder holder, EmptyModel model, final int viewType) {

            Context mContext = holder.itemView.getContext();

            GradientDrawable enabled = (GradientDrawable) holder.button.getBackground();
            if (enabled == null) {
                enabled = new GradientDrawable();
                enabled.setCornerRadius(CommonUtils.dpToPx(mContext, 4));
            }
            enabled.setColor(MainColor.getInstance().getColorPrimary());
            holder.button.setBackgroundDrawable(enabled);

            if (model.imageResourceId == 0) {
                holder.imageView.setVisibility(GONE);
            } else {
                holder.imageView.setImageResource(model.imageResourceId);
                holder.imageView.setVisibility(VISIBLE);
            }
            if (model.title == 0) {
                holder.titleTextView.setVisibility(GONE);
            } else {
                holder.titleTextView.setText(model.title);
                holder.titleTextView.setVisibility(VISIBLE);
            }
            if (model.subtitle == 0) {
                holder.subTitleTextView.setVisibility(GONE);
            } else {
                holder.subTitleTextView.setText(model.subtitle);
                holder.subTitleTextView.setVisibility(VISIBLE);
            }
            if (model.buttonTitle == 0) {
                holder.button.setVisibility(GONE);
            } else {
                holder.button.setText(model.buttonTitle);
                holder.button.setVisibility(VISIBLE);
            }

            holder.imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEmptyViewClicked(viewType);
                }
            });

            holder.button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEmptyViewClicked(viewType);
                }
            });
        }

        private int getEmptyViewType(int position) {
            Object object = data.get(position);
            if (object instanceof OfflineModel) {
                return VIEW_TYPE_OFFLINE;
            } else if (object instanceof EmptyModel) {
                return VIEW_TYPE_EMPTY;
            }
            return -1;
        }

        private void onEmptyViewClicked(int viewType) {
            if (onClickListener != null) {
                switch (viewType) {
                    case VIEW_TYPE_EMPTY:
                        onClickListener.onEmptyViewClicked();
                        break;
                    case VIEW_TYPE_OFFLINE:
                        onClickListener.onOfflineViewClicked();
                        break;
                }
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        super.setAdapter(mEmptyAdapter);
        if (mAdapter != null) {
            mAdapter.registerAdapterDataObserver(mObserver);
        }
    }

    private class EmptyModel {
        public int viewType;
        public int title;
        public int subtitle;
        public int buttonTitle;
        public int imageResourceId;

        public EmptyModel() {
            viewType = VIEW_TYPE_EMPTY;
            title = R.string.emptyList;
            imageResourceId = R.mipmap.bg_public;
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            if (gridLayoutManager.getSpanSizeLookup() != null) {
                mSpanSizeLookup = gridLayoutManager.getSpanSizeLookup();
                GridLayoutManager.SpanSizeLookup emptySpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (mAdapter.getItemCount() == 0) {
                            return gridLayoutManager.getSpanCount();
                        } else {
                            return mSpanSizeLookup.getSpanSize(position);
                        }
                    }
                };
                gridLayoutManager.setSpanSizeLookup(emptySpanSizeLookup);
            }
        }
    }

    private class OfflineModel extends EmptyModel {
        public OfflineModel() {
            super();
            title = R.string.requestFailed;
            subtitle = R.string.pleaseCheckYourNetwork;
            buttonTitle = R.string.reload;
            imageResourceId = R.mipmap.bg_public;
            viewType = VIEW_TYPE_OFFLINE;
        }
    }

}
