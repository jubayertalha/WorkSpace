package com.talha.workspace;

/**
 * Created by HP-NPC on 21/05/2018.
 */

public class WorkData {
    private String title,description,time;
    private int star,status,id;

    public WorkData(String title, String description, String time, int star, int status,int id) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.star = star;
        this.status = status;
        this.id = id;

    }
    public WorkData(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
