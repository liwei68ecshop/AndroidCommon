package com.szy.common.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by zongren on 16/5/4.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class TextWatcherAdapter implements TextWatcher {

    private final EditText view;
    private final TextWatcherListener listener;

    public TextWatcherAdapter(EditText editText, TextWatcherListener listener) {
        this.view = editText;
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // pass
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        listener.onTextChanged(view, s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        // pass
    }

    public interface TextWatcherListener {

        void onTextChanged(EditText view, String text);

    }

}
