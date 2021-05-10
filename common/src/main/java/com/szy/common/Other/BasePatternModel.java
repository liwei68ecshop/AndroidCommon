package com.szy.common.Other;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 宗仁 on 2016/12/15.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public abstract class BasePatternModel {
    public String pattern;
    public String target;

    public BasePatternModel(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Deal with the target string.
     *
     * @param context The context.
     * @param target  The target string.
     * @return If the target is consumed.
     */
    public boolean execute(Context context, String target) {
        Pattern pattern = Pattern.compile(this.pattern);
        Matcher matcher = pattern.matcher(target);
        if (matcher.matches()) {
            this.target = target;
            return handlePattern(context, matcher);
        } else {
            return false;
        }
    }

    /**
     * Called when the target matches the pattern.
     *
     * @param context The context.
     * @param matcher The matcher.
     * @return If the target is consumed.
     */
    public abstract boolean handlePattern(Context context, Matcher matcher);
}