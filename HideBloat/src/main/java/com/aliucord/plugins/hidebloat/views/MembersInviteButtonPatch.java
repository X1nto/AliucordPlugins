package com.aliucord.plugins.hidebloat.views;

import android.view.View;
import android.view.ViewGroup;

import com.aliucord.plugins.hidebloat.Const;
import com.aliucord.plugins.hidebloat.views.base.BasePatcher;
import com.discord.databinding.WidgetChannelMembersListItemAddBinding;

import java.lang.reflect.Field;

import kotlin.jvm.functions.Function0;
import top.canyie.pine.Pine;

public class MembersInviteButtonPatch extends BasePatcher {

    private static final String CHANNEL_MEMBERS_LIST_VIEW_HOLDER_ADD = "com.discord.widgets.channels.memberlist.adapter.ChannelMembersListViewHolderAdd";
    private static final String BIND = "bind";

    public MembersInviteButtonPatch() {
        super(Const.MEMBERS_INVITE_BUTTON_KEY, CHANNEL_MEMBERS_LIST_VIEW_HOLDER_ADD, BIND, new Class[] { Function0.class, int.class });
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) throws Exception {
        Object _this = callFrame.thisObject;
        Field bindingField = _this.getClass().getDeclaredField("binding");
        bindingField.setAccessible(true);
        WidgetChannelMembersListItemAddBinding binding = (WidgetChannelMembersListItemAddBinding) bindingField.get(_this);
        assert binding != null;
        binding.a.setVisibility(View.GONE);

        //Adjust LayoutParams, especially height, otherwise list
        //will include padding of the button with it's original size
        binding.a.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        callFrame.setResult(callFrame.getResult());
    }
}
