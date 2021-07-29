package com.aliucord.plugins.hidebloat.patchers;

import android.view.View;

import com.aliucord.plugins.hidebloat.util.Const;
import com.aliucord.plugins.hidebloat.patchers.base.BasePatcher;
import com.lytefast.flexinput.fragment.FlexInputFragment;

import c.b.a.e.a;
import top.canyie.pine.Pine;

public class NitroGiftButtonPatch extends BasePatcher {

    public NitroGiftButtonPatch() throws Exception {
        super(Const.Key.GIFT_BUTTON_KEY, Const.ViewName.GIFT_BUTTON_NAME, FlexInputFragment.d.class.getDeclaredMethod("invoke", Object.class));
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) {
        var fragment = (FlexInputFragment) ((FlexInputFragment.d) callFrame.thisObject).receiver;
        var binding = fragment.j();

        if (binding == null) return;

        binding.h.setVisibility(View.GONE);
        binding.m.setVisibility(View.GONE);
        binding.l.setVisibility(View.VISIBLE);
    }
}
