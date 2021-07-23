package com.aliucord.plugins.hidebloat.util;

import android.view.View;
import android.view.ViewGroup;

import com.aliucord.plugins.hidebloat.patchers.CallButtonsPatch;
import com.aliucord.plugins.hidebloat.patchers.ChannelsInviteButtonPatch;
import com.aliucord.plugins.hidebloat.patchers.DmSearchBoxPatch;
import com.aliucord.plugins.hidebloat.patchers.MembersInviteButtonPatch;
import com.aliucord.plugins.hidebloat.patchers.NitroGiftButtonPatch;
import com.aliucord.plugins.hidebloat.patchers.base.BasePatcher;

public class Util {

    public static BasePatcher[] patches;

    static {
        try {
            patches = new BasePatcher[] {
                    new CallButtonsPatch(),
                    new ChannelsInviteButtonPatch(),
                    new DmSearchBoxPatch(),
                    new MembersInviteButtonPatch(),
                    new NitroGiftButtonPatch()
            };
        } catch (Exception ignored) {}
    }

    public static void hideViewCompletely(View view) {
        view.setVisibility(View.GONE);
        view.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
    }

}
