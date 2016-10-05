package me.price.nicelife.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by jx-pc on 2016/9/23.
 */
public class BaseFragment extends Fragment {

    protected void closeKeyboard() {
        View view = getView();
        InputMethodManager inputMethodManager =
                (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
