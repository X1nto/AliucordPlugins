package com.aliucord.plugins.layoutcontroller.patchers

import android.view.View
import android.view.ViewGroup
import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher
import com.aliucord.plugins.layoutcontroller.util.Description
import com.aliucord.plugins.layoutcontroller.util.Key
import com.discord.databinding.WidgetChannelsListBinding
import com.discord.widgets.channels.list.WidgetChannelListModel
import com.discord.widgets.channels.list.WidgetChannelsList
import top.canyie.pine.Pine.CallFrame

class DmSearchBoxPatch : BasePatcher(
    key = Key.SEARCH_BOX_KEY,
    description = Description.SEARCH_BOX_DESCRIPTION,
    classMember = WidgetChannelsList::class.java.getDeclaredMethod(
        "configureUI",
        WidgetChannelListModel::class.java
    )
) {

    override fun patchBody(callFrame: CallFrame) {
        val thisObject = callFrame.thisObject
        val binding = thisObject.javaClass
            .getDeclaredMethod("getBinding")
            .let {
                it.isAccessible = true
                it.invoke(thisObject) as WidgetChannelsListBinding
            }

        (binding.j.getChildAt(0) as ViewGroup).getChildAt(2).visibility = View.GONE
        callFrame.result = callFrame.result
    }
}