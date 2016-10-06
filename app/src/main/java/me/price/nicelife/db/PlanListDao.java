package me.price.nicelife.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.price.nicelife.bean.PlanList;
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

    /**
     * 添加一个Article
     */
    public void add(PlanList planList)
    {
        try
        {
            if(Utils.isConnect){

            }
            planListDaoOpe.create(planList);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 通过Id得到一个PlanList
     */
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

    @SuppressWarnings("unchecked")
    public List<PlanList> getPlanListAll(){
        List<PlanList> reusltList = new ArrayList<>();
        try {
            reusltList =  planListDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reusltList;
    }

    public List<PlanList> getByName(String name) {
        List<PlanList> resultList = new ArrayList<>();
        try {
            resultList = planListDaoOpe.queryBuilder()
                    .where()
                    .eq("title", name).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }


}
