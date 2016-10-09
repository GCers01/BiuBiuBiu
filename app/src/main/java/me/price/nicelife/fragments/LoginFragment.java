package me.price.nicelife.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;

import me.price.nicelife.MainActivity;
import me.price.nicelife.R;
import me.price.nicelife.bean.User;
import me.price.nicelife.db.UserDao;
import me.price.nicelife.okhttp.OkHttpUtil;
import me.price.nicelife.utils.Utils;

/**
 * Created by jx-pc on 2016/10/8.
 */
public class LoginFragment extends BaseFragment {

    View view;

    AwesomeValidation awesomeValidation;
    MainActivity myActivity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        myActivity = (MainActivity) getActivity();
        awesomeValidation.addValidation(getActivity(),R.id.edt_username,"([\\s\\S]+)", R.string.err_username);
        awesomeValidation.addValidation(getActivity(),R.id.edt_password,"([\\s\\S]+)", R.string.err_password);

        final EditText edtUsername = (EditText) view.findViewById(R.id.edt_username);
        final EditText edtPassword = (EditText) view.findViewById(R.id.edt_password);

        view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                    String username = edtUsername.getText().toString();
                    String password = edtPassword.getText().toString();
                    closeKeyboard();
                    login(username, password);
                }
            }
        });

        view.findViewById(R.id.btn_regester).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) myActivity).addNewFragment(RegisterFragment.newInstance(),"注册");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.login_fragment, container, false);

        return view;
    }

    public void login(final String userName, String passWord) {

        RequestBody formBody = new FormEncodingBuilder()
                .add("username", userName)
                .add("password", passWord)
                .build();
        final Request request = new Request.Builder()
                .url(Utils.webUrl + "login")
                .post(formBody)
                .build();
        OkHttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    final HashMap<String, String> files = Utils.toHashMap(body);
                    if (files.get("result").equals("true")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        Runnable runnable= new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog isExit = new AlertDialog.Builder(myActivity.getContext())
                                        .setTitle("系统提示")
                                        .setMessage("登录成功")
                                        .setPositiveButton("确认", null)
                                        .create();
                                isExit.show();
                                Utils.username = userName;
                                new UserDao(getContext()).add(User.newInstance(userName));
                                myActivity.updateTouxiang();
                                myActivity.backFragment();
                            }
                        };
                        handler.post(runnable);
                    } else {
                        Handler handler = new Handler(Looper.getMainLooper());
                        Runnable runnable= new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog isExit = new AlertDialog.Builder(myActivity.getContext())
                                        .setTitle("系统提示")
                                        .setMessage("登录失败")
                                        .setPositiveButton("确认", null)
                                        .create();
                                isExit.show();
                            }
                        };
                        handler.post(runnable);
                    }
                }
            }
        });
    }

    public static LoginFragment newInstance() {

        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return  fragment;
    }


}
