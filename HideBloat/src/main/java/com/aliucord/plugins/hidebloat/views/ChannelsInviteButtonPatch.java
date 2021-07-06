package com.aliucord.plugins.hidebloat.views;

import android.view.View;
import android.view.ViewGroup;

import com.aliucord.plugins.hidebloat.Const;
import com.aliucord.plugins.hidebloat.views.base.BasePatcher;
import com.discord.widgets.channels.list.WidgetChannelsListAdapter;
import com.discord.widgets.channels.list.items.ChannelListItem;

import top.canyie.pine.Pine;

public class ChannelsInviteButtonPatch extends BasePatcher {

    private static final String WIDGET_CHANNELS_LIST_ADAPTER_ITEM_INVITE = "com.discord.widgets.channels.list.WidgetChannelsListAdapter$ItemInvite";
    private static final String ON_CONFIGURE = "onConfigure";

    public ChannelsInviteButtonPatch() {
        super(Const.CHANNELS_INVITE_BUTTON_KEY, WIDGET_CHANNELS_LIST_ADAPTER_ITEM_INVITE, ON_CONFIGURE, new Class[] { int.class, ChannelListItem.class });
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
