package com.example.ui_exmaple.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ui_exmaple.PermissionProxy;
import com.example.ui_exmaple.R;
import com.example.ui_exmaple.widget.SegmentProgressBar;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class HomeFragment extends Fragment {
    private SegmentProgressBar mTextHome = null;
    private View mRoot;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_home, container, false);
        mTextHome = mRoot.findViewById(R.id.text_home);
        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();

        mTextHome.start();

        String keySource = HomeFragment.class.getSimpleName();
//        PermissionProxy proxy = new PermissionProxy.Builder()
//                .setKeySource(keySource)
//                .setRawRationale((context, data, executor) -> {
//                    Timber.tag(keySource).i("== RawRationale ==");
//                })
//                .setRawGranted(data -> {
//                    Timber.tag(keySource).i("== RawGranted ==");
//                })
//                .setRawDenied(permissions -> {
//                    Timber.tag(keySource).i("== RawDenied ==");
//                })
//                .create();
//
//        AndPermission.with(this)
//                .runtime()
//                .permission(Permission.CAMERA)
//                .rationale(proxy.getRationale())
//                .onGranted(proxy.getGranted())
//                .onDenied(proxy.getDenied())
//                .start();
    }

    @Override
    public void onPause() {
        mTextHome.stop();
        super.onPause();
    }
}
