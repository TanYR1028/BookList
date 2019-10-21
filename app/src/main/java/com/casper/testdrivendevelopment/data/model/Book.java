package com.casper.testdrivendevelopment.data.model;

import java.io.Serializable;

/**
 * Created by jszx on 2019/9/24.
 */
//book类实现序列化才能在返回的时候再进人能刚才修改的操作界面
public class Book implements Serializable {


    public Book(String title, int coverResourceId, double price) {
        this.setTitle(title);
        this.setCoverResourceId(coverResourceId);
        this.setPrice(price);
    }

    private String title;
    private int coverResourceId;
    private  double price;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }

    public void setCoverResourceId(int coverResourceId) {
        this.coverResourceId = coverResourceId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
