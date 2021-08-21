package com.aliucord.plugins.layoutcontroller.patchers;

import android.view.View;

import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher;
import com.aliucord.plugins.layoutcontroller.util.Const;
import com.lytefast.flexinput.fragment.FlexInputFragment;
import com.lytefast.flexinput.fragment.FlexInputFragment$d;

import top.canyie.pine.Pine;

public class NitroGiftButtonPatch extends BasePatcher {

    public NitroGiftButtonPatch() throws Exception {
        super(Const.Key.GIFT_BUTTON_KEY, Const.Description.GIFT_BUTTON_DESCRIPTION, FlexInputFragment$d.class.getDeclaredMethod("invoke", Object.class));
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) {
        var fragment = (FlexInputFragment) ((FlexInputFragment$d) callFrame.thisObject).receiver;
        var binding = fragment.j();

        if (binding == null) return;

        binding.h.setVisibility(View.GONE);
        binding.m.setVisibility(View.GONE);
        binding.l.setVisibility(View.VISIBLE);
    }
}
