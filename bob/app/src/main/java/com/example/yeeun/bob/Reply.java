package com.example.yeeun.bob;

public class Reply {
    boolean date;//1인 경우 공소시효 해당 0 인 경우 공소시효 만료
    int group;//1은 장애인 2는 13세 미만 0은 일반인
    int where;
    int what;//1. 강간 2. 강제 추행 3. 준강간준강제추행 4. 간음 5. 촬영

    boolean act;//특정 행위
    boolean num; //2명이상일때는true
    boolean hp;//협박 또는 폭행시 true
    boolean hg;//흉기 지참 여부
    boolean cousin;//4촌 이내 친족 여부
    boolean power;//위력을 갖고 있는 상태
    boolean drunken;//음주나 약물을 취한 상태

    boolean agree;
    boolean show;
    boolean money;

    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Reply(boolean date, int group, int where, int what, boolean act, boolean num, boolean hp, boolean hg, boolean cousin, boolean power, boolean drunken, boolean agree, boolean show, boolean money, String description) {
        this.date = date;
        this.group = group;
        this.where = where;
        this.what = what;
        this.act = act;
        this.num = num;
        this.hp = hp;
        this.hg = hg;
        this.cousin = cousin;
        this.power = power;
        this.drunken = drunken;
        this.agree = agree;
        this.show = show;
        this.money = money;
        this.description = description;
    }

    public boolean isAct() {
        return act;
    }

    public void setAct(boolean act) {
        this.act = act;
    }

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isMoney() {
        return money;
    }

    public void setMoney(boolean money) {
        this.money = money;
    }

    Reply(){
        this.date = false;
        this.group = 0;
        this.where = 0;
        this.what = 0;
        this.num = false;
        this.hp = false;
        this.hg = false;
        this.cousin = false;
        this.power = false;
        this.drunken = false;
        this.agree = false;
        this.show = false;
        this.money = false;

    }

    public boolean isNum() {
        return num;
    }

    public void setNum(boolean num) {
        this.num = num;
    }

    public boolean isHp() {
        return hp;
    }

    public void setHp(boolean hp) {
        this.hp = hp;
    }

    public boolean isHg() {
        return hg;
    }

    public void setHg(boolean hg) {
        this.hg = hg;
    }

    public boolean isCousin() {
        return cousin;
    }

    public void setCousin(boolean cousin) {
        this.cousin = cousin;
    }

    public boolean isPower() {
        return power;
    }

    public void setPower(boolean power) {
        this.power = power;
    }

    public boolean isDrunken() {
        return drunken;
    }

    public void setDrunken(boolean drunken) {
        this.drunken = drunken;
    }

    public Reply(boolean date, int group, int where, int what, boolean num, boolean hp, boolean hg, boolean cousin, boolean power, boolean drunken) {
        this.date = date;
        this.group = group;
        this.where = where;
        this.what = what;
        this.num = num;
        this.hp = hp;
        this.hg = hg;
        this.cousin = cousin;
        this.power = power;
        this.drunken = drunken;
    }

    public boolean isDate() {

        return date;
    }

    public void setDate(boolean date) {
        this.date = date;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getWhere() {
        return where;
    }

    public void setWhere(int where) {
        this.where = where;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public Reply(boolean date, int group, int where, int what) {
        this.date = date;
        this.group = group;
        this.where = where;
        this.what = what;
    }
}