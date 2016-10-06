package me.price.nicelife.fragments;

import android.app.Activity;
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
import me.price.nicelife.bean.PlanList;
import me.price.nicelife.db.PlanDao;
import me.price.nicelife.db.PlanListDao;
import me.price.nicelife.utils.Utils;

/**
 * Created by jx-pc on 2016/10/6.
 */
public class CreateTaskFragment extends BaseFragment {

    View view;
    Activity myActivity;

    AwesomeValidation awesomeValidation;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        myActivity = getActivity();
        awesomeValidation.addValidation(getActivity(),R.id.edt_title,"([\\s\\S]+)", R.string.err_title);
        awesomeValidation.addValidation(getActivity(),R.id.edt_content,"([\\s\\S]*)", R.string.err_content);
        awesomeValidation.addValidation(getActivity(),R.id.edt_priority, "([\\s\\S]*)", R.string.err_priority);
        awesomeValidation.addValidation(getActivity(),R.id.limitedDate,"([\\s\\S]*)" , R.string.err_year);

        view.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                    EditText edtTitle = (EditText) view.findViewById(R.id.edt_title);
                    String title = edtTitle.getText().toString();
                    EditText edtContent = (EditText) view.findViewById(R.id.edt_content);
                    String content = edtContent.getText().toString();
                    EditText limitedDate = (EditText) view.findViewById(R.id.limitedDate);
                    String ddl = limitedDate.getText().toString();
                    EditText prioritys = (EditText) view.findViewById(R.id.edt_priority);
                    int priority = Integer.parseInt(prioritys.getText().toString());
                    PlanList planList = null;
                    if(planList == null) {
                        planList = new PlanListDao(getContext()).getByName("default").get(0);
                    }
                    new PlanDao(getContext()).add(Plan.newInstance(title, content, priority, Utils.string2Date(ddl), planList));
                    closeKeyboard();
                    ((MainActivity)myActivity).backFragment();
                }
            }
        });

        view.findViewById(R.id.btn_clr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awesomeValidation.clear();
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.create_task_fragment, container, false);

        return view;
    }

    public static CreateTaskFragment newInstance() {
        CreateTaskFragment fragment = new CreateTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return  fragment;
    }
}
