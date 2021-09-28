package com.aliucord.plugins

import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.PineInsteadFn
import com.aliucord.patcher.PinePatchFn
import com.aliucord.plugins.nitrospoof.EMOTE_SIZE_DEFAULT
import com.aliucord.plugins.nitrospoof.EMOTE_SIZE_KEY
import com.aliucord.plugins.nitrospoof.PluginSettings
import com.discord.models.domain.emoji.Emoji
import com.discord.models.domain.emoji.ModelEmojiCustom
import com.discord.utilities.mg_recycler.MGRecyclerDataPayload
import com.discord.widgets.chat.input.emoji.EmojiPickerViewModel
import com.discord.widgets.chat.input.emoji.WidgetEmojiAdapter
import top.canyie.pine.Pine
import java.lang.reflect.Field

@AliucordPlugin
class NitroSpoof : Plugin() {

    private val reflectionCache = HashMap<String, Field>()

    override fun start(context: Context) {
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("getChatInputText"),
            PinePatchFn { getChatReplacement(it) }
        )
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("getMessageContentReplacement"),
            PinePatchFn { getChatReplacement(it) }
        )
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("isUsable"),
            PineInsteadFn { true }
        )
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("isAvailable"),
            PineInsteadFn { true }
        )

        //TL;DR: THIS IS INTENTIONAL!!!!
        //
        //There's a bug in Aliucord/Pine that causes caller code to call
        //unpatched methods unless caller code is patched too.
        //Hence we apply an "empty patch" so that NitroSpoof works again. :P
        patcher.patch(
            WidgetEmojiAdapter.EmojiViewHolder::class.java.getDeclaredMethod("onConfigure", Int::class.javaPrimitiveType, MGRecyclerDataPayload::class.java),
            PinePatchFn { callFrame ->
                callFrame.result = callFrame.result
            }
        )
        patcher.patch(
            EmojiPickerViewModel::class.java.getDeclaredMethod("onEmojiSelected", Emoji::class.java, Function1::class.java),
            PinePatchFn { callFrame ->
                callFrame.result = callFrame.result
            }
        )
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
    }

    private fun getChatReplacement(callFrame: Pine.CallFrame) {
        val thisObject = callFrame.thisObject
        val isUsable = thisObject.getCachedField<Boolean>("isUsable")

        if (isUsable) {
            callFrame.result = callFrame.result
            return
        }

        var finalUrl = "https://cdn.discordapp.com/emojis/"

        val idStr = thisObject.getCachedField<String>("idStr")
        val isAnimated = thisObject.getCachedField<Boolean>("isAnimated")

        finalUrl += idStr
        val emoteSize = settings.getString(EMOTE_SIZE_KEY, EMOTE_SIZE_DEFAULT).toIntOrNull()

        finalUrl += if (isAnimated) ".gif" else ".png"

        if (emoteSize != null) {
            finalUrl += "?size=${emoteSize}"
        }
        callFrame.result = finalUrl
    }

    /**
     * Get a reflected field from cache or compute it if cache is absent
     * @param V type of the field value
     */
    private inline fun <reified V> Any.getCachedField(
        name: String,
        instance: Any? = this,
    ): V {
        val clazz = this::class.java
        return reflectionCache.computeIfAbsent(clazz.name + name) {
            clazz.getDeclaredField(name).also {
                it.isAccessible = true
            }
        }.get(instance) as V
    }

    init {
        settingsTab = SettingsTab(
            PluginSettings::class.java,
            SettingsTab.Type.PAGE
        ).withArgs(settings)
    }
}