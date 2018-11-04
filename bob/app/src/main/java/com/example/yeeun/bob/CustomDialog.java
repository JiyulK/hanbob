package com.example.yeeun.bob;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * Created by S on 2018-06-10.
 */

public class CustomDialog extends Dialog {
    TextView tv;
    TextView title;
    Button bt;


    public CustomDialog(@NonNull Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.law_detail);
        title = (TextView)findViewById(R.id.title);
        tv = (TextView)findViewById(R.id.content);

        bt = (Button)findViewById(R.id.closeBtn);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ButterKnife.bind(this);
    }
    public void setLawDetail(String str, String str2){

        title.setText(str2);
        tv.setText(str);
    }

}
