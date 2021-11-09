package com.xinto.aliuplugins.layoutcontroller.patchers

import com.xinto.aliuplugins.layoutcontroller.patchers.base.BasePatcher
import com.xinto.aliuplugins.layoutcontroller.util.Description
import com.xinto.aliuplugins.layoutcontroller.util.Key
import com.xinto.aliuplugins.layoutcontroller.util.hideCompletely
import com.discord.widgets.user.usersheet.WidgetUserSheet
import com.discord.widgets.user.usersheet.WidgetUserSheetViewModel
import de.robv.android.xposed.XC_MethodHook

class NotesPatch : BasePatcher(
    key = Key.NOTES_KEY,
    description = Description.NOTES_DESCRIPTION,
    classMember = WidgetUserSheet::class.java.getDeclaredMethod(
        "configureNote",
        WidgetUserSheetViewModel.ViewState.Loaded::class.java
    )
) {
    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        val binding = WidgetUserSheet.`access$getBinding$p`(callFrame.thisObject as WidgetUserSheet)
        
        binding.w.hideCompletely()
        binding.x.hideCompletely()
        binding.y.hideCompletely()
    }
}