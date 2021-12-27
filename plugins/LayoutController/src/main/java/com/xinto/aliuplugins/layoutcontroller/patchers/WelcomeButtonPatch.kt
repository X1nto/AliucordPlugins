package com.xinto.aliuplugins.layoutcontroller.patchers

import android.widget.LinearLayout
import com.aliucord.Utils
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemSystemMessage
import com.discord.widgets.chat.list.entries.ChatListEntry
import com.xinto.aliuplugins.layoutcontroller.patchers.base.BasePatcher
import com.xinto.aliuplugins.layoutcontroller.util.Description
import com.xinto.aliuplugins.layoutcontroller.util.Key
import com.xinto.aliuplugins.layoutcontroller.util.hideCompletely
import de.robv.android.xposed.XC_MethodHook

class WelcomeButtonPatch : BasePatcher(
    key = Key.WELCOME_BUTTON_KEY,
    description = Description.WELCOME_BUTTON_DESCRIPTION,
    requiresRestart = false,
    classMember = WidgetChatListAdapterItemSystemMessage::class.java.getDeclaredMethod(
        "onConfigure", Int::class.javaPrimitiveType, ChatListEntry::class.java
    ),
) {
    private val systemWelcomeCtaButtonId = Utils.getResId("system_welcome_cta_button", "id")

    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        val thisObject = callFrame.thisObject as WidgetChatListAdapterItemSystemMessage
        val btnContainer = thisObject.itemView.findViewById<LinearLayout>(systemWelcomeCtaButtonId)
        btnContainer.hideCompletely()
    }
}