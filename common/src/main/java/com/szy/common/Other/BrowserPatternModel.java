package com.szy.common.Other;

import android.content.Context;

import com.szy.common.Util.CommonUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;

/**
 * Created by 宗仁 on 2016/12/17.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class BrowserPatternModel extends BasePatternModel {
    public BrowserPatternModel() {
        super(null);
    }

    @Override
    public boolean execute(Context context, String target) {
        try {
            new URL(target);
            CommonUtils.openBrowser(context, target);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    @Override
    public boolean handlePattern(Context context, Matcher matcher) {
        return true;
    }
}
