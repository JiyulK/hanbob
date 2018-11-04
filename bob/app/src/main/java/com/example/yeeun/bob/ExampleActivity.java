package com.example.yeeun.bob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.util.Charsets;
import com.koushikdutta.ion.Ion;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ExampleActivity extends AppCompatActivity {
    ArrayList<Example> examples; // 구분번호에 따른 판례를 담을 arraylist
    // 판례를 클릭하면 인터넷으로 url 전달해주기

    ListView exampleList;
    MyExampleAdapter adapter;

    private String key ="PyOLPxBZbJeAAV3kd0yHuWO9msxxQbrEDmEZAObfqyTc0m1sniwmOT3F15WHP68XuL6uYoUmNgNriaC42uyeGw==";

    // 수정
    String str =
            "http://openapi.ccourt.go.kr/openapi/services/PrecedentSearchSvc/getPrcdntSearchInfo?ServiceKey="
                    +key+"&eventNm=";
    String[] arr = {"성폭력범죄의","처벌","등에","관한","특례법","제32조"};

    ArrayList<String> law_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        init();
    }

    public void init() {
        examples = new ArrayList<>();
        exampleList = (ListView)findViewById(R.id.exampleList);

        Intent intent = getIntent();
        law_data = intent.getStringArrayListExtra("law_data");


        for(int j = 0; j < law_data.size(); j++) {
            // 수정, 여기서 string으로 법 이름 받아주기
            arr[5] = "제"+law_data.get(j);
            Toast.makeText(this, law_data.get(j), Toast.LENGTH_SHORT);
            // 테스트용!
            if(arr[5].equals("제4조")) {
                textExamples();
                continue;
            }

            String text = "";
            try {
                for (int i = 0; i < arr.length; i++) {
                    text = URLEncoder.encode(arr[i], "UTF8");
                    str += text;
                    str += "%20";
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getApplication(), str,Toast.LENGTH_SHORT).show();

            // 그래들에 implementation 'com.koushikdutta.ion:ion:2.+' 넣어주기
            Ion.with(this).load(str)
                    .asString(Charsets.UTF_8)
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            //Toast.makeText(getApplication(), result,Toast.LENGTH_SHORT).show();
                            parsingXML(result);
                            //Toast.makeText(getApplicationContext(), "파싱완료", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

        adapter = new MyExampleAdapter(getApplicationContext(), R.layout.example_layout, examples);
        adapter.setNotifyOnChange(true);
        exampleList.setAdapter(adapter);

        // 리스너 달기
        exampleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = examples.get(position).getUrl();
                Intent intent = new Intent(ExampleActivity.this, DetailExampleActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

    }

    public void addExamples(Example e) { // 여기서 exNum에 해당하는 판례를 찾아서 add 시켜준다.
        examples.add(e);
        //Toast.makeText(this, "추가", Toast.LENGTH_SHORT).show();
    }



    private void parsingXML(String result) {
        Example ee = new Example();
        XmlPullParserFactory factory= null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(result));

            boolean bitem = false;
            boolean bset = false;
            boolean check = false;
            String title = ""; String url =  ""; String id = "";
            String tag_name = "";

            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {

                if(eventType == XmlPullParser.START_DOCUMENT) {
                    ;
                } else if(eventType == XmlPullParser.START_TAG) {
                    tag_name = xpp.getName();
                    if(tag_name.equals("item")) {
                        bitem = true;
                    } else if(bitem) {
                        if(tag_name.equals("eventNo")||tag_name.equals("eventNm")||tag_name.equals("eventNum")) {
                            bset = true;
                        }
                    }
                } else if(eventType == XmlPullParser.TEXT) {
                    if(bset) {
                        if(tag_name.equals("eventNo")) {
                            url = xpp.getText();
                            ee.setUrl(url);
                        } else if(tag_name.equals("eventNm")) {
                            title = xpp.getText();
                            ee.setTitle(title+url);
                            check = true;
                        } else if(tag_name.equals("eventNum")) {
                            id = xpp.getText();
                            ee.setId(Integer.valueOf(id));
                        }
                        bset = false;
                        if(tag_name.equals("eventNm")) {
                            bitem = false;
                        }
                    }
                    if(check) {
                        // 객체에 필요한 정보가 다 받아와질때
                        addExamples(ee);
                        ee = new Example();
                        check = false;
                    }
                } else if(eventType == XmlPullParser.END_TAG) {
                    ;
                }
                eventType = xpp.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }


    public void textExamples() {
        String[] test = {"2015헌바136", "2016헌바368" , "2014헌마340" , "2014헌바436" , "2016헌바258" };
        for(int i = 0; i < 5; i++) {
            Example e = new Example();
            e.setUrl(test[i]);
            e.setTitle("성폭력범죄의 처벌 등에 관한 특례법 제 4조"+test[i]);
            examples.add(e);
        }
    }
}
