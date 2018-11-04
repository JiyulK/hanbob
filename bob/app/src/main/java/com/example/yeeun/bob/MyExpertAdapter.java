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
import java.util.List;

/**
 * Created by YeEun on 2018-06-01.
 */

public class MyExpertAdapter extends ArrayAdapter<Expert> {
    ArrayList<Expert> experts;
    Context context;
    private int lastPosition = -1;

    public MyExpertAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Expert> objects) {
        super(context, resource, objects);
        this.experts = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.expert_layout, null);
        }

        final Expert e = experts.get(position);

        if(e != null) {
            TextView tt1 = (TextView)v.findViewById(R.id.expertName);
            TextView tt2 = (TextView)v.findViewById(R.id.expertSubject);
            if(tt1 != null) {
                tt1.setText(e.getName());
            }
            if(tt2 != null) {
                tt2.setText(e.getSubject());
            }
        }

        Animation animation = AnimationUtils.loadAnimation(
                context, (position>this.lastPosition) ?
                        R.anim.up_from_bottom : R.anim.down_from_top);
        v.startAnimation(animation);
        this.lastPosition = position;

        return v;
    }
}
