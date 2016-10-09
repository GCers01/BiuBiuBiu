package me.price.nicelife.db;

import android.content.Context;

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

import me.price.nicelife.bean.Plan;
import me.price.nicelife.bean.PlanList;
import me.price.nicelife.okhttp.OkHttpUtil;
import me.price.nicelife.utils.Utils;


/**
 * Created by zihe on 2016/10/6.
 */
public class PlanDao {
    private Dao<Plan, Integer> planDaoOpe;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public PlanDao(Context context)
    {
        try
        {
            helper = DatabaseHelper.getHelper(context);
            planDaoOpe = helper.getDao(Plan.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    //add*******************************************************************
    public void addWebAndLocal(Plan plan) {
        int syn;
        plan.setSynchronization(Utils.NOT_SYN);
        plan.setDb_state(Utils.STATE_ADD);
        this.add(plan);
        if (Utils.isConnect) {
            this.addWeb(plan);
        }
    }

    public void addWeb(final Plan plan){
        RequestBody formBody = new FormEncodingBuilder()
                .add("title",plan.getTitle())
                .add("content",plan.getContent())
                .add("state",""+plan.getState())
                .add("start_time","2016-02-05")
                .add("plan_list_id",plan.getPlanList().getId()+"")
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "add_plan")
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

                        int plan_list_id = Integer.parseInt(files.get("plan_id"));
                        plan.setWeb_db_id(plan_list_id);
                        plan.setSynchronization(Utils.IS_SYN);
                        update(plan);
                    }
                }
            }
        });
    }

    public void add(Plan plan)
    {
        try
        {
            planDaoOpe.create(plan);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public Plan getPlanWithPlanList(int id)
    {
        Plan plan  = null;
        try
        {
            plan = planDaoOpe.queryForId(id);
            helper.getDao(PlanList.class).refresh(plan.getPlanList());

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return plan;
    }


    public Plan get(int id)
    {
        Plan plan  = null;
        try
        {
            plan = planDaoOpe.queryForId(id);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return plan;
    }

    public List<Plan> listByPlanListId(int planListId)
    {
        try
        {
            return planDaoOpe.queryBuilder().where().eq("plan_list_id", planListId).and().ne("db_state", Utils.STATE_DELETE)
                    .query();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return new ArrayList<Plan>();
    }

    public List<Plan> getAll(){
        try {
            return planDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    //*******update*****************************************
    public void update(Plan plan){
        try{
            planDaoOpe.update(plan);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void updateWebAndLocal(Plan plan) {
        int syn;
        plan.setSynchronization(Utils.NOT_SYN);
        plan.setDb_state(Utils.STATE_MODIFY);
        this.update(plan);
        if (Utils.isConnect) {
            this.updateWeb(plan);
        }
    }

    public void updateWeb(final Plan plan){
        RequestBody formBody = new FormEncodingBuilder()
                .add("id",plan.getWeb_db_id()+"")
                .add("title",plan.getTitle())
                .add("content",plan.getContent())
                .add("state",""+plan.getState())
                .add("start_time","2016-02-05")
                .add("plan_list_id",plan.getPlanList().getId()+"")
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "modify_plan")
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
                        plan.setSynchronization(Utils.IS_SYN);
                        update(plan);
                    }
                }
            }
        });
    }

    //*******delete*****************************************
    public void delete(Plan plan){
        plan.setDb_state(Utils.STATE_DELETE);
        plan.setSynchronization(Utils.NOT_SYN);
        update(plan);
    }
    public void deleteWebAndLocal(Plan plan) {
        this.delete(plan);
        if (Utils.isConnect) {
            this.deleteeWeb(plan);
        }
    }

    public void deleteeWeb(final Plan plan){
        RequestBody formBody = new FormEncodingBuilder()
                .add("id", plan.getWeb_db_id()+"")
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "delete_plan")
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
                        plan.setSynchronization(Utils.IS_SYN);
                        update(plan);
                    }
                }
            }
        });
    }

    //*同步到服务器
    public void synToWeb(){
        List<Plan> plans = this.getAll();
        for(Plan plan : plans){
            if(plan.getSynchronization()==Utils.NOT_SYN){
                switch (plan.getDb_state()){
                    case Utils.STATE_ADD:
                        addWeb(plan);
                        break;
                    case Utils.STATE_MODIFY:
                        updateWeb(plan);
                        break;
                    case Utils.STATE_DELETE:
                        deleteeWeb(plan);
                        break;
                }
            }
        }
    }



}
