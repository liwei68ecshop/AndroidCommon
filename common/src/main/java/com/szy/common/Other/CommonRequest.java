package com.szy.common.Other;

import com.szy.common.R;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zongren on 2016/3/23.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class CommonRequest extends StringRequest {
    public boolean alarm;
    public int message = R.string.loading;

    private int mWhat;
    private Map<String, Object> mAttaches;

    public CommonRequest(String url, int what) {
        this(url, what, RequestMethod.GET);
    }

    public CommonRequest(String url, int what, RequestMethod method) {
        this(url, what, method, new HashMap<String, Object>());
    }

    public CommonRequest(String url, int what, RequestMethod method, Map<String, Object> params) {
        this(url, what, method, params, new HashMap<String, Object>());
    }

    public CommonRequest(String url, int what, RequestMethod method, Map<String, Object> params, Map<String, Object> attaches) {
        super(url, method);
        setCancelSign(what);
        setConnectTimeout(60000);
        setReadTimeout(60000);
        alarm = true;
        mWhat = what;
        mAttaches = attaches;
        setUserAgent("szyapp/android");
    }

    public void addAttach(String key, Object object) {
        mAttaches.put(key, object);
    }

    public Object getAttach(String key) {
        return mAttaches.get(key);
    }

    public Map<String, Object> getAttaches() {
        return mAttaches;
    }

    public int getWhat() {
        return mWhat;
    }

    public void setAjax(boolean ajax) {
        if (ajax) {
            setHeader("X-Requested-With", "XMLHttpRequest");
        } else {
            removeHeader("X-Requested-With");
        }
    }
}
