package me.price.nicelife.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import me.price.nicelife.MainActivity;
import me.price.nicelife.R;
import me.price.nicelife.bean.CountDown;
import me.price.nicelife.db.CountDownDao;
import me.price.nicelife.utils.Utils;

/**
 * Created by jx-pc on 2016/10/5.
 */
public class ChangeCountdownFragment extends BaseFragment {


    View view;
    Activity myActivity;
    CountDown countdown;

    EditText edtTitle;
    EditText edtContent;
    EditText limitedDate;

    AwesomeValidation awesomeValidation;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtTitle = (EditText) view.findViewById(R.id.edt_title);
        edtTitle.setText(countdown.getTitle());
        edtContent = (EditText) view.findViewById(R.id.edt_content);
        edtContent.setText(countdown.getContent());
        limitedDate = (EditText) view.findViewById(R.id.limitedDate);
        limitedDate.setText(Utils.date2String(countdown.getEnd_time()));

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        myActivity = getActivity();
        awesomeValidation.addValidation(getActivity(), R.id.edt_title,"([\\s\\S]+)", R.string.err_title);
        awesomeValidation.addValidation(getActivity(),R.id.edt_content,"([\\s\\S]*)", R.string.err_content);
        awesomeValidation.addValidation(getActivity(),R.id.limitedDate,"([\\s\\S]*)" , R.string.err_year);


        view.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                    countdown.setTitle(edtTitle.getText().toString());
                    countdown.setContent(edtContent.getText().toString());
                    countdown.setEnd_time(Utils.string2Date(limitedDate.getText().toString()));
                    new CountDownDao(getContext()).updateWebAndLocal(countdown);
                    closeKeyboard();
                    ((MainActivity)myActivity).backFragment();
                }
            }
        });

        view.findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CountDownDao(getContext()).deleteWebAndLocal(new CountDownDao(getContext()).get(countdown.getId()));
                ((MainActivity) getActivity()).backFragment();
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.change_countdown_fragment, container, false);

        return view;
    }

    public static ChangeCountdownFragment newInstance(CountDown countdown) {

        ChangeCountdownFragment fragment = new ChangeCountdownFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.countdown = countdown;

        Log.e("isssssd", countdown.toString());
        return  fragment;
    }
}
