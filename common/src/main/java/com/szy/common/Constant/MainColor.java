package com.szy.common.Constant;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * Created by Smart on 2018/3/31.
 */

public class MainColor {

    private static MainColor mainColor;

    public static MainColor getInstance() {
        if (mainColor == null) {
            mainColor = new MainColor();
        }
        return mainColor;
    }

    //主题颜色
    private int colorPrimary = Color.parseColor("#f23030");
    private int colorPrimaryDark = Color.parseColor("#f23030");

    private String colorPrimaryDarkStr = "#f23030";
    private String colorPrimaryStr = "#f23030";

    public String getColorPrimaryStr() {
        return colorPrimaryStr;
    }

    public void setColorPrimaryStr(String colorPrimaryStr) {
        this.colorPrimaryStr = colorPrimaryStr;
    }

    public String getColorPrimaryDarkStr() {
        return colorPrimaryDarkStr;
    }

    public void setColorPrimaryDarkStr(String colorPrimaryDarkStr) {
        this.colorPrimaryDarkStr = colorPrimaryDarkStr;
    }

    public int getColorPrimary() {
        return colorPrimary;
    }

    public int getColorPrimaryDark() {
        return colorPrimaryDark;
    }

    public void setColorPrimary(@NonNull @ColorInt int colorInt) {
        colorPrimary = colorInt;
    }

    public void setColorPrimaryDark(@NonNull @ColorInt int colorInt) {
        colorPrimaryDark = colorInt;
    }

}
