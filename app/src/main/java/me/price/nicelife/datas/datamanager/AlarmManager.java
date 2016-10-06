package me.price.nicelife.datas.datamanager;

import android.util.Log;

import java.util.ArrayList;

import me.price.nicelife.datas.datastruct.AlarmData;

/**
 * Created by jx-pc on 2016/10/6.
 */
public class AlarmManager extends BaseDataManager {

    public static ArrayList<AlarmData> alarms = new ArrayList<>();

    public static int getSize() {
        return alarms.size();
    }

    public static ArrayList<AlarmData> getAlams() {
        return alarms;
    }

    public static AlarmData getAlarmById(int id){
        for(AlarmData alarm : alarms) {
            Log.e("TAG", Integer.toString(id) + " " + Integer.toString(alarm.getId()));
            if(alarm.getId() == id) return alarm;
        }
        return null;
    }

    public static AlarmData getAlarm(int position) {
        return alarms.get(position);
    }

    public static void add(String title) {
        alarms.add(AlarmData.newInstance(title));
    }
}
