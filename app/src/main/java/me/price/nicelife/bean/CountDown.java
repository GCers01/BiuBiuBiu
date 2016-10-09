package me.price.nicelife.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "tp_count_down")
public class CountDown {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "content")
    private String content;
    @DatabaseField(columnName = "start_time")
    private Date start_time;
    @DatabaseField(columnName = "end_time")
    private Date end_time;

    @DatabaseField(canBeNull = true, foreign = true, columnName = "alarm")
    private Alarm alarm;

    @DatabaseField(columnName = "synchronization")
    private int synchronization;//1 已经同步 0 还没有同步
    @DatabaseField(columnName = "db_state")
    private int db_state;//0 删除 1修改 2插入
    @DatabaseField(columnName = "web_db_id")
    private int web_db_id;

    public CountDown(String title, String content, Date start_time, Date end_time, Alarm alarm, int synchronization,int db_state) {
        this.title =title;
        this.content = content;
        this.end_time = end_time;
        this.start_time = start_time;

        this.synchronization = synchronization;
        this.db_state = db_state;
    }
    public CountDown(){

    }

    public static CountDown newInstancer(String title, String content, Date end_time, Alarm alarm) {

        return new CountDown(title, content, new Date(), end_time, alarm, 0, 0);
    }

    public void setWeb_db_id(int web_db_id) {
        this.web_db_id = web_db_id;
    }

    public int getWeb_db_id() {
        return web_db_id;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public int getDb_state() {
        return db_state;
    }

    public void setDb_state(int db_state) {
        this.db_state = db_state;
    }

    public int getSynchronization() {
        return synchronization;
    }

    public void setSynchronization(int synchronization) {
        this.synchronization = synchronization;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    @Override
    public String toString() {
        return  "ID: "+id+
                "\ntitle: "+title+"     content: "+content+
                "\nstart_time: "+ start_time+ "    end_time: "+end_time+
                "\ntongbu: "+ this.synchronization +  "    dbState: " + db_state + "\n"
                +"web id: " + this.web_db_id+"\n";
    }
}

