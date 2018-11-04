package com.example.yeeun.bob;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by YeEun on 2018-06-01.
 */

public class Example implements Serializable { // 판례 정보 클래스
    String url;
    String title;
    int id;

    public Example() {

    }

    public Example(String url, String title, int id) {
        this.url = url;
        this.title = title;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        String temp ="";
        try {
            temp = URLEncoder.encode(url, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.url = "http://www.ccourt.go.kr/cckhome/comn/event/eventSearchInfoView.do?searchType=1&viewType=3&changeEventNo="+temp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
