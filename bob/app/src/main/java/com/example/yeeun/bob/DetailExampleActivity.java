package com.example.yeeun.bob;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class DetailExampleActivity extends AppCompatActivity {
    TextView textView;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_example);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        textView = (TextView)findViewById(R.id.text);

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        parsingHTML(result);
                    }
                });
    }

    private void parsingHTML(String result){
        String word ="";
        int pt_start=-1;
        int pt_end=-1;
        String tag_start="<div class=\"pre-content3\" id=\"detailContent\">";
        String tag_end="</div>";
        pt_start=result.indexOf(tag_start);

        if(pt_start != -1){
            pt_end = result.indexOf(tag_end);
            if(pt_end!= -1){
                word = result.substring(pt_start+tag_start.length(), pt_end);
                String date[] = word.split("<br>");

                for(int i=0 ; i<date.length ; i++)
                {
                   textView.append(date[i]+"\n");
                }
            }else
                textView.append("데이터가 없습니다.");
        }else
            textView.append("데이터가 없습니다.");
    }
}
