package me.price.nicelife.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.price.nicelife.R;

/**
 * Created by jx-pc on 2016/10/2.
 */
public class CountdownFragment extends Fragment {

    View view;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.countdown_fragment, container, false);

        return view;
    }

    public static CountdownFragment newInstance() {

        CountdownFragment fragment = new CountdownFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return  fragment;
    }
}
