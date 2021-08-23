package com.aliucord.plugins.layoutcontroller.patchers

import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher
import com.aliucord.plugins.layoutcontroller.util.Description
import com.aliucord.plugins.layoutcontroller.util.Key
import com.aliucord.plugins.layoutcontroller.util.hideCompletely
import com.discord.widgets.channels.list.WidgetChannelsListAdapter
import com.discord.widgets.channels.list.items.ChannelListItem
import top.canyie.pine.Pine.CallFrame

class ChannelsInviteButtonPatch : BasePatcher(
    key = Key.INVITE_BUTTON_CHANNELS_KEY,
    description = Description.INVITE_BUTTON_CHANNELS_DESCRIPTION,
    classMember = WidgetChannelsListAdapter.ItemInvite::class.java.getDeclaredMethod(
        "onConfigure",
        Int::class.javaPrimitiveType,
        ChannelListItem::class.java
    )
) {
    override fun patchBody(callFrame: CallFrame) {
        val itemView = (callFrame.thisObject as WidgetChannelsListAdapter.ItemInvite).itemView
        itemView.hideCompletely()
        callFrame.result = callFrame.result
    }
}