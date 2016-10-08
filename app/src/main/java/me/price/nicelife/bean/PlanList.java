package me.price.nicelife.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import me.price.nicelife.utils.Utils;

/**
 * Created by zihe on 2016/10/6.
 */

@DatabaseTable(tableName = "tp_plan_list")
public class PlanList {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "synchronization")
    private int synchronization;
    @DatabaseField(columnName = "db_state")
    private int db_state;
    @DatabaseField(columnName = "web_db_id")
    private int web_db_id;

    public PlanList(){

    }
    public PlanList(String title,int synchronization,int db_state){
        this.title = title;
        this.synchronization = synchronization;
        this.db_state = db_state;
    }
    public PlanList(String title,int synchronization,int db_state,int web_db_id){
        this.title = title;
        this.synchronization = synchronization;
        this.db_state = db_state;
        this.web_db_id = web_db_id;
    }

    public static PlanList newInstance(String title) {
        return new PlanList(title, Utils.IS_SYN, Utils.STATE_ADD);
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDb_state() {
        return db_state;
    }

    public void setDb_state(int db_state) {
        this.db_state = db_state;
    }

    public void setSynchronization(int synchronization) {
        this.synchronization = synchronization;
    }

    public int getSynchronization() {
        return synchronization;
    }

    public void setWeb_db_id(int web_db_id) {
        this.web_db_id = web_db_id;
    }

    public int getWeb_db_id() {
        return web_db_id;
    }

    @Override
    public String toString() {
        return "id: "+ id+"    title: " + this.title+"    synchronization: "+synchronization+"    db_state: "+db_state+"\n";
    }
}
