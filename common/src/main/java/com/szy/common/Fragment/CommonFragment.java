package com.szy.common.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.szy.common.Constant.MainColor;
import com.szy.common.Interface.OnConfirmDialogClickListener;
import com.szy.common.Interface.OnEmptyViewClickListener;
import com.szy.common.Other.CommonEvent;
import com.szy.common.Other.CommonRequest;
import com.szy.common.R;
import com.szy.common.ResponseModel.Common.ResponseCommonModel;
import com.szy.common.Util.CommonUtils;
import com.szy.common.Util.JSON;
import com.szy.common.View.CustomProgressDialog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by zongren on 2016/3/16.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public abstract class CommonFragment extends Fragment implements View.OnClickListener, OnConfirmDialogClickListener, OnEmptyViewClickListener {
    public OnFinishFragmentListener onFinishFragmentListener;
    protected int mLayoutId = R.layout.fragment_common;
    protected CustomProgressDialog mProgress;
    protected RequestQueue mRequestQueue;
    protected LayoutInflater mInflater;
    protected Map<Integer, CommonRequest> mRequests;
    protected OnResponseListener<String> mRequestListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
            updateDialog();
            onRequestStart(what);
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            onRequestSucceed(what, response.get());
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            onRequestFailed(what, response.get());
        }

        @Override
        public void onFinish(int what) {
            mRequests.remove(what);
            updateDialog();
            onRequestFinish(what);
        }
    };
    private ViewGroup mViewContainer;
    private AlertDialog mConfirmDialog;
    private View mOfflineView;

    /**
     * Add a #CommonRequest to current mRequestQueue.
     *
     * @param request The request you want to execute.
     */
    public void addRequest(CommonRequest request) {
        for (CommonRequest queuedRequest : mRequests.values()) {
            if (queuedRequest.getWhat() == request.getWhat() && queuedRequest.url().equals(
                    request.url())) {
                mRequestQueue.cancelBySign(request.getWhat());
                mRequests.remove(queuedRequest.getWhat());
                break;
            }
        }
        mRequests.put(request.getWhat(), request);
        mRequestQueue.add(request.getWhat(), request, mRequestListener);
    }

    public void hideOfflineView() {
        if (mViewContainer == null) {
            return;
        }
        if (mOfflineView == null) {
            return;
        }
        mViewContainer.removeView(mOfflineView);
    }

    @Override
    public void onClick(View view) {
        int viewType = CommonUtils.getViewTypeIntegerOfTag(view);
        switch (viewType) {
            default:
                break;
        }
    }

    @Override
    public void onConfirmDialogCanceled(int viewType, int position, int extraInfo) {
        Log.i(CommonFragment.this.getClass().getSimpleName(),
                "Stub method:onConfirmDialogCanceled been called");
    }

    @Override
    public void onConfirmDialogConfirmed(int viewType, int position, int extraInfo) {
        Log.i(CommonFragment.this.getClass().getSimpleName(),
                "Stub method:onConfirmDialogConfirmed been called");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mRequests = new HashMap<>();
        mProgress = new CustomProgressDialog(getActivity());
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mRequestQueue.cancelAll();
            }
        });
        mRequestQueue = NoHttp.newRequestQueue(1);

        // Retain this fragment across configuration changes.
        // Change screen orientation,language setting,font size etc.
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(mLayoutId, container, false);
        mViewContainer = container;
        ButterKnife.bind(this, view);
        mInflater = inflater;
        return view;
    }

    @Override
    public void onPause() {
        if (mProgress.isShowing()) {
            mProgress.dismiss();
        }
        super.onPause();

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mRequestQueue.stop();
        mRequestQueue.cancelAll();
        if (mProgress.isShowing()) {
            mProgress.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onEmptyViewClicked() {

    }

    @Override
    public void onOfflineViewClicked() {

    }

    /**
     * Called when post a event.
     *
     * @param event CommonEvent.
     */
    @Subscribe
    public void onEvent(CommonEvent event) {
        switch (event.getWhat()) {
            default:
                break;
        }
    }

    public void showConfirmDialog(int message, final int viewType, final int position, final int extraInfo) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_common_confirm, null);
        mConfirmDialog = new AlertDialog.Builder(getContext()).create();
        mConfirmDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mConfirmDialog.setView(view);
        Button confirmButton = (Button) view.findViewById(R.id.dialog_common_confirm_confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                mConfirmDialog.dismiss();
                onConfirmDialogConfirmed(viewType, position, extraInfo);
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.dialog_common_confirm_cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                mConfirmDialog.dismiss();
                onConfirmDialogCanceled(viewType, position, extraInfo);
            }
        });

        TextView textView = (TextView) view.findViewById(R.id.dialog_common_confirm_textView);
        textView.setText(message);

        mConfirmDialog.show();
    }


    public void dismissConfirmDialog() {
        mConfirmDialog.dismiss();
    }

    public void showConfirmDialog(int message) {
        showConfirmDialog(message, 0, 0, 0);
    }

    public void showConfirmDialog(int message, int viewType) {
        showConfirmDialog(message, viewType, 0, 0);
    }

    public void showConfirmDialog(int message, int viewType, int position) {
        showConfirmDialog(message, viewType, position, 0);
    }

    public void showOfflineView() {
        if (mViewContainer == null) {
            return;
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.offline_view, mViewContainer,
                false);


        if (view == null) {
            return;
        }
        mOfflineView = view;

        mViewContainer.addView(view);
        mViewContainer.bringChildToFront(view);
        Button button = (Button) view.findViewById(R.id.empty_view_button);
        Context mContext = view.getContext();
        GradientDrawable enabled = new GradientDrawable();
        enabled.setColor(MainColor.getInstance().getColorPrimary());
        enabled.setCornerRadius(CommonUtils.dpToPx(mContext, 4));

        button.setBackgroundDrawable(enabled);

        ImageView imageView = (ImageView) view.findViewById(R.id.empty_view_imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOfflineViewClicked();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOfflineViewClicked();
            }
        });
    }

    public void updateDialog() {
        boolean hasAlarmRequests = false;
        for (CommonRequest request : mRequests.values()) {
            if (request.alarm) {
                hasAlarmRequests = true;
                break;
            }
        }

        if (hasAlarmRequests) {
            if (!mProgress.isShowing() && isVisible()) {
                mProgress.show();
            }
        } else {
            mProgress.dismiss();
        }
    }

    protected void finish() {
        if (onFinishFragmentListener != null) {
            onFinishFragmentListener.fragmentFinish();
        }
    }

    /**
     * Called when requests failed.
     *
     * @param what     a int value,indicate which request this is.
     * @param response response string.
     */
    protected void onRequestFailed(int what, String response) {
        switch (what) {
            default:
                break;
        }
    }

    /**
     * Called when requests finish
     *
     * @param what a HTTP_WHAT value,indicate which request this is
     */
    protected void onRequestFinish(int what) {
        switch (what) {
            default:
                break;
        }
    }

    /**
     * Called when requests start.
     *
     * @param what a HTTP_WHAT value.
     */
    protected void onRequestStart(int what) {
        switch (what) {
            default:
                break;
        }
    }

    /**
     * Called when requests succeed.
     *
     * @param what     a HTTP_WHAT value.
     * @param response response string.
     */
    protected void onRequestSucceed(int what, String response) {
        switch (what) {
            default:
                ResponseCommonModel model = JSON.parseObject(response, ResponseCommonModel.class);
                if (model.code != 0) {
                    onRequestFailed(what, response);
                } else {
                    Toast.makeText(getContext(), R.string.requestSucceed,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected void setResult(int resultCode, Intent data) {
        if (onFinishFragmentListener != null) {
            onFinishFragmentListener.setFragmentResult(resultCode, data);
        }
    }

    protected void setResult(int resultCode) {
        if (onFinishFragmentListener != null) {
            onFinishFragmentListener.setFragmentResult(resultCode);
        }
    }

    public interface OnFinishFragmentListener {
        void fragmentFinish();

        void setFragmentResult(int resultCode, Intent data);

        void setFragmentResult(int resultCode);
    }

}
