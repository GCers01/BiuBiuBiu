package me.price.nicelife.datas.datamanager;

import java.util.ArrayList;

import me.price.nicelife.datas.datastruct.Countdown;

/**
 * Created by jx-pc on 2016/10/3.
 */
public class CountdownAll extends BaseDataManager {

    private static ArrayList<Countdown> countdowns = new ArrayList<>();

    public static ArrayList<Countdown> getAll() {
        return countdowns;
    }

    public static boolean add(Countdown countdown) {
        countdowns.add(countdown);
        return true;
    }

    public static boolean change(int index, Countdown countdown) {
        countdowns.remove(index);
        countdowns.add(index, countdown);
        return true;
    }

    public static Countdown get(int index) {
        return countdowns.get(index);
    }

    public static int getSize() {
        return countdowns.size();
    }

    public static void init() {
        for(int i=0;i<10;i++) {
            countdowns.add(Countdown.newInstance("Title " + i, "Biubiubiu"));
        }
    }

    public static void refresh() {
        countdowns.clear();
        init();
    }
}
