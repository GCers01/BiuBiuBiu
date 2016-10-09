package me.price.nicelife.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.google.gson.Gson;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.price.nicelife.MainActivity;
import me.price.nicelife.R;
import me.price.nicelife.bean.CountDown;
import me.price.nicelife.bean.Plan;
import me.price.nicelife.bean.PlanList;
import me.price.nicelife.db.CountDownDao;
import me.price.nicelife.db.PlanDao;
import me.price.nicelife.db.PlanListDao;
import me.price.nicelife.db.UserDao;
import me.price.nicelife.okhttp.OkHttpUtil;
import me.price.nicelife.utils.Utils;

/**
 * Created by jx-pc on 2016/9/23.
 */
public class MainFragment extends BaseFragment {

    View view;
    ViewPager viewPager;

    NavigationTabBar navigationTabBar;
    ArrayList<NavigationTabBar.Model> models;

    ListView listView;
    ArrayAdapter<String> itemsAdapter;
    ArrayList<String> items;

    SwipeRefreshRecyclerView countdownRecyclerView;
    CountdownAdapter countdownAdapter;
    List<CountDown> countdowns;

    SwipeRefreshRecyclerView taskRecyclerView;
    TaskAdapter taskAdapter;
    List<Plan> plans;

    ImageView touxiang;
    TextView username;

    boolean isInit = false;

    private void initNavigationTabBar() {
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
                        getActivity().getBaseContext()).inflate(R.layout.item_vp, null, false);
                TextView txtPage;

