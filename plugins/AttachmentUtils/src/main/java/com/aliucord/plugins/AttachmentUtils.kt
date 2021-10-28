package com.aliucord.plugins

import android.content.Context
import android.view.View
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.Hook
import com.aliucord.plugins.attachmentutils.AttachmentContextMenu
import com.aliucord.wrappers.messages.AttachmentWrapper
import com.discord.api.message.attachment.MessageAttachment
import com.discord.databinding.WidgetChatListAdapterItemAttachmentBinding
import com.discord.utilities.textprocessing.MessageRenderContext
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemAttachment

@AliucordPlugin
class AttachmentUtils : Plugin() {

    override fun start(context: Context?) {
        patcher.patch(
            WidgetChatListAdapterItemAttachment::class.java.getDeclaredMethod(
                "configureFileData",
                MessageAttachment::class.java,
                MessageRenderContext::class.java
            ),
            Hook { callFrame ->
                val thisObject = callFrame.thisObject as WidgetChatListAdapterItemAttachment
                val binding = thisObject::class.java
                    .getDeclaredField("binding")
                    .let {
                        it.isAccessible = true
                        it.get(thisObject) as WidgetChatListAdapterItemAttachmentBinding
                    }
                val messageAttachment = AttachmentWrapper(callFrame.args[0] as MessageAttachment)

                binding.d.setCopyUrlSheetListener(messageAttachment.url)
                binding.h.setCopyUrlSheetListener(messageAttachment.url)
            }
        )
    }

    override fun stop(context: Context?) {
        patcher.unpatchAll()
    }

    private fun View.setCopyUrlSheetListener(url: String) {
        setOnLongClickListener {
            AttachmentContextMenu
                .newInstance(url)
                .show(Utils.appActivity.supportFragmentManager, "Attachment Utils")
            return@setOnLongClickListener true
        }
    }

}