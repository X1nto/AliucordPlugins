package com.aliucord.plugins.hidebloat.patchers;

import com.aliucord.plugins.hidebloat.Const;
import com.aliucord.plugins.hidebloat.patchers.base.BasePatcher;
import com.aliucord.plugins.hidebloat.util.Util;
import com.discord.databinding.WidgetChannelMembersListItemAddBinding;

import java.lang.reflect.Field;

import kotlin.jvm.functions.Function0;
import top.canyie.pine.Pine;

public class MembersInviteButtonPatch extends BasePatcher {

    private static final String CHANNEL_MEMBERS_LIST_VIEW_HOLDER_ADD = "com.discord.widgets.channels.memberlist.adapter.ChannelMembersListViewHolderAdd";
    private static final String BIND = "bind";

    public MembersInviteButtonPatch() {
        super(Const.Key.INVITE_BUTTON_MEMBERS_KEY, Const.ViewName.INVITE_BUTTON_MEMBERS_NAME, CHANNEL_MEMBERS_LIST_VIEW_HOLDER_ADD, BIND, new Class[] { Function0.class, int.class });
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) throws Exception {
        Object _this = callFrame.thisObject;
        Field _binding = _this.getClass().getDeclaredField("binding");
        _binding.setAccessible(true);

        WidgetChannelMembersListItemAddBinding binding = (WidgetChannelMembersListItemAddBinding) _binding.get(_this);

        Util.hideViewCompletely(binding.a);

        callFrame.setResult(callFrame.getResult());
    }
}
