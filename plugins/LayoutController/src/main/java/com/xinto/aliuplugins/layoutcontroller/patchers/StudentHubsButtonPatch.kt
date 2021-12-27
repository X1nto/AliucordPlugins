package com.xinto.aliuplugins.layoutcontroller.patchers

import com.discord.widgets.guilds.list.GuildListItem
import com.discord.widgets.guilds.list.WidgetGuildsListViewModel
import com.xinto.aliuplugins.layoutcontroller.patchers.base.BasePatcher
import com.xinto.aliuplugins.layoutcontroller.util.Description
import com.xinto.aliuplugins.layoutcontroller.util.Key
import de.robv.android.xposed.XC_MethodHook

class StudentHubsButtonPatch : BasePatcher(
    key = Key.STUDENT_HUBS_BUTTON,
    description = Description.STUDENT_HUBS_BUTTON_DESCRIPTION,
    classMember = WidgetGuildsListViewModel.ViewState.Loaded::class.java.getDeclaredMethod("getItems")
) {
    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        val items = (callFrame.result as ArrayList<*>)
        if (items[items.size - 2] is GuildListItem.HubItem) {
            items.removeAt(items.size - 2)
            callFrame.result = items
        }
    }
}
