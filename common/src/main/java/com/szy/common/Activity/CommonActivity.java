package com.szy.common.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.szy.common.Constant.MainColor;
import com.szy.common.Constant.ViewType;
import com.szy.common.Fragment.CommonFragment;
import com.szy.common.Interface.OnEmptyViewClickListener;
import com.szy.common.Other.CommonEvent;
import com.szy.common.Other.CommonRequest;
import com.szy.common.R;
import com.szy.common.Util.CommonUtils;
import com.szy.common.View.CustomProgressDialog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by zongren on 2016/3/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 * Used with CommonFragment
 *
 * @see CommonFragment
 */
public abstract class CommonActivity extends AppCompatActivity implements CommonFragment.OnFinishFragmentListener, View.OnClickListener, OnEmptyViewClickListener {
    private final List<FragmentTransaction> mPendingTransactions = new ArrayList<>();
    protected CommonFragment mFragment;
    protected FragmentManager mFragmentManager;
    protected InputMethodManager mInputManager;
    protected int mLayoutId;
    protected Dialog mProgress;
    protected RequestQueue mRequestQueue;
    protected ActionBar mActionBar;
    protected TextView mTitleView;
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
    View mOfflineView;
    private volatile boolean mIsResumed = false;
    private AlertDialog mConfirmDialog;

    /**
     * Add a #CommonRequest to current mRequestQueue.
     *
     * @param request The request you want to execute.
     */
    public void addRequest(CommonRequest request) {
        addRequest(request, mRequestListener);
    }

    public void addRequest(CommonRequest request, OnResponseListener listener) {
        for (CommonRequest queuedRequest : mRequests.values()) {
            if (queuedRequest.getWhat() == request.getWhat()) {
                mRequestQueue.cancelBySign(request.getWhat());
                mRequests.remove(queuedRequest.getWhat());
                break;
            }
        }
        mRequests.put(request.getWhat(), request);
        mRequestQueue.add(request.getWhat(), request, mRequestListener);
    }

    @Override
    public void fragmentFinish() {
        this.finish();
    }

    @Override
    public void setFragmentResult(int resultCode, Intent data) {
        setResult(resultCode, data);
    }

    @Override
    public void setFragmentResult(int resultCode) {
        setResult(resultCode);
    }

