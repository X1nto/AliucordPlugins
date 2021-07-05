package com.aliucord.plugins.hidebloat.views;

import android.view.View;

import com.aliucord.plugins.hidebloat.Const;
import com.aliucord.plugins.hidebloat.views.base.BasePatcher;
import com.lytefast.flexinput.fragment.FlexInputFragment;

import c.b.a.e.a;
import top.canyie.pine.Pine;

public class NitroGiftButtonPatch extends BasePatcher {

    private static final String FLEX_INPUT_FRAGMENT_D = "com.lytefast.flexinput.fragment.FlexInputFragment$d";
    private static final String INVOKE = "invoke";

    public NitroGiftButtonPatch() {
        super(Const.GIFT_BUTTON_KEY, FLEX_INPUT_FRAGMENT_D, INVOKE, new Class[] { Object.class });
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) {
        FlexInputFragment fragment = (FlexInputFragment) ((FlexInputFragment.d) callFrame.thisObject).receiver;
        a binding = fragment.j();

        if (binding == null) return;

        binding.h.setVisibility(View.GONE); // hide expand button
        binding.m.setVisibility(View.GONE); // hide gift button
        binding.l.setVisibility(View.VISIBLE); // show gallery button
    }
}
