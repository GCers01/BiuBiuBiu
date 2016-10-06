package me.price.nicelife.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import me.price.nicelife.utils.Utils;

/**
 * Created by zihe on 2016/10/6.
 */
@DatabaseTable(tableName = "tp_plan")
public class Plan {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "content")
    private String content;
    @DatabaseField(columnName = "priority")
    private int priority;
    @DatabaseField(columnName = "state")
    private int state;
    @DatabaseField(columnName = "start_time")
    private Date start_time;
    @DatabaseField(canBeNull = true, foreign = true, columnName = "plan_list_id")
    private PlanList planList;
    @DatabaseField(columnName = "synchronization")
    private int synchronization;//1 已经同步 0 还没有同步
    @DatabaseField(columnName = "db_state")
    private int db_state;//0 删除 1修改 2插入

    public Plan(String title, String content, PlanList planList, int state,int priority, Date start_time, int synchronization,int db_state) {
        this.title =title;
        this.content = content;
        this.priority = priority;
        this.state = state;
        this.start_time = start_time;
        this.planList = planList;
        this.synchronization = synchronization;
        this.db_state = db_state;
    }

    public static Plan newInstance(String title, String content, int priority, Date endTime , PlanList list) {
        return new Plan(title, content, list, Utils.STATE_TODO, priority, endTime, Utils.IS_SYN, 0);
    }

    public Plan(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public PlanList getPlanList() {
        return planList;
    }

    public void setPlanList(PlanList planList) {
        this.planList = planList;
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

    @Override
    public String toString() {
        return  "ID: "+id+
                "\ntitle: "+title+"     content: "+content+
                "\nstate: " + state + " priority: " + priority+
                "\nstart_time: "+ start_time+ "    PlanList: "+planList+
                "\ntongbu: "+ this.synchronization +  "    dbState: " + db_state + "\n";
    }
}

