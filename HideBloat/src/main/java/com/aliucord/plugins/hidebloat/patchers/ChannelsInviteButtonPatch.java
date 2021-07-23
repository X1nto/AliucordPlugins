package com.aliucord.plugins.hidebloat.patchers;

import android.view.View;

import com.aliucord.plugins.hidebloat.Const;
import com.aliucord.plugins.hidebloat.patchers.base.BasePatcher;
import com.aliucord.plugins.hidebloat.util.Util;
import com.discord.widgets.channels.list.WidgetChannelsListAdapter;
import com.discord.widgets.channels.list.items.ChannelListItem;

import top.canyie.pine.Pine;

public class ChannelsInviteButtonPatch extends BasePatcher {

    private static final String WIDGET_CHANNELS_LIST_ADAPTER_ITEM_INVITE = "com.discord.widgets.channels.list.WidgetChannelsListAdapter$ItemInvite";
    private static final String ON_CONFIGURE = "onConfigure";

    public ChannelsInviteButtonPatch() {
        super(Const.Key.INVITE_BUTTON_CHANNELS_KEY, Const.ViewName.INVITE_BUTTON_CHANNELS_NAME, WIDGET_CHANNELS_LIST_ADAPTER_ITEM_INVITE, ON_CONFIGURE, new Class[] { int.class, ChannelListItem.class });
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) {
        View itemView = ((WidgetChannelsListAdapter.ItemInvite) callFrame.thisObject).itemView;
        Util.hideViewCompletely(itemView);
        callFrame.setResult(callFrame.getResult());
    }
}
