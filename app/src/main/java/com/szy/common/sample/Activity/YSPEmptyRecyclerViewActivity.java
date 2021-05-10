package com.szy.common.sample.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.szy.common.Constant.ViewType;
import com.szy.common.Interface.EmptyViewListener;
import com.szy.common.View.CommonRecyclerView;
import com.szy.common.sample.Adapter.EmptyAdapter;
import com.szy.common.sample.R;
import com.szy.common.sample.Util.EmptyViewUtils;

/**
 * Created by 宗仁 on 2016/12/2.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class YSPEmptyRecyclerViewActivity extends AppCompatActivity implements EmptyViewListener {
    CommonRecyclerView mCommonRecyclerView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_empty, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_empty_showEmptyView:
                EmptyViewUtils.showEmptyView(mCommonRecyclerView, this);
                return true;
            case R.id.activity_empty_showOfflineView:
                EmptyViewUtils.showOfflineView(mCommonRecyclerView, this);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onEmptyViewClicked(int what) {
        switch (ViewType.valueOf(what)) {
            case VIEW_TYPE_OFFLINE:
            case VIEW_TYPE_EMPTY:
                EmptyViewUtils.hideEmptyView(mCommonRecyclerView);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_recycler_view);
        mCommonRecyclerView = (CommonRecyclerView) findViewById(
                R.id.activity_empty_commonRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCommonRecyclerView.setLayoutManager(layoutManager);
        mCommonRecyclerView.setAdapter(new EmptyAdapter());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
