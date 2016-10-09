package me.price.nicelife.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import me.price.nicelife.bean.CountDown;
import me.price.nicelife.bean.Plan;
import me.price.nicelife.bean.PlanList;
import me.price.nicelife.okhttp.OkHttpUtil;

/**
 * Created by jx-pc on 2016/10/5.
 */
public class Utils {

    public static final int INTERVAL = 1000 * 60 * 60 * 24;

    public static boolean isConnect = false;

    public  static final int IS_SYN = 0;
    public  static final  int NOT_SYN =1;
    public static  final int STATE_MODIFY = 0;
    public static final int STATE_ADD = 1;
    public  static  final int STATE_DELETE = 2;
    public static final int STATE_TODO = 0;
    public static final int STATE_FINISH = 1;

    public static PlanList nowPlanList;
    public static String username;

    public static int testNum = 100;

    public static Date string2Date(String dates) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dates);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return date;
    }

    public static String date2String(Date dates) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dates);
    }
    public static final String webUrl = "http://121.42.169.109:8090/backend/";


    /**
     * 将json格式的字符串解析成Map对象 <li>
     * json格式：{"name":"admin","retries":"3fff","testname"
     * :"ddd","testretries":"fffffffff"}
     */
    public static HashMap<String, String> toHashMap(String object)
    {
        HashMap<String, String> data = new HashMap<String, String>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Iterator it = jsonObject.keys();

        // 遍历jsonObject数据，添加到Map对象
        try {
            while (it.hasNext()) {
                String key = String.valueOf(it.next());
                String value = jsonObject.getString(key);
                data.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void synFromWeb(Context context){

        final Request request = new Request.Builder()
                .url(Utils.webUrl + "delete_plan_list")
                .build();

        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    DBJSON dbjson = gson.fromJson(response.body().charStream(),DBJSON.class);
                    //同步planlist
                    //同步plan
                    //同步alarem
                    //同步countDown
                }
            }
        });



    }
    public static class DBJSON{
        public PlanList plan_lists[];
        public Plan plans[];
        public CountDown count_downs[];



        public void setCountDowns(CountDown[] countDowns) {
            this.count_downs = countDowns;
        }

        public CountDown[] getCountDowns() {
            return count_downs;
        }

        public void setPlanLists(PlanList[] planLists) {
            this.plan_lists = planLists;
        }

        public PlanList[] getPlanLists() {
            return plan_lists;
        }

        public void setPlans(Plan[] plans) {
            this.plans = plans;
        }

        public Plan[] getPlans() {
            return plans;
        }
    }
}
