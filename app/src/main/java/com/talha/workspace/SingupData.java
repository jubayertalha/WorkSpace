package com.talha.workspace;

import java.util.ArrayList;

/**
 * Created by HP-NPC on 22/05/2018.
 */

public class SingupData {
    private String name,mobile,email,pass,uId;
    private ArrayList<WorkData> works = new ArrayList<>();

    public SingupData(String name, String mobile, String email, String pass, ArrayList<WorkData> works,String uId) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.pass = pass;
        this.works = works;
        this.uId = uId;
    }
    public SingupData(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public ArrayList<WorkData> getWorks() {
        return works;
    }

    public void setWorks(ArrayList<WorkData> works) {
        this.works = works;
    }
}
