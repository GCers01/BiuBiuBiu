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
import me.price.nicelife.bean.Plan;
import me.price.nicelife.db.PlanDao;
import me.price.nicelife.utils.Utils;

/**
 * Created by jx-pc on 2016/10/6.
 */
public class ChangeTaskFragment extends BaseFragment {

    Plan plan;
    View view;
    MainActivity myActivity;

    AwesomeValidation awesomeValidation;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        myActivity = (MainActivity) getActivity();
        awesomeValidation.addValidation(getActivity(),R.id.edt_title,"([\\s\\S]+)", R.string.err_title);
        awesomeValidation.addValidation(getActivity(),R.id.edt_content,"([\\s\\S]*)", R.string.err_content);
        awesomeValidation.addValidation(getActivity(),R.id.edt_priority, "([\\s\\S]*)", R.string.err_priority);
        awesomeValidation.addValidation(getActivity(),R.id.limitedDate,"([\\s\\S]*)" , R.string.err_year);

        final EditText prioritys = (EditText) view.findViewById(R.id.edt_priority);
        prioritys.setText("" + plan.getPriority());
        final EditText limitedDate = (EditText) view.findViewById(R.id.limitedDate);
        limitedDate.setText(Utils.date2String(plan.getStart_time()));
        final EditText edtContent = (EditText) view.findViewById(R.id.edt_content);
        edtContent.setText(plan.getContent());
        final EditText edtTitle = (EditText) view.findViewById(R.id.edt_title);
        edtTitle.setText(plan.getTitle());

        view.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                    String title = edtTitle.getText().toString();
                    String content = edtContent.getText().toString();
                    String ddl = limitedDate.getText().toString();
                    int priority = Integer.parseInt(prioritys.getText().toString());

                    plan.setTitle(title);
                    plan.setContent(content);
                    plan.setStart_time(Utils.string2Date(ddl));
                    plan.setPriority(priority);
                    new PlanDao(getContext()).updateWebAndLocal(plan);
                    closeKeyboard();
                    myActivity.backFragment();
                }
            }
        });

        view.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PlanDao(getContext()).deleteWebAndLocal(plan);
//                MainFragment fragment = (MainFragment) myActivity.getBackFragment();
//                fragment.refreshPlan();
                myActivity.backFragment();
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.change_task_fragment, container, false);

        return view;
    }

    public static ChangeTaskFragment newInstance(Plan plan) {
        ChangeTaskFragment fragment = new ChangeTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.plan = plan;
        return  fragment;
    }
}
