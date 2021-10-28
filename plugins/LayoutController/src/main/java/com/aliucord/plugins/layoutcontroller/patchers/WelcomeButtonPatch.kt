package com.aliucord.plugins.layoutcontroller.patchers

import android.view.View
import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher
import com.aliucord.plugins.layoutcontroller.util.Description
import com.aliucord.plugins.layoutcontroller.util.Key
import com.discord.databinding.WidgetChatListAdapterItemSystemBinding
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemSystemMessage
import com.discord.widgets.chat.list.entries.ChatListEntry
import de.robv.android.xposed.XC_MethodHook

class WelcomeButtonPatch : BasePatcher(
    key = Key.WELCOME_BUTTON_KEY,
    description = Description.WELCOME_BUTTON_DESCRIPTION,
    classMember = WidgetChatListAdapterItemSystemMessage::class.java.getDeclaredMethod(
        "onConfigure",
        Int::class.javaPrimitiveType,
        ChatListEntry::class.java
    ),
) {
    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        val thisObject = callFrame.thisObject as WidgetChatListAdapterItemSystemMessage
        thisObject.javaClass
            .getDeclaredField("binding")
            .let { f ->
                f.isAccessible = true
                val binding = f.get(thisObject) as WidgetChatListAdapterItemSystemBinding
                binding.h.visibility = View.GONE // LinearLayout @id/system_welcome_cta_button
            }
    }
}