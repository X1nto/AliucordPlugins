package com.xinto.aliuplugins.layoutcontroller.patchers

import com.xinto.aliuplugins.layoutcontroller.patchers.base.BasePatcher
import com.xinto.aliuplugins.layoutcontroller.util.Description
import com.xinto.aliuplugins.layoutcontroller.util.Key
import com.xinto.aliuplugins.layoutcontroller.util.hideCompletely
import com.discord.databinding.WidgetChannelMembersListItemAddOrLeaveBinding
import com.discord.widgets.channels.memberlist.adapter.ChannelMembersListViewHolderAdd
import de.robv.android.xposed.XC_MethodHook

class MembersInviteButtonPatch : BasePatcher(
    key = Key.INVITE_BUTTON_MEMBERS_KEY,
    description = Description.INVITE_BUTTON_MEMBERS_DESCRIPTION,
    classMember = ChannelMembersListViewHolderAdd::class.java.getDeclaredMethod(
        "bind",
        Function0::class.java,
        Int::class.javaPrimitiveType
    )
) {

    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        val thisObject = callFrame.thisObject

        val binding = thisObject.javaClass
            .getDeclaredField("binding")
            .let {
                it.isAccessible = true
                it.get(thisObject) as WidgetChannelMembersListItemAddOrLeaveBinding
            }

        binding.a.hideCompletely()
        callFrame.result = callFrame.result
    }

}