package me.price.nicelife.bean;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "tp_alarm")
public class Alarm {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "alarm_id")
    private int alarm_id;

    @DatabaseField(columnName = "repeat")
    private String repeat;
    @DatabaseField(columnName = "type")
    private String type;
    @DatabaseField(columnName = "start_time")
    private Date start_time;

    public Alarm(int alarm_id, String times, String type, Date start_time) {
        this.alarm_id = alarm_id;
        this.repeat = repeat;
        this.type = type;
        this.start_time = start_time;
    }
    public Alarm(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}


