package com.aliucord.plugins.layoutcontroller.patchers

import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher
import com.aliucord.plugins.layoutcontroller.util.Description
import com.aliucord.plugins.layoutcontroller.util.Key
import com.aliucord.plugins.layoutcontroller.util.hideCompletely
import com.discord.databinding.WidgetUserSheetBinding
import com.discord.widgets.user.usersheet.WidgetUserSheet
import com.discord.widgets.user.usersheet.WidgetUserSheetViewModel
import top.canyie.pine.Pine.CallFrame

class CallButtonsPatch : BasePatcher(
    key = Key.CALL_BUTTONS_KEY,
    description = Description.CALL_BUTTONS_DESCRIPTION,
    classMember = WidgetUserSheet::class.java.getDeclaredMethod(
        "configureProfileActionButtons",
        WidgetUserSheetViewModel.ViewState.Loaded::class.java
    )
) {
    override fun patchBody(callFrame: CallFrame) {
        val thisObject = callFrame.thisObject as WidgetUserSheet

        val binding = thisObject.javaClass
            .getDeclaredMethod("getBinding")
            .let {
                it.isAccessible = true
                it.invoke(thisObject) as WidgetUserSheetBinding
            }

        binding.i.hideCompletely() //Voice call button
        binding.K.hideCompletely() //Video call button
        callFrame.result = callFrame.result
    }
}