package com.example.yeeun.bob;

/**
 * Created by YeEun on 2018-05-31.
 */

class ListContents{
    public String msg;
    public int type;
    public ListContents(String _msg,int _type)
    {
        this.msg = _msg;
        this.type = _type;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getType() {
        return this.type;
    }
}