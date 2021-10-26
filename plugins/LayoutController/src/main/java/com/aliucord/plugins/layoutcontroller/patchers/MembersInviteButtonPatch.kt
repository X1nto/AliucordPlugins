package com.aliucord.plugins.layoutcontroller.patchers

import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher
import com.aliucord.plugins.layoutcontroller.util.Description
import com.aliucord.plugins.layoutcontroller.util.Key
import com.aliucord.plugins.layoutcontroller.util.hideCompletely
import com.discord.databinding.WidgetChannelMembersListItemAddOrLeaveBinding
import com.discord.widgets.channels.memberlist.adapter.ChannelMembersListViewHolderAdd
import de.robv.android.xposed.XC_MethodHook
import top.canyie.pine.Pine.CallFrame

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