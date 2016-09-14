package com.engr195.spartansuperway.spartansuperway;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MainFragment extends Fragment {

    private static final String ARG_FIRST = "ARG_FIRST";
    private static final String ARG_LAST = "ARG_LAST";

    // TODO: Pass Firebase credentials here
    public static MainFragment newInstance(String first, String last) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FIRST, first);
        args.putString(ARG_LAST, last);
        fragment.setArguments(args);
        return fragment;
    }
}
