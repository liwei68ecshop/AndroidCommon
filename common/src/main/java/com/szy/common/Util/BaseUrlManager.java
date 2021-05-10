package com.szy.common.Util;

import android.content.Context;
import android.widget.Toast;

import com.szy.common.Interface.NoMatchListener;
import com.szy.common.Other.BasePatternModel;
import com.szy.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 宗仁 on 2016/12/15.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class BaseUrlManager implements NoMatchListener {
    public NoMatchListener noMatchListener;
    public List<BasePatternModel> patterns;

    public BaseUrlManager() {
        patterns = new ArrayList<>();
        noMatchListener = this;
    }

    public void addPattern(BasePatternModel newPattern) {
        patterns.add(newPattern);
    }

    @Override
    public boolean noMatches(Context context, String url) {
        CommonUtils.copyToClipboard(context, context.getString(R.string.copyUrl), url);
        Toast.makeText(context, R.string.copyToClipboard, Toast.LENGTH_SHORT).show();
        return true;
    }

    /**
     * 处理链接，并返回是否处理成功
     *
     * @param context The context.
     * @param url     The url to be parsed.
     * @return If the url is successfully consumed.
     */
    public boolean parseUrl(Context context, String url) {
        if (context == null) {
            return false;
        }
        if (url == null || url.trim().length() == 0) {
            Toast.makeText(context, R.string.emptyUrl, Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean consumed = false;
        for (BasePatternModel patternModel : patterns) {
            consumed = patternModel.execute(context, url);
            if (consumed) {
                break;
            }
        }
        return consumed || noMatchListener != null && noMatchListener.noMatches(context, url);
    }
}
