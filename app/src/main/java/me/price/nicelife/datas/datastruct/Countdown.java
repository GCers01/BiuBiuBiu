package me.price.nicelife.datas.datastruct;

import me.price.nicelife.datas.datamanager.CountdownAll;

/**
 * Created by jx-pc on 2016/10/3.
 */
public class Countdown extends BaseData{

    private int id;
    private String title;
    private String content;
    private String ddl;

    Countdown(String title, String content) {
        this.id = CountdownAll.getSize() + 1;
        this.title = title;
        this.content = content;
    }

    Countdown(String title, String content, String ddl) {
        this.id = CountdownAll.getSize() + 1;
        this.title = title;
        this.content = content;
        this.ddl = ddl;
    }

    public static Countdown newInstance(String title, String content) {
        return new Countdown(title, content);
    }

    public static Countdown newInstance(String title, String content, String ddl) {
        return new Countdown(title, content, ddl);
    }

    public static Countdown newInstance(String title) {
        return new Countdown(title, null);
    }

    public void reset(String title, String content, String ddl) {
        this.title = title;
        this.content = content;
        this.ddl = ddl;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDdl(String ddl) {
        this.ddl = ddl;
    }

    public String getDdl() {
        return ddl;
    }
}
