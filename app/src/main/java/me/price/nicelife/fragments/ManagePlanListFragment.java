package me.price.nicelife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.util.List;

import me.price.nicelife.MainActivity;
import me.price.nicelife.R;
import me.price.nicelife.bean.PlanList;
import me.price.nicelife.db.PlanListDao;

/**
 * Created by jx-pc on 2016/10/8.
 */
public class ManagePlanListFragment extends BaseFragment {

    View view;
    ViewPager viewPager;
    MainActivity myActivity;
    List<PlanList> planLists;
    SwipeRefreshRecyclerView planListRecyclerView;
    PlanListAdapter planListAdapter;

    private void initViewPager() {
        viewPager = (ViewPager) view.findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {

                View pageView = LayoutInflater.from(
                        getContext()).inflate(R.layout.view_pager_planlist, null, false);
                planListAdapter = new PlanListAdapter();
                refreshPlanList();
                planListRecyclerView = (SwipeRefreshRecyclerView) pageView.findViewById(R.id.planListSwipeRefresh);
                planListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                planListRecyclerView.setOnListLoadListener(new SwipeRefreshAdapterView.OnListLoadListener() {
                    @Override
                    public void onListLoad() {
                        refreshPlanList();
                        planListAdapter.notifyDataSetChanged();
                        planListRecyclerView.setLoading(false);
                    }
                });
                planListRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        planListRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refreshPlanList();
                                planListAdapter.notifyDataSetChanged();
                                planListRecyclerView.setRefreshing(false);
                            }
                        }, 1000);
                    }
                });
                planListRecyclerView.setAdapter(planListAdapter);
                planListRecyclerView.setEmptyText("数据又没有了!");
                refreshPlanList();

                FloatingActionButton addPlanlistButton = (FloatingActionButton) pageView.findViewById(R.id.plan_list_fab);
                addPlanlistButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MainActivity) getActivity()).addNewFragment(CreatePlanListFragment.newInstance(), "新建清单");
                    }
                });

                container.addView(pageView);

                return pageView;
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.manage_plan_list_fragment, container, false);
        return view;
    }

    public void refreshPlanList() {
        planLists = new PlanListDao(getContext()).getPlanListAll();
        planListAdapter.notifyDataSetChanged();
    }

    public static ManagePlanListFragment newInstance() {

        ManagePlanListFragment fragment = new ManagePlanListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return  fragment;
    }

    class PlanListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return planLists.size();
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TaskViewHolder holder = new TaskViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.planlist_recycler_view_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final PlanList planList = planLists.get(position);
            TaskViewHolder taskViewHolder = ((TaskViewHolder) holder);
            taskViewHolder.planList = planList;
            final TextView title = ((TaskViewHolder) holder).title;
            title.setText(planList.getTitle());
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getActivity()).addNewFragment(ChangePlanListFragment.newInstance(planList), "清单详情");
                }
            });
        }

        class TaskViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            PlanList planList;

            public TaskViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.plan_list_title);
            }
        }
    }
}
