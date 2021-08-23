package com.aliucord.plugins.layoutcontroller.patchers

import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher
import com.aliucord.plugins.layoutcontroller.util.*
import com.discord.widgets.user.usersheet.WidgetUserSheet
import com.discord.widgets.user.usersheet.WidgetUserSheetViewModel
import top.canyie.pine.Pine.CallFrame

class NotesPatch : BasePatcher(
    key = Key.NOTES_KEY,
    description = Description.NOTES_DESCRIPTION,
    classMember = WidgetUserSheet::class.java.getDeclaredMethod(
        "configureNote",
        WidgetUserSheetViewModel.ViewState.Loaded::class.java
    )
) {
    override fun patchBody(callFrame: CallFrame) {
        val binding = WidgetUserSheet.`access$getBinding$p`(callFrame.thisObject as WidgetUserSheet)
        
        binding.w.hideCompletely()
        binding.x.hideCompletely()
        binding.y.hideCompletely()
    }
}