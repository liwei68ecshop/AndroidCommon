package com.szy.common.sample.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.szy.common.Constant.ViewType;
import com.szy.common.Interface.EmptyViewListener;
import com.szy.common.Util.BaseEmptyViewUtils;
import com.szy.common.sample.R;
import com.szy.common.sample.Util.EmptyViewUtils;

/**
 * Created by 宗仁 on 2016/12/16.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class YSPEmptyLinearLayoutActivity extends AppCompatActivity implements EmptyViewListener {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_empty, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_empty_showEmptyView:
                EmptyViewUtils.showEmptyView((LinearLayout) findViewById(R.id.activity_root_view),
                        this);
                return true;
            case R.id.activity_empty_showOfflineView:
                EmptyViewUtils.showOfflineView((LinearLayout) findViewById(R.id.activity_root_view),
                        this);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onEmptyViewClicked(int type) {
        switch (ViewType.valueOf(type)) {
            case VIEW_TYPE_OFFLINE:
                BaseEmptyViewUtils.hideEmptyView(
                        (LinearLayout) findViewById(R.id.activity_root_view));
                break;
            case VIEW_TYPE_EMPTY:
                BaseEmptyViewUtils.hideEmptyView(
                        (LinearLayout) findViewById(R.id.activity_root_view));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_linear_layout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
