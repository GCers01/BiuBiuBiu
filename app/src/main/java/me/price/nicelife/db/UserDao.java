package me.price.nicelife.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import me.price.nicelife.bean.User;


/**
 * Created by zihe on 2016/10/8.
 */
public class UserDao {
    private Dao<User, Integer> userDaoOpe;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public UserDao(Context context)
    {
        try
        {
            helper = DatabaseHelper.getHelper(context);
            userDaoOpe = helper.getDao(User.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public void add(User user)
    {
        try
        {
            userDaoOpe.create(user);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void delete()
    {
        try
        {
            User user = this.get();
            if(user!=null)
                userDaoOpe.delete(user);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 通过Id得到一个PlanList
     */
    @SuppressWarnings("unchecked")
    public User get()
    {
        User user = null;
        List<User> users;
        try
        {
            users = userDaoOpe.queryForAll();
            if(users == null || users.size()==0)
                return null;
            return users.get(0);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return user;
    }
}
