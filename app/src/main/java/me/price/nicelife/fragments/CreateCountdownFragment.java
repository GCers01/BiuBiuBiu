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
import me.price.nicelife.bean.CountDown;
import me.price.nicelife.db.CountDownDao;
import me.price.nicelife.utils.Utils;

/**
 * Created by jx-pc on 2016/10/4.
 */
public class CreateCountdownFragment extends BaseFragment {

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
                    CountDown countDown = CountDown.newInstancer(title, content, Utils.string2Date(ddl), null);
                    new CountDownDao(getContext()).addWebAndLocal(countDown);
                    closeKeyboard();
                    myActivity.backFragment();
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

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.create_countdown_fragment, container, false);

        return view;
    }

    public static CreateCountdownFragment newInstance() {

        CreateCountdownFragment fragment = new CreateCountdownFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return  fragment;
    }
}
