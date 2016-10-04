package me.price.nicelife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.common.collect.Range;

import java.util.Calendar;

import me.price.nicelife.R;
import me.price.nicelife.datas.datastruct.Countdown;

/**
 * Created by jx-pc on 2016/10/4.
 */
public class CreateCountdownFragment extends BaseFragment {

    View view;
    Countdown countdown;

    AwesomeValidation awesomeValidation;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.create_countdown_fragment, container, false);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(getActivity(), R.id.edt_userid, "[a-zA-Z0-9_-]+", R.string.err_userid);
        awesomeValidation.addValidation(getActivity(), R.id.edt_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
        awesomeValidation.addValidation(getActivity(), R.id.edt_ip, Patterns.IP_ADDRESS, R.string.err_ip);
        awesomeValidation.addValidation(getActivity(), R.id.edt_tel, RegexTemplate.TELEPHONE, R.string.err_tel);
        awesomeValidation.addValidation(getActivity(), R.id.edt_zipcode, "\\d+", R.string.err_zipcode);
        awesomeValidation.addValidation(getActivity(), R.id.edt_year, Range.closed(1900, Calendar.getInstance().get(Calendar.YEAR)), R.string.err_year);
        awesomeValidation.addValidation(getActivity(), R.id.edt_height, Range.closed(0.0f, 2.72f), R.string.err_height);

// or
//        mAwesomeValidation.addValidation(editText, "regex", "Error info");

        view.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation == null) {
                    Log.e("TAG===", "JIONG");
                }
                else {
                    awesomeValidation.validate();
                }
            }
        });

        view.findViewById(R.id.btn_clr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awesomeValidation.clear();
            }
        });

        return view;
    }

    public static CreateCountdownFragment newInstance() {

        CreateCountdownFragment fragment = new CreateCountdownFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return  fragment;
    }
}
