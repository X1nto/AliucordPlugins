package com.aliucord.plugins.layoutcontroller.patchers

import android.annotation.SuppressLint
import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher
import com.aliucord.plugins.layoutcontroller.util.Description
import com.aliucord.plugins.layoutcontroller.util.Key
import com.aliucord.plugins.layoutcontroller.util.hideCompletely
import com.discord.databinding.WidgetUserSheetBinding
import com.discord.widgets.user.usersheet.WidgetUserSheet
import com.discord.widgets.user.usersheet.WidgetUserSheetViewModel
import de.robv.android.xposed.XC_MethodHook

class CallButtonsPatch : BasePatcher(
    key = Key.CALL_BUTTONS_KEY,
    description = Description.CALL_BUTTONS_DESCRIPTION,
    classMember = WidgetUserSheet::class.java.getDeclaredMethod(
        "configureProfileActionButtons",
        WidgetUserSheetViewModel.ViewState.Loaded::class.java
    )
) {
    @SuppressLint("SetTextI18n")
    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        val thisObject = callFrame.thisObject as WidgetUserSheet

        val binding = thisObject.javaClass
            .getDeclaredMethod("getBinding")
            .let {
                it.isAccessible = true
                it.invoke(thisObject) as WidgetUserSheetBinding
            }

        binding.i.hideCompletely() //Voice call button
        binding.L.hideCompletely() // Video call button
        callFrame.result = callFrame.result
    }
}