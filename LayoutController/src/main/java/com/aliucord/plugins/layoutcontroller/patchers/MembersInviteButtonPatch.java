package com.aliucord.plugins.layoutcontroller.patchers;

import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher;
import com.aliucord.plugins.layoutcontroller.util.Const;
import com.aliucord.plugins.layoutcontroller.util.Util;
import com.discord.databinding.WidgetChannelMembersListItemAddBinding;
import com.discord.widgets.channels.memberlist.adapter.ChannelMembersListViewHolderAdd;

import kotlin.jvm.functions.Function0;
import top.canyie.pine.Pine;

public class MembersInviteButtonPatch extends BasePatcher {

    public MembersInviteButtonPatch() throws Exception {
        super(Const.Key.INVITE_BUTTON_MEMBERS_KEY, Const.Description.INVITE_BUTTON_MEMBERS_DESCRIPTION, ChannelMembersListViewHolderAdd.class.getDeclaredMethod("bind", Function0.class, int.class));
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) throws Exception {
        var _this = callFrame.thisObject;
        var _binding = _this.getClass().getDeclaredField("binding");
        _binding.setAccessible(true);

        var binding = (WidgetChannelMembersListItemAddBinding) _binding.get(_this);

        if (binding == null) return;

        Util.hideViewCompletely(binding.a);

        callFrame.setResult(callFrame.getResult());
    }
}
