package com.aliucord.plugins.layoutcontroller.patchers

import android.widget.Button
import com.aliucord.Utils
import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher
import com.aliucord.plugins.layoutcontroller.util.Description
import com.aliucord.plugins.layoutcontroller.util.Key
import com.aliucord.plugins.layoutcontroller.util.hideCompletely
import com.discord.databinding.WidgetUserSheetBinding
import com.discord.widgets.user.usersheet.WidgetUserSheet
import com.discord.widgets.user.usersheet.WidgetUserSheetViewModel
import de.robv.android.xposed.XC_MethodHook

class CallButtonsPatch : BasePatcher(
    key = Key.CALL_BUTTONS_KEY, description = Description.CALL_BUTTONS_DESCRIPTION, classMember = WidgetUserSheet::class.java.getDeclaredMethod(
        "configureProfileActionButtons", WidgetUserSheetViewModel.ViewState.Loaded::class.java
    )
) {
    private val callButtonId = Utils.getResId("user_sheet_call_action_button", "id")
    private val videoButtonId = Utils.getResId("user_sheet_video_action_button", "id")

    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        val thisObject = callFrame.thisObject as WidgetUserSheet

        val root = thisObject.javaClass.getDeclaredMethod("getBinding").let {
                it.isAccessible = true
                it.invoke(thisObject) as WidgetUserSheetBinding
            }.root

        root.findViewById<Button>(callButtonId).hideCompletely()
        root.findViewById<Button>(videoButtonId).hideCompletely()
        callFrame.result = callFrame.result
    }
}