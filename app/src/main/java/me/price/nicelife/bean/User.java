package me.price.nicelife.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zihe on 2016/10/8.
 */
@DatabaseTable(tableName = "tp_user")
public class User {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "username")
    private String username;

    public User(String username) {
        this.username = username;
    }
    public User(){

    }

    public static User newInstance(String username) {
        return new User(username);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
