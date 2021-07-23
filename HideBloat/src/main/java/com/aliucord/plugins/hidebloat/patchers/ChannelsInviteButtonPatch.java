package com.aliucord.plugins.hidebloat.patchers;

import android.view.View;

import com.aliucord.plugins.hidebloat.util.Const;
import com.aliucord.plugins.hidebloat.patchers.base.BasePatcher;
import com.aliucord.plugins.hidebloat.util.Util;
import com.discord.widgets.channels.list.WidgetChannelsListAdapter;
import com.discord.widgets.channels.list.items.ChannelListItem;

import top.canyie.pine.Pine;

public class ChannelsInviteButtonPatch extends BasePatcher {

    public ChannelsInviteButtonPatch() throws Exception {
        super(Const.Key.INVITE_BUTTON_CHANNELS_KEY, Const.ViewName.INVITE_BUTTON_CHANNELS_NAME, WidgetChannelsListAdapter.ItemInvite.class.getDeclaredMethod("onConfigure", int.class, ChannelListItem.class));
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) {
        View itemView = ((WidgetChannelsListAdapter.ItemInvite) callFrame.thisObject).itemView;
        Util.hideViewCompletely(itemView);
        callFrame.setResult(callFrame.getResult());
    }
}
