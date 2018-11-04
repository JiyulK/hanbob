package com.example.yeeun.bob;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Info1Fragment extends android.support.v4.app.Fragment {


    public Info1Fragment() {
        // Required empty public constructor
    }

    public static Info1Fragment newFragment() {
        Bundle args = new Bundle();
        Info1Fragment info1Fragment = new Info1Fragment();
        info1Fragment.setArguments(args);
        return info1Fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info1, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
