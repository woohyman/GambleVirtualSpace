package com.example.ui_exmaple;

import android.util.Log;

import androidx.annotation.NonNull;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.runtime.PermissionRequest;

import java.util.List;

public class PermissionProxy implements PermissionRequest {
    private PermissionRequest mPermissionRequest;
    private String mKeySource;

    private PermissionProxy(PermissionRequest permissionRequest) {
        mPermissionRequest = permissionRequest;
    }

    public PermissionProxy setKeySource(String keySource) {
        mKeySource = keySource;
        return this;
    }

    public static PermissionProxy getRequestProxy(PermissionRequest permissionRequest) {
        return new PermissionProxy(permissionRequest);
    }

    @Override
    public PermissionRequest onGranted(@NonNull Action<List<String>> granted) {
        Action<List<String>> action = data -> {
            for (String item : data) {
                Log.i("test001", "onGranted ==> " + item);
            }
            Log.i("test001", "onGranted|mKeySource ==> " + mKeySource);
            granted.onAction(data);
        };
        return mPermissionRequest.onGranted(action);
    }

    @Override
    public PermissionRequest onDenied(@NonNull Action<List<String>> denied) {
        Action<List<String>> action = data -> {
            for (String item : data) {
                Log.i("test001", "onDenied ==> " + item);
            }
            Log.i("test001", "onDenied|mKeySource ==> " + mKeySource);
            denied.onAction(data);
        };
        return mPermissionRequest.onDenied(action);
    }

    @Override
    public void start() {
        mPermissionRequest.start();
    }

    @Override
    public PermissionRequest permission(@NonNull String... permissions) {
        return mPermissionRequest.permission(permissions);
    }

    @Override
    public PermissionRequest permission(@NonNull String[]... groups) {
        return mPermissionRequest.permission(groups);
    }

    @Override
    public PermissionRequest rationale(@NonNull Rationale<List<String>> rationale) {
        return mPermissionRequest.rationale(rationale);
    }
}
