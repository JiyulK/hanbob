package com.example.yeeun.bob;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by YeEun on 2018-06-01.
 */

public class MyExampleAdapter extends ArrayAdapter<Example> {
    ArrayList<Example> examples;
    Context context;
    private int lastPosition = -1;

    public MyExampleAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Example> objects) {
        super(context, resource, objects);
        this.examples = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.example_layout, null);
        }

        final Example e = examples.get(position);

        if(e != null) {
            TextView tt1 = (TextView)v.findViewById(R.id.example1);
           //TextView tt2 = (TextView)v.findViewById(R.id.example2);
            if(tt1 != null) {
                tt1.setText(e.getTitle());
            }
//            if(tt2 != null) {
//                //tt2.setText(e.getUrl());
//            }
        }

        Animation animation = AnimationUtils.loadAnimation(
                context, (position>this.lastPosition) ?
                        R.anim.up_from_bottom : R.anim.down_from_top);
        v.startAnimation(animation);
        this.lastPosition = position;

        return v;
    }
}
