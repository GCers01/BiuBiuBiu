package me.price.nicelife.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import java.util.ArrayList;

import me.price.nicelife.R;

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

                View view = LayoutInflater.from(
                        getActivity().getBaseContext()).inflate(R.layout.item_vp, null, false);
                TextView txtPage;

                switch (position)
                {
                    case 0:
                        setListViewPager(view);
//                        view = LayoutInflater.from(
//                                getActivity().getBaseContext()).inflate(R.layout.view_pager_countdown, null, false);
//                        txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
//                        txtPage.setText(String.format("Page #%d\ncountdown", position));
                        break;
                    case 1:
                        view = LayoutInflater.from(
                                getActivity().getBaseContext()).inflate(R.layout.view_pager_countdown, null, false);
                        txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
                        txtPage.setText(String.format("Page #%d\ncountdown", position));
                        break;
                    case 2:
                        view = LayoutInflater.from(
                                getActivity().getBaseContext()).inflate(R.layout.view_pager_mine, null, false);
                        txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
                        txtPage.setText(String.format("Page #%d\nmine", position));
                        break;
                    default:;
                }

                container.addView(view);

                return view;
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

    private void setListViewPager(View _view) {
        _view = LayoutInflater.from(
                getActivity().getBaseContext()).inflate(R.layout.view_pager_list, null, false);

        listView = (ListView)_view.findViewById(R.id.list_view);

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
}
