package me.price.nicelife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import me.price.nicelife.MainActivity;
import me.price.nicelife.R;
import me.price.nicelife.bean.PlanList;
import me.price.nicelife.db.PlanListDao;

/**
 * Created by jx-pc on 2016/10/8.
 */
public class ChangePlanListFragment extends BaseFragment {

    View view;
    MainActivity myActivity;
    AwesomeValidation awesomeValidation;
    PlanList planList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myActivity = (MainActivity)getActivity();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(),R.id.edt_title,"([\\s\\S]+)", R.string.err_title);
        final EditText titleET = (EditText) view.findViewById(R.id.edt_title);

        view.findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                    String title = titleET.getText().toString();
                    planList.setTitle(title);
                    closeKeyboard();
                    new PlanListDao(getContext()).update(planList);
                    myActivity.backFragment();
                }
            }
        });

        view.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu menu = myActivity.getNavigationView().getMenu();
                for(int i=0;i<menu.size();i++) {
                    if(menu.getItem(i).getTitle().equals(planList.getTitle())) {
                        menu.removeItem(i);
                        break;
                    }
                }
                myActivity.getNavigationView().refreshDrawableState();
                new PlanListDao(getContext()).delete(planList);
                myActivity.backFragment();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.change_plan_list_fragment, container, false);
        return view;
    }

    public static ChangePlanListFragment newInstance(PlanList planList) {

        ChangePlanListFragment fragment = new ChangePlanListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.planList = planList;
        return  fragment;
    }
}