    public void hideOfflineView() {
        if (mOfflineView != null) {
            mOfflineView.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "You need to add offline view in the layout",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onEmptyViewClicked() {

    }

    @Override
    public void onOfflineViewClicked() {

    }

    /**
     * Handle the events of this activity.
     *
     * @param event The event you received.
     */
    @Subscribe
    public void onEvent(CommonEvent event) {
        switch (event.getWhat()) {
            default:
                break;
        }
    }

    public synchronized boolean onResumed() {
        if (!mPendingTransactions.isEmpty()) {
            for (int i = 0, size = mPendingTransactions.size(); i < size; i++) {
                mPendingTransactions.get(i).commit();
            }
            mPendingTransactions.clear();
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                mInputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence sequence) {
        super.setTitle(sequence);
        if (mTitleView != null) {
            mTitleView.setText(sequence);
        }
    }

    @Override
    public void setTitle(int title) {
        super.setTitle(getString(title));
        if (mTitleView != null) {
            mTitleView.setText(getString(title));
        }
    }

    public synchronized boolean safeCommit(@NonNull FragmentTransaction transaction) {
        if (mIsResumed) {
            transaction.commitAllowingStateLoss();
            return false;
        } else {
            mPendingTransactions.add(transaction);
            return true;
        }
    }


    public void showConfirmDialog(int message, ViewType viewType, int position) {
        showConfirmDialog(message, viewType, position, 0);
    }

    public void showConfirmDialog(int message, ViewType viewType, int position, int extraInfo) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_common_confirm, null);
        mConfirmDialog = new AlertDialog.Builder(this).create();
        mConfirmDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mConfirmDialog.setView(view);
        Button confirmButton = (Button) view.findViewById(R.id.dialog_common_confirm_confirmButton);
        CommonUtils.commonSetViewTypeForTag(confirmButton, viewType);
        CommonUtils.setPositionForTag(confirmButton, position);
        CommonUtils.setExtraInfoForTag(confirmButton, extraInfo);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                mConfirmDialog.dismiss();
                CommonActivity.this.onClick(button);
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.dialog_common_confirm_cancelButton);
        CommonUtils.commonSetViewTypeForTag(cancelButton, ViewType.VIEW_TYPE_DIALOG_CANCEL);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                mConfirmDialog.dismiss();
                CommonActivity.this.onClick(button);
            }
        });

        TextView textView = (TextView) view.findViewById(R.id.dialog_common_confirm_textView);
        textView.setText(message);
        mConfirmDialog.show();
    }

    public void dismissConfirmDialog() {
        mConfirmDialog.dismiss();
    }

    public void showConfirmDialog(int message, ViewType viewType) {
        showConfirmDialog(message, viewType, 0);
    }

    public void showConfirmDialog(int message) {
        showConfirmDialog(message, ViewType.VIEW_TYPE_DIALOG_CONFIRM, 0, 0);
    }

    public void showOfflineView() {
        if (mOfflineView != null) {
            mOfflineView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "You need to add offline view in the layout",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create a fragment which extends #CommonFragment
     *
     * @return a fragment which extends #CommonFragment
     */
    protected abstract CommonFragment createFragment();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onPause() {
        if (mProgress.isShowing()) {
            mProgress.dismiss();
        }
        super.onPause();
        mIsResumed = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsResumed = true;
        onResumed();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mIsResumed = true;
        onResumed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止Android8.0报错
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mFragmentManager = getSupportFragmentManager();
        if (mLayoutId == 0) {
            mLayoutId = R.layout.activity_common;
        }
        setContentView(mLayoutId);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mRequests = new HashMap<>();
        mFragment = (CommonFragment) mFragmentManager.findFragmentById(
                R.id.activity_common_fragment_container);
        if (mFragment == null) {
            mFragment = createFragment();
            if (mFragment != null) {
                mFragment.onFinishFragmentListener = this;
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.add(R.id.activity_common_fragment_container, mFragment);
                safeCommit(transaction);
            }
        }
        mOfflineView = findViewById(R.id.activity_common_offline_view);
        if (mOfflineView != null) {
            Button button = (Button) mOfflineView.findViewById(R.id.empty_view_button);
            GradientDrawable enabled = new GradientDrawable();
            enabled.setColor(MainColor.getInstance().getColorPrimary());
            enabled.setCornerRadius(CommonUtils.dpToPx(this, 4));
            button.setBackgroundDrawable(enabled);

            ImageView imageView = (ImageView) mOfflineView.findViewById(R.id.empty_view_imageView);
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
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mProgress = new CustomProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mRequestQueue.cancelAll();
            }
        });
        mRequestQueue = NoHttp.newRequestQueue(1);

        /*将传入Activity的参数继续传入到Fragment*/
        Bundle extras = getIntent().getExtras();
        if (extras != null && mFragment != null) {
            if (mFragment.getArguments() == null) {
                mFragment.setArguments(extras);
            } else {
                //                mFragment.getArguments().clear();
                mFragment.getArguments().putAll(extras);
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_common_toolbar);
        if (toolbar != null) {
            mTitleView = (TextView) toolbar.findViewById(R.id.toolbar_common_titleTextView);
            if (mTitleView != null) {
                mTitleView.setText(getTitle());
            }
            if (getSupportActionBar() == null) {
                setSupportActionBar(toolbar);
                mActionBar = getSupportActionBar();
                assert mActionBar != null;
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mActionBar.setDisplayShowTitleEnabled(false);
            } else {
                mActionBar = getSupportActionBar();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mProgress.isShowing()) {
            mProgress.dismiss();
        }
        EventBus.getDefault().unregister(this);
        mRequestQueue.stop();
        mRequestQueue.cancelAll();
        super.onDestroy();
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
     * Called when requests finish.
     *
     * @param what a int value,indicate which request this is.
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
     * @param what a int value.
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
     * @param what     a int value.
     * @param response response string.
     */
    protected void onRequestSucceed(int what, String response) {
        switch (what) {
            default:
                break;
        }
    }

    protected void updateDialog() {
        boolean hasAlarmRequests = false;
        for (CommonRequest request : mRequests.values()) {
            if (request.alarm) {
                hasAlarmRequests = true;
                break;
            }
        }

        if (hasAlarmRequests) {
            if (!mProgress.isShowing()) {
                mProgress.show();
            }
        } else {
            mProgress.dismiss();
        }
    }
}
