package com.casper.testdrivendevelopment;

/**
 * Created by jszx on 2019/9/24.
 */

public class Book {
    public Book(String title, int coverResoourseId) {
        this.setTitle(title);
        this.setCoverResoourseId(coverResoourseId);
    }

    private String title;
    private int coverResoourseId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCoverResoourseId() {
        return coverResoourseId;
    }

    public void setCoverResoourseId(int coverResoourseId) {
        this.coverResoourseId = coverResoourseId;
    }
}
