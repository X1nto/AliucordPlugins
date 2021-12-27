package com.xinto.aliuplugins.layoutcontroller.patchers

import com.xinto.aliuplugins.layoutcontroller.patchers.base.BasePatcher
import com.xinto.aliuplugins.layoutcontroller.util.Description
import com.xinto.aliuplugins.layoutcontroller.util.Key
import com.xinto.aliuplugins.layoutcontroller.util.hideCompletely
import com.discord.widgets.channels.list.WidgetChannelsListAdapter
import com.discord.widgets.channels.list.items.ChannelListItem
import de.robv.android.xposed.XC_MethodHook

class ChannelsInviteButtonPatch : BasePatcher(
    key = Key.INVITE_BUTTON_CHANNELS_KEY,
    description = Description.INVITE_BUTTON_CHANNELS_DESCRIPTION,
    requiresRestart = false,
    classMember = WidgetChannelsListAdapter.ItemInvite::class.java.getDeclaredMethod(
        "onConfigure",
        Int::class.javaPrimitiveType,
        ChannelListItem::class.java
    )
) {
    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        val itemView = (callFrame.thisObject as WidgetChannelsListAdapter.ItemInvite).itemView
        itemView.hideCompletely()
        callFrame.result = callFrame.result
    }
}