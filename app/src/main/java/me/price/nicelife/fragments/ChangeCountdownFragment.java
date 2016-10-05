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
import me.price.nicelife.datas.datamanager.CountdownAll;
import me.price.nicelife.datas.datastruct.Countdown;

/**
 * Created by jx-pc on 2016/10/5.
 */
public class ChangeCountdownFragment extends BaseFragment {


    View view;
    Activity myActivity;
    int id;

    EditText edtTitle;
    EditText edtContent;
    EditText limitedDate;

    Countdown countdown;

    AwesomeValidation awesomeValidation;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countdown = CountdownAll.get(id);

        edtTitle = (EditText) view.findViewById(R.id.edt_title);
        edtTitle.setText(countdown.getTitle());
        edtContent = (EditText) view.findViewById(R.id.edt_content);
        edtContent.setText(countdown.getContent());
        limitedDate = (EditText) view.findViewById(R.id.limitedDate);
        limitedDate.setText(countdown.getDdl());

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        myActivity = getActivity();
        awesomeValidation.addValidation(getActivity(), R.id.edt_title,"([\\s\\S]+)", R.string.err_title);
        awesomeValidation.addValidation(getActivity(),R.id.edt_content,"([\\s\\S]*)", R.string.err_content);
        awesomeValidation.addValidation(getActivity(),R.id.limitedDate,"([\\s\\S]*)" , R.string.err_year);


        view.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                    String title = edtTitle.getText().toString();
                    String content = edtContent.getText().toString();
                    String ddl = limitedDate.getText().toString();
                    countdown.reset(title, content, ddl);
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

        view = inflater.inflate(R.layout.create_countdown_fragment, container, false);

        return view;
    }

    public static ChangeCountdownFragment newInstance(int id) {

        ChangeCountdownFragment fragment = new ChangeCountdownFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.id = id;
        return  fragment;
    }
}
