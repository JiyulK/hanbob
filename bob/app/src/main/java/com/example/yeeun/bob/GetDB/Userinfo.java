package com.example.yeeun.bob.GetDB;

public class Userinfo {

    private String TEL;
    private String NAME;
    private String MAJOR;
    private String EMAIL;
    private String OFFICE;
    private int DEFAULTSIZE;  //6


    public Userinfo(){};

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public void setMAJOR(String MAJOR) {
        this.MAJOR = MAJOR;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setOFFICE(String OFFICE) {
        this.OFFICE = OFFICE;
    }

    public void setDEFAULTSIZE(String PIC) {
        this.DEFAULTSIZE = DEFAULTSIZE;
    }

    public void setTEL(String TEL) {
        this.TEL = TEL;
    }
    ///------get

    public String getMAJOR() {
        return MAJOR;
    }

    public String getOFFICE() {
        return OFFICE;
    }

    public String getTEL() {
        return TEL;
    }

    public int getDEFAULTSIZE() {
        return DEFAULTSIZE;
    }
    public String getNAME(){
        return NAME;
    }
    public String getEMAIL(){
        return EMAIL;
    }
}
