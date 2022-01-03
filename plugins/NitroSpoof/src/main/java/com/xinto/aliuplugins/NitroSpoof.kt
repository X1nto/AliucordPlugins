package com.xinto.aliuplugins

import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.Hook
import com.aliucord.patcher.InsteadHook
import com.xinto.aliuplugins.nitrospoof.EMOTE_SIZE_DEFAULT
import com.xinto.aliuplugins.nitrospoof.EMOTE_SIZE_KEY
import com.xinto.aliuplugins.nitrospoof.PluginSettings
import com.discord.models.domain.emoji.ModelEmojiCustom
import de.robv.android.xposed.XC_MethodHook
import java.lang.reflect.Field

@AliucordPlugin
class NitroSpoof : Plugin() {

    private val reflectionCache = HashMap<String, Field>()

    override fun start(context: Context) {
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("getChatInputText"),
            Hook { getChatReplacement(it) }
        )
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("getMessageContentReplacement"),
            Hook { getChatReplacement(it) }
        )
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("isUsable"),
            InsteadHook { true }
        )
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("isAvailable"),
            InsteadHook { true }
        )
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
    }
    

    private fun getChatReplacement(callFrame: XC_MethodHook.MethodHookParam) {
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