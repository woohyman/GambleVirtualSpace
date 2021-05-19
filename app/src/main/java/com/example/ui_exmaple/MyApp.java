package com.example.ui_exmaple;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;

import com.example.ui_exmaple.manager.heartBeat;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.yanzhenjie.permission.runtime.PermissionRequest;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        heartBeat.getInstance().init();
    }
}
