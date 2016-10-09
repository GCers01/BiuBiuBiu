package me.price.nicelife.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.price.nicelife.bean.Alarm;
import me.price.nicelife.bean.CountDown;
import me.price.nicelife.okhttp.OkHttpUtil;
import me.price.nicelife.utils.Utils;


public class CountDownDao {
    private Dao<CountDown, Integer> countDownDaoOpe;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public CountDownDao(Context context)
    {
        try
        {
            helper = DatabaseHelper.getHelper(context);
            countDownDaoOpe = helper.getDao(CountDown.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //add*******************************************************************
    public void addWebAndLocal(CountDown countDown) {
        countDown.setSynchronization(Utils.NOT_SYN);
        countDown.setDb_state(Utils.STATE_ADD);
        this.add(countDown);
        if (Utils.isConnect) {
            this.addWeb(countDown);
        }
    }

    public void addWeb(final CountDown countDown){
        RequestBody formBody = new FormEncodingBuilder()
                .add("title",countDown.getTitle())
                .add("content",countDown.getContent())
                .add("end_time","2016-02-06")
                .add("start_time", "2016-02-05")
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "add_count_down")
                .post(formBody)
                .build();



        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    HashMap<String, String> files = Utils.toHashMap(body);
                    if (files.get("result").equals("true")) {

                        int plan_list_id = Integer.parseInt(files.get("count_down_id"));
                        countDown.setWeb_db_id(plan_list_id);
                        countDown.setSynchronization(Utils.IS_SYN);
                        update(countDown);


                        Log.e("id", countDown.toString());

                        int temp = 1;
                    }
                }
            }
        });
    }
    public void add(CountDown countDown)
    {
        try
        {
            countDown.setDb_state(Utils.STATE_ADD);
            countDownDaoOpe.create(countDown);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }




    public CountDown get(int id)
    {
        CountDown plan  = null;
        try
        {
            plan = countDownDaoOpe.queryForId(id);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return plan;
    }


    public CountDown getWithAlarm(int id){
        CountDown plan  = null;
        try
        {
            plan = countDownDaoOpe.queryForId(id);
            helper.getDao(Alarm.class).refresh(plan.getAlarm());

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return plan;
    }

    public List<CountDown> getAll()
    {
        List<CountDown> reusltList = new ArrayList<>();
        try {
            reusltList =  countDownDaoOpe.queryBuilder().where().ne("db_state",Utils.STATE_DELETE).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reusltList;
    }

    //**update*******************************************************************
    public void updateWebAndLocal(CountDown countDown) {
        countDown.setSynchronization(Utils.NOT_SYN);
        countDown.setDb_state(Utils.STATE_MODIFY);
        this.update(countDown);
        if (Utils.isConnect) {
            this.updateWeb(countDown);
        }
    }

    public void updateWeb(final CountDown countDown){
        RequestBody formBody = new FormEncodingBuilder()
                .add("count_down_id",countDown.getWeb_db_id()+"")
                .add("title", countDown.getTitle())
                .add("content",countDown.getContent())
                .add("end_time","2016-02-06")
                .add("start_time", "2016-02-05")
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "modify_count_down")
                .post(formBody)
                .build();


        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    HashMap<String, String> files = Utils.toHashMap(body);
                    if (files.get("result").equals("true")) {
                        countDown.setSynchronization(Utils.IS_SYN);
                        update(countDown);
                    }
                }
            }
        });
    }
    public void update(CountDown countDown){
        try{

            countDownDaoOpe.update(countDown);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    //*******delete*****************************************
    public void delete(CountDown countDown){

        countDown.setDb_state(Utils.STATE_DELETE);
        countDown.setSynchronization(Utils.NOT_SYN);
        try {
            countDownDaoOpe.update(countDown);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteWebAndLocal(CountDown countDown) {

        Log.e("id", countDown.toString());

        this.delete(countDown);
        if (Utils.isConnect) {
            this.deleteWeb(countDown);
        }
    }

    public void deleteWeb(final CountDown countDown){
        RequestBody formBody = new FormEncodingBuilder()
                .add("id", countDown.getWeb_db_id()+"")
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "delete_count_down")
                .post(formBody)
                .build();

        Log.e("id", countDown.toString());

        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    HashMap<String, String> files = Utils.toHashMap(body);
                    if (files.get("result").equals("true")) {
                        countDown.setSynchronization(Utils.IS_SYN);
                        update(countDown);
                    }
                }
            }
        });
    }

    //*同步到服务器
    public void synToWeb(){
        List<CountDown> countDowns = this.getAll();
        for(CountDown countDown : countDowns){
            if(countDown.getSynchronization()==Utils.NOT_SYN){
                switch (countDown.getDb_state()){
                    case Utils.STATE_ADD:
                        addWeb(countDown);
                        break;
                    case Utils.STATE_MODIFY:
                        updateWeb(countDown);
                        break;
                    case Utils.STATE_DELETE:
                        deleteWeb(countDown);
                        break;
                }
            }
        }
    }


}
