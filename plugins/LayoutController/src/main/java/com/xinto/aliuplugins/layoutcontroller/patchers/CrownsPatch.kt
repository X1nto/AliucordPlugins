package com.xinto.aliuplugins.layoutcontroller.patchers

import android.view.View
import android.widget.ImageView
import com.aliucord.Utils
import com.discord.widgets.channels.memberlist.adapter.ChannelMembersListAdapter
import com.discord.widgets.channels.memberlist.adapter.ChannelMembersListViewHolderMember
import com.xinto.aliuplugins.layoutcontroller.patchers.base.BasePatcher
import com.xinto.aliuplugins.layoutcontroller.util.Description
import com.xinto.aliuplugins.layoutcontroller.util.Key
import de.robv.android.xposed.XC_MethodHook

class CrownsPatch : BasePatcher(
    key = Key.CROWNS_KEY,
    description = Description.CROWNS_DESCRIPTION,
    requiresRestart = false,
    classMember = ChannelMembersListViewHolderMember::class.java.getDeclaredMethod(
        "bind",
        ChannelMembersListAdapter.Item.Member::class.java,
        Function0::class.java
    ),
) {
    private val crownId = Utils.getResId("channel_members_list_item_group_owner_indicator", "id")

    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        val thisObj = callFrame.thisObject as ChannelMembersListViewHolderMember
        val crown = thisObj.itemView.findViewById<ImageView>(crownId)
        crown.visibility = View.GONE
    }
}