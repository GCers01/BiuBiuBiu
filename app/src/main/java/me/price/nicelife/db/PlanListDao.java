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
public class PlanListDao {

    private Dao<PlanList, Integer> planListDaoOpe;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public PlanListDao(Context context)
    {
        try
        {
            helper = DatabaseHelper.getHelper(context);
            planListDaoOpe = helper.getDao(PlanList.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    ///******************************************增加一个清单************************************************
    public void add(PlanList planList)
    {
        try
        {
            planListDaoOpe.create(planList);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void addWebAndLocal(PlanList planList) {
        int syn;
        planList.setSynchronization(Utils.NOT_SYN);
        planList.setDb_state(Utils.STATE_ADD);
        this.add(planList);
        if (Utils.isConnect) {
            this.addWeb(planList);
        }
    }

    public void addWeb(final PlanList planList){
        RequestBody formBody = new FormEncodingBuilder()
                .add("title",planList.getTitle())
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "add_plan_list")
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

                        int plan_list_id = Integer.parseInt(files.get("plan_list_id"));
                        planList.setWeb_db_id(plan_list_id);
                        planList.setSynchronization(Utils.IS_SYN);
                        update(planList);
                    }
                }
            }
        });
    }

    ///*更新一个清单***********************************************************************************
    public void updateWebAndLocal(PlanList planList) {
        planList.setSynchronization(Utils.NOT_SYN);
        planList.setDb_state(Utils.STATE_MODIFY);
        this.add(planList);
        if (Utils.isConnect) {
            this.updateWeb(planList);
        }
    }

    public void updateWeb(final PlanList planList) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("plan_list_id",""+planList.getWeb_db_id())
                .add("title",planList.getTitle())
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "modify_list")
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
                        planList.setSynchronization(Utils.IS_SYN);
                        update(planList);
                    }
                }
            }
        });
    }

    public void update(PlanList planList) {
        try {
            planListDaoOpe.update(planList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //*根据id获取一个清单
    @SuppressWarnings("unchecked")
    public PlanList get(int id)
    {
        PlanList planList = null;
        try
        {
            planList = planListDaoOpe.queryForId(id);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return planList;
    }

    //*获取所有没有删除的清单
    @SuppressWarnings("unchecked")
    public List<PlanList> getPlanListAll(){
        List<PlanList> reusltList = new ArrayList<>();
        try {
            reusltList =  planListDaoOpe.queryBuilder().where().ne("db_state",Utils.STATE_DELETE).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reusltList;
    }

    public List<PlanList> getByName(String name) {
        List<PlanList> resultList = new ArrayList<>();
        try {
            resultList = planListDaoOpe.queryBuilder().
                    where()
                    .eq("title", name).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    //*删除一个清单************************************************************************************
    @SuppressWarnings("unchecked")
    public void delete(PlanList planList) {
        planList.setDb_state(Utils.STATE_DELETE);
        planList.setSynchronization(Utils.NOT_SYN);
        update(planList);

        try {
            Dao<Plan,Integer> planDao = helper.getDao(Plan.class);
            List<Plan> plans =  getPlans(planList.getId());
            for (Plan plan : plans) {
                plan.setDb_state(Utils.STATE_DELETE);
                plan.setSynchronization(Utils.NOT_SYN);
                planDao.update(plan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public void deleteWeb(final PlanList planList) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("plan_list_id",""+planList.getWeb_db_id())
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "delete_plan_list")
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
                        int plan_list_id = Integer.parseInt(files.get("plan_list_id"));
                        planList.setSynchronization(Utils.IS_SYN);
                        update(planList);

                        try {
                            Dao<Plan, Integer> planDao = helper.getDao(Plan.class);
                            List<Plan> plans = getPlans(planList.getId());
                            for (Plan plan : plans) {
                                plan.setSynchronization(Utils.IS_SYN);
                                planDao.update(plan);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }
    public void deleteWebAndLocal(PlanList planList){
        planList.setSynchronization(Utils.NOT_SYN);
        planList.setDb_state(Utils.STATE_DELETE);
        this.delete(planList);
        if (Utils.isConnect) {
            this.updateWeb(planList);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Plan> getPlans(int planListId){
        Dao<Plan,Integer> planDao = null;
        List<Plan> plans = null;
        try {
            planDao = helper.getDao(Plan.class);
            plans =  planDao.queryBuilder().where().eq("plan_list_id", planListId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plans;
    }

    //*同步到服务器
    public void synToWeb(){
        List<PlanList> planLists = getPlanListAll();
        for(PlanList planList : planLists){
            if(planList.getSynchronization()==Utils.NOT_SYN){
                switch (planList.getDb_state()){
                    case Utils.STATE_ADD:
                        addWeb(planList);
                        break;
                    case Utils.STATE_MODIFY:
                        updateWeb(planList);
                        break;
                    case Utils.STATE_DELETE:
                        deleteWeb(planList);
                        break;
                }
            }
        }
    }


}