                switch (position)
                {
                    case 0:
                        pageView = LayoutInflater.from(
                                getContext()).inflate(R.layout.view_pager_task, null, false);
                        taskAdapter = new TaskAdapter();
                        refreshPlan();
                        taskRecyclerView = (SwipeRefreshRecyclerView)pageView.findViewById(R.id.taskSwipeRefresh);
                        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        taskRecyclerView.setOnListLoadListener(new SwipeRefreshAdapterView.OnListLoadListener() {
                            @Override
                            public void onListLoad() {
                                refreshPlan();
                                taskAdapter.notifyDataSetChanged();
                                taskRecyclerView.setLoading(false);
                            }
                        });
                        taskRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                taskRecyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshPlan();
                                        taskAdapter.notifyDataSetChanged();
                                        taskRecyclerView.setRefreshing(false);
                                    }
                                }, 1000);
                            }
                        });
                        taskRecyclerView.setAdapter(taskAdapter);
                        taskRecyclerView.setEmptyText("数据又没有了!");
                        refreshPlan();

                        FloatingActionButton addTaskButton = (FloatingActionButton) pageView.findViewById(R.id.create_task_fab);
                        addTaskButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((MainActivity)getActivity()).addNewFragment(CreateTaskFragment.newInstance(), "新建计划");
                            }
                        });
                        break;
                    case 1:
                        pageView = LayoutInflater.from(
                                getContext()).inflate(R.layout.view_pager_countdown, null, false);
                        countdownAdapter = new CountdownAdapter();
                        refreshCountdown();
                        countdownRecyclerView = (SwipeRefreshRecyclerView)pageView.findViewById(R.id.countdownSwipeRefresh);
                        countdownRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        countdownRecyclerView.setOnListLoadListener(new SwipeRefreshAdapterView.OnListLoadListener() {
                            @Override
                            public void onListLoad() {
                                refreshPlan();
                                countdownAdapter.notifyDataSetChanged();
                                countdownRecyclerView.setLoading(false);
                            }
                        });
                        countdownRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                countdownRecyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshCountdown();
                                        countdownAdapter.notifyDataSetChanged();
                                        countdownRecyclerView.setRefreshing(false);
                                    }
                                }, 1000);
                            }
                        });
                        countdownAdapter = new CountdownAdapter();
                        countdownRecyclerView.setAdapter(countdownAdapter);
                        countdownRecyclerView.setEmptyText("数据又没有了!");

                        FloatingActionButton addCountdownButton = (FloatingActionButton) pageView.findViewById(R.id.fab);
                        addCountdownButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((MainActivity)getActivity()).addNewFragment(CreateCountdownFragment.newInstance(), "新建倒计时");
                            }
                        });
                        break;
                    case 2:
                        pageView = LayoutInflater.from(
                                getContext()).inflate(R.layout.view_pager_mine, null, false);
                        touxiang = (ImageView) pageView.findViewById(R.id.touxiang);
                        username = (TextView) pageView.findViewById(R.id.username);
                        if(Utils.username == null) {
                            username.setText("您还未登录！");
                        }
                        else {
                            setInfo();
                        }
                        ImageView i_setting = (ImageView) pageView.findViewById(R.id.i_setting);
                        i_setting.setImageResource(R.drawable.ic_settings);
                        ImageView i_version = (ImageView) pageView.findViewById(R.id.i_version);
                        i_version.setImageResource(R.drawable.ic_add);
                        ImageView i_update = (ImageView) pageView.findViewById(R.id.i_update);
                        i_update.setImageResource(R.drawable.ic_add);
                        ImageView i_logout = (ImageView) pageView.findViewById(R.id.i_logout);
                        i_logout.setImageResource(R.drawable.ic_add);
                        ImageView i_right1 = (ImageView) pageView.findViewById(R.id.right1);
                        i_right1.setImageResource(R.drawable.ic_right);
                        ImageView i_right3 = (ImageView) pageView.findViewById(R.id.right3);
                        i_right3.setImageResource(R.drawable.ic_right);
                        ImageView i_right4 = (ImageView) pageView.findViewById(R.id.right4);
                        i_right4.setImageResource(R.drawable.ic_right);
                        ImageView i_right5 = (ImageView) pageView.findViewById(R.id.right5);
                        i_right5.setImageResource(R.drawable.ic_right);
                        View tongbu = pageView.findViewById(R.id.tongbu);
                        tongbu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MainActivity myActivity = (MainActivity) getActivity();
                                Menu menu = myActivity.getNavigationView().getMenu();
                                List<PlanList> list = new PlanListDao(getContext()).getPlanListAll();
                                for(PlanList planList : list) {
                                    for(int i=0;i<menu.size();i++) {
                                        if(menu.getItem(i).getTitle().equals(planList.getTitle())) {
                                            menu.removeItem(i);
                                            break;
                                        }
                                    }
                                }
                                final Request request = new Request.Builder()
                                        .url(Utils.webUrl + "get_all_objects")
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
                                            Log.e("login", body );
                                            Gson gson = new Gson();
                                            Utils.DBJSON dbjson = gson.fromJson(body, Utils.DBJSON.class);

                                            for (PlanList planList : dbjson.getPlanLists()) {
                                                new PlanListDao(getContext()).addLocal(planList);
                                            }
                                            for (Plan plan : dbjson.getPlans()) {
                                                for (PlanList planList : dbjson.getPlanLists()) {
                                                    if(planList.getWeb_db_id() == plan.getList_id()){
                                                        plan.setPlanList(planList);
                                                        break;
                                                    }
                                                }
                                                new PlanDao(getContext()).add(plan);
                                            }
                                            for (CountDown countDown : dbjson.getCountDowns()) {
                                                new CountDownDao(getContext()).add(countDown);
                                            }
                                            Handler handler = new Handler(Looper.getMainLooper());
                                            Runnable runnable= new Runnable() {
                                                @Override
                                                public void run() {
                                                    List<PlanList> list = new PlanListDao(getContext()).getPlanListAll();
                                                    MainActivity myActivity = (MainActivity) getActivity();
                                                    for(PlanList planList : list) {
                                                        myActivity.resetNavigationView(planList);
                                                    }
                                                    myActivity.getNavigationView().refreshDrawableState();
                                                }
                                            };
                                            handler.post(runnable);
                                        }
                                    }
                                });
                            }
                        });
                        View relogin = pageView.findViewById(R.id.relogin);
                        relogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.username = null;
                                new UserDao(getContext()).delete();
                                ((MainActivity) getActivity()).addNewFragment(LoginFragment.newInstance(), "登录");
                            }
                        });
                        break;
                    default:;
                }

                container.addView(pageView);

                return pageView;
            }
        });

        String colors[] = new String[20];
        colors[0] = "#e53935";
        colors[1] = "#D81B60";
        colors[2] = "#8E24AA";
        colors[3] = "#5E35B1";
        colors[4] = "#3949AB";
        navigationTabBar = (NavigationTabBar) view.findViewById(R.id.ntb);
        models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_name),
                        Color.parseColor(colors[0])
                ).title("Heart")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_name),
                        Color.parseColor(colors[1])
                ).title("Cup")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_name),
                        Color.parseColor(colors[2])
                ).title("Diploma")
                        .badgeTitle("state")
                        .build()
        );
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        refreshPlan();
                        break;
                    case 1:
                        refreshCountdown();
                        break;
                    case 2:
                        setInfo();
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                navigationTabBar.getModels().get(position).hideBadge();
                switch (position) {
                    case 0:
                        refreshPlan();
                        break;
                    case 1:
                        refreshCountdown();
                        break;
                    case 2:
                        setInfo();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        navigationTabBar.setTypeface("fonts/custom_font.ttf");
        navigationTabBar.setIsBadged(true);
        navigationTabBar.setIsTitled(true);
        navigationTabBar.setIsTinted(true);
        navigationTabBar.setIsBadgeUseTypeface(true);
        navigationTabBar.setBadgeBgColor(Color.RED);
        navigationTabBar.setBadgeTitleColor(Color.WHITE);
        navigationTabBar.setBehaviorEnabled(true);

        isInit = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        taskAdapter = new TaskAdapter();
        countdownAdapter = new CountdownAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
//        setInfo();
        refreshPlan();
        refreshCountdown();
    }

    public void setInfo() {
        touxiang.setImageResource(R.drawable.touxiang);
        username.setText(Utils.username);
    }

    public void refreshPlan() {
        plans = new PlanDao(getContext()).listByPlanListId(Utils.nowPlanList.getId());
        taskAdapter.notifyDataSetChanged();
    }

    public void refreshCountdown() {
        countdowns = new CountDownDao(getContext()).getAll();
        countdownAdapter.notifyDataSetChanged();
    }

    private void setListViewPager(View pageView) {
        listView = (ListView)pageView.findViewById(R.id.list_view);

        items = new ArrayList<>();

        itemsAdapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1,
                items);

        listView.setAdapter(itemsAdapter);

        items.add("First Item");
        items.add("Second Item");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNavigationTabBar();
    }

    public static MainFragment newInstance() {

        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return  fragment;
    }

    class CountdownAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return countdowns.size();
        }

        @Override
        public CountdownViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CountdownViewHolder holder = new CountdownViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.countdown_recycler_view_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            final CountDown countdown = countdowns.get(position);
            final TextView title = ((CountdownViewHolder) holder).title;
            title.setText(countdown.getTitle());
            final TextView content = ((CountdownViewHolder) holder).content;
            content.setText(countdown.getContent());
            content.measure(View.MeasureSpec.getMode(0), 0);
            ((CountdownViewHolder) holder).height = content.getMeasuredHeight();
            content.setHeight(0);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CountdownViewHolder h = ((CountdownViewHolder) holder);
                    if(h.content.getHeight() == h.height) {
                        h.content.setHeight(0);
                    }
                    else {
                        h.content.setHeight(h.height);
                    }
                }
            });

            ((CountdownViewHolder) holder).editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("isssssssssssssssd", countdown.toString());
                    ((MainActivity)getActivity()).addNewFragment(ChangeCountdownFragment.newInstance(countdown), "倒计时详情");
                }
            });
        }

        class CountdownViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            TextView content;
            Button finishButton;
            Button editButton;
            int height;

            public CountdownViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.countdown_title);
                content = (TextView) view.findViewById(R.id.contentdown_content);
                finishButton = (Button) view.findViewById(R.id.finish_button);
                editButton = (Button) view.findViewById(R.id.edit_button);
            }
        }
    }

    class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return plans.size();
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TaskViewHolder holder = new TaskViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.task_recycler_view_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            final Plan plan = plans.get(position);
            final TaskViewHolder taskViewHolder = ((TaskViewHolder) holder);
            taskViewHolder.plan = plan;
            final TextView title = taskViewHolder.title;
            title.setText(plan.getTitle());
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getActivity()).addNewFragment(ChangeTaskFragment.newInstance(plan), "计划详情");
                }
            });
            final ImageView image = taskViewHolder.image;
            if(plan.getState() == Utils.STATE_FINISH) {
                image.setImageResource(R.drawable.ic_add);
            }
            else if(plan.getState() == Utils.STATE_TODO) {
                image.setImageResource(R.drawable.ic_right);
            }
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (plan.getState() == Utils.STATE_TODO) {
                        Plan plan1 = plan;
                        plan1.setState(Utils.STATE_FINISH);
                        new PlanDao(getContext()).updateWebAndLocal(plan1);
                        image.setImageResource(R.drawable.ic_add);
                    }
                    else if(plan.getState() == Utils.STATE_FINISH) {
                        Plan plan1 = plan;
                        plan1.setState(Utils.STATE_TODO);
                        new PlanDao(getContext()).updateWebAndLocal(plan1);
                        image.setImageResource(R.drawable.ic_right);
                    }
                    taskAdapter.notifyDataSetChanged();
                }
            });
        }

        class TaskViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            ImageView image;
            Plan plan;

            public TaskViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.task_title);
                image = (ImageView) view.findViewById(R.id.btn_finish);
            }
        }
    }
}
