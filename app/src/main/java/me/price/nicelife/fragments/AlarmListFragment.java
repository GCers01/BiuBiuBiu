package me.price.nicelife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import me.price.nicelife.MainActivity;
import me.price.nicelife.R;
import me.price.nicelife.datas.datamanager.AlarmManager;
import me.price.nicelife.datas.datamanager.CountdownAll;
import me.price.nicelife.datas.datastruct.AlarmData;

/**
 * Created by jx-pc on 2016/10/6.
 */
public class AlarmListFragment extends BaseFragment {

    View view;

    SwipeRefreshRecyclerView alarmRecyclerView;
    AlarmAdapter alarmAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alarmRecyclerView = (SwipeRefreshRecyclerView)view.findViewById(R.id.alarmSwipeRefresh);
        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        alarmRecyclerView.setOnListLoadListener(new SwipeRefreshAdapterView.OnListLoadListener() {
            @Override
            public void onListLoad() {
//                                ++index;
//                                int count = footerIndex + 5;
//                                for (int i = footerIndex; i < count; i++) {
//                                    objects.add("上拉 = " + i);
//                                }
//                                footerIndex = count;
                alarmAdapter.notifyDataSetChanged();
                alarmRecyclerView.setLoading(false);
            }
        });
        alarmRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                alarmRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CountdownAll.refresh();
                        alarmAdapter.notifyDataSetChanged();
                        alarmRecyclerView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        alarmAdapter = new AlarmAdapter();
        alarmRecyclerView.setAdapter(alarmAdapter);
        alarmRecyclerView.setEmptyText("数据又没有了!");

        FloatingActionButton addAlarmButton = (FloatingActionButton) view.findViewById(R.id.alarm_fab);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).addNewFragment(CreateAlarmFragment.newInstance("test"), "新建闹钟提醒");
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.alarm_list_fragment, container, false);

        return view;
    }

    public static AlarmListFragment newInstance() {

        AlarmListFragment fragment = new AlarmListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return  fragment;
    }

    class AlarmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return AlarmManager.getSize();
        }

        @Override
        public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AlarmViewHolder holder = new AlarmViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.alarm_recycler_view_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            final TextView title = ((AlarmViewHolder) holder).title;
            title.setText(CountdownAll.get(position).getTitle());
            final int id = position + 1;
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmData alarm = AlarmManager.getAlarmById(id);
                    ((MainActivity) getActivity()).addNewFragment(ChangeAlarmFragment.newInstance(alarm.getId()), alarm.getTitle());
                }
            });
        }

        class AlarmViewHolder extends RecyclerView.ViewHolder{
            TextView title;
            int id;

            public AlarmViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.alarm_title);
                id = 0;
            }
        }
    }
}
