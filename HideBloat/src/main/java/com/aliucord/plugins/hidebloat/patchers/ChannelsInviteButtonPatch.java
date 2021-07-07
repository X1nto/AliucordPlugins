package com.aliucord.plugins.hidebloat.patchers;

import android.view.View;
import android.view.ViewGroup;

import com.aliucord.plugins.hidebloat.Const;
import com.aliucord.plugins.hidebloat.patchers.base.BasePatcher;
import com.discord.widgets.channels.list.WidgetChannelsListAdapter;
import com.discord.widgets.channels.list.items.ChannelListItem;

import top.canyie.pine.Pine;

public class ChannelsInviteButtonPatch extends BasePatcher {

    private static final String WIDGET_CHANNELS_LIST_ADAPTER_ITEM_INVITE = "com.discord.widgets.channels.list.WidgetChannelsListAdapter$ItemInvite";
    private static final String ON_CONFIGURE = "onConfigure";

    public ChannelsInviteButtonPatch() {
        super(Const.Key.CHANNELS_INVITE_BUTTON_KEY, Const.ViewName.CHANNELS_INVITE_BUTTON_NAME, WIDGET_CHANNELS_LIST_ADAPTER_ITEM_INVITE, ON_CONFIGURE, new Class[] { int.class, ChannelListItem.class });
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) {
        View itemView = ((WidgetChannelsListAdapter.ItemInvite) callFrame.thisObject).itemView;
        assert itemView != null;
        itemView.setVisibility(View.GONE);

        //Adjust LayoutParams, especially height, otherwise list
        //will include padding of the button with it's original size
        itemView.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        callFrame.setResult(callFrame.getResult());
    }
}
