package com.xinto.aliuplugins.layoutcontroller.patchers

import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemStickerGreet
import com.discord.widgets.chat.list.entries.ChatListEntry
import com.xinto.aliuplugins.layoutcontroller.patchers.base.BasePatcher
import com.xinto.aliuplugins.layoutcontroller.util.Description
import com.xinto.aliuplugins.layoutcontroller.util.Key
import com.xinto.aliuplugins.layoutcontroller.util.hideCompletely
import de.robv.android.xposed.XC_MethodHook

class DMWavePatch : BasePatcher(
    key = Key.DM_WAVE_KEY,
    description = Description.DM_WAVE_DESCRIPTION,
    classMember = WidgetChatListAdapterItemStickerGreet::class.java.getDeclaredMethod(
        "onConfigure",
        Int::class.javaPrimitiveType,
        ChatListEntry::class.java
    ),
) {
    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        val thisObj = callFrame.thisObject as WidgetChatListAdapterItemStickerGreet
        thisObj.itemView.hideCompletely()
    }
}