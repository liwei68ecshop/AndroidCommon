package com.szy.common.Util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 宗仁 on 2016/11/29.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class JSON {
    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        return com.alibaba.fastjson.JSON.parseArray(text, clazz);
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        return new JSON().realParseObject(text, clazz, null);
    }

    public <T> T realParseObject(String text, Class<T> clazz, Set<String> otherPackages) {
        T object = com.alibaba.fastjson.JSON.parseObject(text, clazz);
        Set<String> packages = new HashSet<>();
        packages.add("ResponseModel");
        packages.add("ViewModel");
        if (otherPackages != null) {
            for (String otherPackage : otherPackages) {
                packages.add(otherPackage);
            }
        }
        try {
            CommonUtils.instantiateFieldsOfObject(object, packages);
        } catch (IllegalAccessException e) {
            return object;
        } catch (InstantiationException e) {
            return object;
        } catch (IllegalArgumentException e) {
            return object;
        }
        return object;
    }

    public static String toJSONString(Object object) {
        return com.alibaba.fastjson.JSON.toJSONString(object);
    }

    public static String toJSONString(Object object, boolean prettyFormat) {
        return com.alibaba.fastjson.JSON.toJSONString(object, prettyFormat);

    }
}
