package me.price.nicelife.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.price.nicelife.MainActivity;
import me.price.nicelife.R;
import me.price.nicelife.datas.datamanager.CountdownAll;

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
    List<String> objects = new ArrayList<>();
    private int index = 0;
    private int footerIndex = 10;

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
                                getContext()).inflate(R.layout.view_pager_list, null, false);
                        setListViewPager(pageView);
                        break;
                    case 1:
                        pageView = LayoutInflater.from(
                                getContext()).inflate(R.layout.view_pager_countdown, null, false);
                        countdownRecyclerView = (SwipeRefreshRecyclerView)pageView.findViewById(R.id.countdownSwipeRefresh);
                        countdownRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        countdownRecyclerView.setOnListLoadListener(new SwipeRefreshAdapterView.OnListLoadListener() {
                            @Override
                            public void onListLoad() {
//                                ++index;
//                                int count = footerIndex + 5;
//                                for (int i = footerIndex; i < count; i++) {
//                                    objects.add("上拉 = " + i);
//                                }
//                                footerIndex = count;
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
                                        CountdownAll.refresh();
                                        countdownAdapter.notifyDataSetChanged();
                                        countdownRecyclerView.setRefreshing(false);
                                    }
                                }, 1000);
                            }
                        });
                        countdownAdapter = new CountdownAdapter();
                        countdownRecyclerView.setAdapter(countdownAdapter);
                        countdownRecyclerView.setEmptyText("数据又没有了!");
                        break;
                    case 2:
                        pageView = LayoutInflater.from(
                                getContext()).inflate(R.layout.view_pager_mine, null, false);
                        txtPage = (TextView) pageView.findViewById(R.id.txt_vp_item_page);
                        txtPage.setText(String.format("Page #%d\nmine", position));
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

            }

            @Override
            public void onPageSelected(int position) {
                navigationTabBar.getModels().get(position).hideBadge();
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

        initNavigationTabBar();

        return view;
    }

    public static MainFragment newInstance() {

        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return  fragment;
    }

    class CountdownAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private int active = -1;

        @Override
        public int getItemCount() {
            return CountdownAll.getSize();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.countdown_recycler_view_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final TextView title = ((MyViewHolder) holder).title;
            title.setText(CountdownAll.get(position).getTitle());
            final TextView content = ((MyViewHolder) holder).content;
            content.setText(CountdownAll.get(position).getContent());
            content.measure(View.MeasureSpec.getMode(0), 0);
            ((MyViewHolder) holder).height = content.getMeasuredHeight();
//            final Button button = ((MyViewHolder) holder).editButton;
//            button.measure(View.MeasureSpec.getMode(0), 0);
//            ((MyViewHolder) holder).buttonHeight = button.getMeasuredHeight();
            if(position != active) {
                content.setHeight(0);
//                button.setHeight(0);
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            TextView content;
            Button finishButton;
            Button editButton;
            int height;
            int buttonHeight;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.countdown_title);
                content = (TextView) view.findViewById(R.id.contentdown_content);
                finishButton = (Button) view.findViewById(R.id.finish_button);
                editButton = (Button) view.findViewById(R.id.edit_button);

                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(content.getHeight() == height) {
                            content.setHeight(0);
                            editButton.setHeight(0);
                        }
                        else {
                            content.setHeight(height);
                            editButton.setHeight(buttonHeight);
                        }
                    }
                });
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).addNewFragment(CreateCountdownFragment.newInstance(), title.getText().toString());
                    }
                });
            }
        }
    }
}
