package me.price.nicelife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
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
public class CreatePlanListFragment extends BaseFragment {

    View view;
    MainActivity myActivity;
    AwesomeValidation awesomeValidation;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myActivity = (MainActivity)getActivity();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(),R.id.edt_title,"([\\s\\S]+)", R.string.err_title);
        final EditText titleET = (EditText) view.findViewById(R.id.edt_title);

        view.findViewById(R.id.btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                    String title = titleET.getText().toString();
                    closeKeyboard();
                    PlanList planList = PlanList.newInstance(title);
                    new PlanListDao(getContext()).add(planList);
                    myActivity.backFragment();
                    myActivity.resetNavigationView(planList);
                }
            }
        });

        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActivity.backFragment();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_plan_list_fragment, container, false);
        return view;
    }

    public static CreatePlanListFragment newInstance() {

        CreatePlanListFragment fragment = new CreatePlanListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return  fragment;
    }
}
