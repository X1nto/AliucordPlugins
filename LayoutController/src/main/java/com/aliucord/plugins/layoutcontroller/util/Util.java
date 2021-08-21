package com.aliucord.plugins.layoutcontroller.util;

import android.view.View;
import android.view.ViewGroup;

import com.aliucord.plugins.layoutcontroller.patchers.CallButtonsPatch;
import com.aliucord.plugins.layoutcontroller.patchers.ChannelsInviteButtonPatch;
import com.aliucord.plugins.layoutcontroller.patchers.DmSearchBoxPatch;
import com.aliucord.plugins.layoutcontroller.patchers.MembersInviteButtonPatch;
import com.aliucord.plugins.layoutcontroller.patchers.NitroGiftButtonPatch;
import com.aliucord.plugins.layoutcontroller.patchers.NotesPatch;
import com.aliucord.plugins.layoutcontroller.patchers.UntrustedDomainPatch;
import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher;

public class Util {

    public static BasePatcher[] patches;

    static {
        try {
            patches = new BasePatcher[] {
                    new CallButtonsPatch(),
                    new ChannelsInviteButtonPatch(),
                    new DmSearchBoxPatch(),
                    new MembersInviteButtonPatch(),
                    new NitroGiftButtonPatch(),
                    new NotesPatch(),
                    new UntrustedDomainPatch()
            };
        } catch (Exception ignored) {}
    }

    public static void hideViewCompletely(View view) {
        view.setVisibility(View.GONE);
        view.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
    }

}
