package com.example.ui_exmaple.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ui_exmaple.R;
import com.example.ui_exmaple.manager.heartBeat;
import com.example.ui_exmaple.view.SegmentProgressBar;

import org.jetbrains.annotations.NotNull;

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
        mTextHome.StartProgress();
    }

    @Override
    public void onPause() {
        super.onPause();
        mTextHome.StopProgress();
    }
}
