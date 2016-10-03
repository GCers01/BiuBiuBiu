package me.price.nicelife.datas.datastruct;

import me.price.nicelife.datas.datamanager.CountdownAll;

/**
 * Created by jx-pc on 2016/10/3.
 */
public class Countdown extends BaseData{

    private int id;
    private String title;
    private String content;

    Countdown(String title, String content) {
        this.id = CountdownAll.getSize() + 1;
        this.title = title;
        this.content = content;
    }

    public static Countdown newInstance(String title, String content) {
        return new Countdown(title, content);
    }

    public static Countdown newInstance(String title) {
        return new Countdown(title, null);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }
}
