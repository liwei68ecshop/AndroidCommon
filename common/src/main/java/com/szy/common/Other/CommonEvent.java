package com.szy.common.Other;

/**
 * Created by zongren on 2016/3/19.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class CommonEvent {
    private int mWhat;
    private String mMessage;
    private String mMessageSource;

    public CommonEvent(int what) {
        this(what, null, null);
    }

    public CommonEvent(int what, String message) {
        this(what, message, null);
    }

    public CommonEvent(int what, String message, String messageSource) {
        mWhat = what;
        mMessage = message;
        mMessageSource = messageSource;
        if (mMessage == null) {
            mMessage = "";
        }
        if (mMessageSource == null) {
            mMessageSource = this.getClass().getSimpleName();
        }
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMessageSource() {
        return mMessageSource;
    }

    public void setMessageSource(String messageSource) {
        mMessageSource = messageSource;
    }

    public int getWhat() {
        return mWhat;
    }

    public void setWhat(int what) {
        mWhat = what;
    }

    public boolean isFrom(String messageSource) {
        return mMessageSource.equals(messageSource);
    }

    public boolean isFrom(Object object) {
        return isFrom(object.getClass().getSimpleName());
    }

    public static class Builder {
        private int mWhat;
        private String mMessage;
        private String mMessageSource;

        public Builder(int what) {
            this.mWhat = what;
        }

        public CommonEvent build() {
            return new CommonEvent(mWhat, mMessage, mMessageSource);
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setMessageSource(String messageSource) {
            this.mMessageSource = messageSource;
            return this;
        }

        public Builder setMessageSource(Object object) {
            return setMessageSource(object.getClass().getSimpleName());
        }
    }

}
