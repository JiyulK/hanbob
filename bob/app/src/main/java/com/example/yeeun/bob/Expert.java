package com.example.yeeun.bob;

import java.io.Serializable;

/**
 * Created by YeEun on 2018-06-01.
 */

public class Expert implements Serializable { // 전문가 클래스
    String name;
    String subject;
    String phoneNumber;
    String photo; // 쓸지 안쓸지 모르겠음
    String email;
    String addr;

    int id;

    public Expert(String name, String subject, String phoneNumber) {
        this.name = name;
        this.subject = subject;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
