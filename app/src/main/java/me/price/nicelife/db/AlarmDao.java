package me.price.nicelife.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.price.nicelife.bean.Alarm;


/**
 * Created by zihe on 2016/10/7.
 */
public class AlarmDao {
    private Dao<Alarm, Integer> alarmDaoOpe;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public AlarmDao(Context context)
    {
        try
        {
            helper = DatabaseHelper.getHelper(context);
            alarmDaoOpe = helper.getDao(Alarm.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public void add(Alarm alarm)
    {
        try
        {
            alarmDaoOpe.create(alarm);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 通过Id得到一个PlanList
     */
    @SuppressWarnings("unchecked")
    public Alarm get(int id)
    {
        Alarm planList = null;
        try
        {
            planList = alarmDaoOpe.queryForId(id);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return planList;
    }

    @SuppressWarnings("unchecked")
    public List<Alarm> getPlanListAll(){
        List<Alarm> reusltList = new ArrayList<>();
        try {
            reusltList =  alarmDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reusltList;
    }
}
