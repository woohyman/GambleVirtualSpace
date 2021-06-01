package com.woohyman.swipe_assistant;

import android.text.TextUtils;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.Rationale;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PermissionProxy {
    private final Rationale<List<String>> mRationale;
    private final Action<List<String>> mGranted;
    private final Action<List<String>> mDenied;

    protected PermissionProxy(Rationale<List<String>> rationale, Action<List<String>> granted, Action<List<String>> denied, String mKeySource) {
        mRationale = RationaleProxy(rationale);
        mGranted = GrantedProxy(granted, mKeySource);
        mDenied = DeniedProxy(denied, mKeySource);
    }

    private Rationale<List<String>> RationaleProxy(Rationale<List<String>> rationale) {
        return (context, data, executor) -> {
            for (String item : data) {
                Timber.tag(PermissionProxy.class.getSimpleName()).i("RationaleProxy|item ==> %s",item);
            }
            rationale.showRationale(context, data, executor);
        };
    }

    private Action<List<String>> GrantedProxy(Action<List<String>> granted, String mKeySource) {
        return data -> {
            for (String item : data) {
                Timber.tag(PermissionProxy.class.getSimpleName()).i("GrantedProxy|item ==> %s",item);
            }
            granted.onAction(data);
        };
    }

    private Action<List<String>> DeniedProxy(Action<List<String>> denied, String mKeySource) {
        return data -> {
            for (String item : data) {
                Timber.tag(PermissionProxy.class.getSimpleName()).i("DeniedProxy|item ==> %s",item);
            }
            denied.onAction(data);
        };
    }

    public Action<List<String>> getDenied() {
        return mDenied;
    }

    public Action<List<String>> getGranted() {
        return mGranted;
    }

    public Rationale<List<String>> getRationale() {
        return mRationale;
    }

    public static class Builder {
        private Rationale<List<String>> rawRationale;
        private Action<List<String>> rawGranted;
        private Action<List<String>> rawDenied;
        private String mKeySource;

        public Builder setRawRationale(@NotNull Rationale<List<String>> rawRationale) {
            this.rawRationale = rawRationale;
            return this;
        }

        public Builder setRawGranted(@NotNull Action<List<String>> rawGranted) {
            this.rawGranted = rawGranted;
            return this;
        }

        public Builder setRawDenied(@NotNull Action<List<String>> rawDenied) {
            this.rawDenied = rawDenied;
            return this;
        }

        public Builder setKeySource(@NotNull String keySource) {
            mKeySource = keySource;
            return this;
        }

        public PermissionProxy create() {
            return new PermissionProxy(rawRationale, rawGranted, rawDenied, mKeySource);
        }
    }
}
