package com.example.yeeun.bob;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Info2Fragment extends android.support.v4.app.Fragment {


    public Info2Fragment() {
        // Required empty public constructor
    }

    static Info2Fragment newFragment() {
        Bundle args = new Bundle();
        Info2Fragment info2Fragment = new Info2Fragment();
        info2Fragment.setArguments(args);
        return info2Fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info2, container, false);
    }

}
