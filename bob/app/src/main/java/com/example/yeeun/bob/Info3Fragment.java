package com.example.yeeun.bob;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Info3Fragment extends android.support.v4.app.Fragment {


    public Info3Fragment() {
        // Required empty public constructor
    }

    static Info3Fragment newFragment() {
        Bundle args = new Bundle();
        Info3Fragment info3Fragment = new Info3Fragment();
        info3Fragment.setArguments(args);
        return info3Fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info3, container, false);
    }

}
