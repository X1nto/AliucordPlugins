package com.aliucord.plugins.hidebloat.util;

import android.view.View;
import android.view.ViewGroup;

public class Util {

    public static void hideViewCompletely(View view) {
        view.setVisibility(View.GONE);
        view.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
    }

}
