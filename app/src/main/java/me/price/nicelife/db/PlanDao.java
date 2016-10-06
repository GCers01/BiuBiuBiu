package me.price.nicelife.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.price.nicelife.bean.Plan;
import me.price.nicelife.bean.PlanList;


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

    /**
     * 添加一个Article
     */
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

    /**
     * 通过Id得到一个Plan
     */
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

    /**
     * 通过Id得到一篇文章
     */
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

    /**
     * 通过UserId获取所有的文章
     */
    public List<Plan> listByPlanListId(int planListId)
    {
        try
        {
            return planDaoOpe.queryBuilder().where().eq("plan_list_id", planListId)
                    .query();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<Plan> getPlanAll(){
        List<Plan> reusltList = new ArrayList<>();
        try {
            reusltList =  planDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reusltList;
    }

    public void update(Plan plan) {
        try{
            planDaoOpe.update(plan);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

}
