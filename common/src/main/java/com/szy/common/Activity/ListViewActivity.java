package com.szy.common.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.szy.common.Fragment.CommonFragment;
import com.szy.common.Fragment.ListViewFragment;
import com.szy.common.Interface.ListViewFragmentChange;
import com.szy.common.Interface.ListViewItem;
import com.szy.common.R;

import java.util.ArrayList;

/**
 * Created by zongren on 16/5/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 * A multi level ListView,used with #ListViewFragment
 */
public abstract class ListViewActivity<T extends ListViewItem> extends CommonActivity implements ListViewFragmentChange<T> {
    protected static final String EXTRA_LIST_VIEW_DATA = "com.szy.common.EXTRA_LIST_VIEW_DATA";
    protected static final String KEY_LIST_VIEW_DATA = "com.szy.common.KEY_LIST_VIEW_DATA";
    private static final String TAG = "ListViewActivity";
    protected ArrayList<T> mSelectedDataList;
    //    protected BreadCrumbLayout mBreadCrumbLayout;

    @Override
    public void onBackPressed() {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            mSelectedDataList.remove(mSelectedDataList.size() - 1);
            mFragmentManager.popBackStack();
            //            mBreadCrumbLayout.removeLastBreadCrumb();
        }
    }

    /**
     * Called when click bread crumb title.
     *
     * @param position Clicked bread's position.Start from 0.
     */
    public void onBreadCrumbItemClicked(int position) {
        mFragmentManager.popBackStack(String.valueOf(position),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        int size = mSelectedDataList.size();
        Log.i(TAG, "Click bread crumb " + position);
        for (int i = size - 1; i >= position; i--) {
            mSelectedDataList.remove(i);
            Log.i(TAG, "Remove data " + i);
        }
    }

    /**
     * When reach the end of list(isEnd return true),
     * open next activity and put selected data array in extra.
     */
    public void openNextActivity() {
        Intent result = new Intent();
        result.putParcelableArrayListExtra(EXTRA_LIST_VIEW_DATA, mSelectedDataList);
        result.setClass(this, getNextActivity());
        finish();
        startActivity(result);
    }

    @Override
    public void openNextFragment(T data) {
        mSelectedDataList.add(data);
        ListViewFragment<T> nextFragment = createListViewFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(KEY_LIST_VIEW_DATA, data);
        nextFragment.setArguments(arguments);
        nextFragment.setListViewFragmentChange(this);
        /*mFragmentManager.beginTransaction()
                .replace(R.id.activity_common_fragment_container, nextFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(String.valueOf(mBreadCrumbLayout.getBreadCount()))
                .commitAllowingStateLoss();*/
        //        mBreadCrumbLayout.addBreadCrumb(data.getListItemBreadCrumb());
    }

    @Override
    public void onFinishFragment(T data) {
        mSelectedDataList.add(data);
        if (getCallingActivity() == null) {
            openNextActivity();
        } else {
            Intent result = new Intent();
            result.putParcelableArrayListExtra(EXTRA_LIST_VIEW_DATA, mSelectedDataList);
            setResult(RESULT_OK, result);
            finish();
        }
    }

    @Override
    protected CommonFragment createFragment() {
        ListViewFragment<T> fragment = createListViewFragment();
        fragment.setListViewFragmentChange(this);
        return fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutId = R.layout.activity_list_view;
        super.onCreate(savedInstanceState);
        mSelectedDataList = new ArrayList<>();
        /*mBreadCrumbLayout = (BreadCrumbLayout) findViewById(R.id.fragment_listView_breadCrumbLayout);
        if (mBreadCrumbLayout != null) {
            mBreadCrumbLayout.setOnBreadCrumbItemClickListener(this);
        }*/
    }

    /**
     * Create the instance of fragment that extends ListViewFragment
     *
     * @return a fragment
     */
    protected abstract ListViewFragment<T> createListViewFragment();

    /**
     * Get next activity's class you want to open
     * when reach the end of list
     *
     * @return class of activity
     */
    protected abstract Class getNextActivity();
}
