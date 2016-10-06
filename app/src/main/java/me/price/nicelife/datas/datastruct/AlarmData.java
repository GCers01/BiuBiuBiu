package me.price.nicelife.datas.datastruct;

import me.price.nicelife.datas.datamanager.AlarmManager;

/**
 * Created by jx-pc on 2016/10/6.
 */
public class AlarmData {

    private int id;
    private String title;

    AlarmData(String title) {
        this.title = title;
        this.id = AlarmManager.getSize() + 1;
    }

    public static AlarmData newInstance(String title) {
        return new AlarmData(title);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
