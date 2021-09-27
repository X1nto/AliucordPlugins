package com.aliucord.plugins

import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.entities.Plugin.Manifest.Author
import com.aliucord.patcher.PineInsteadFn
import com.aliucord.plugins.nitrospoof.EMOTE_SIZE_DEFAULT
import com.aliucord.plugins.nitrospoof.EMOTE_SIZE_KEY
import com.aliucord.plugins.nitrospoof.PluginSettings
import com.discord.models.domain.emoji.ModelEmojiCustom
import java.lang.reflect.Field

@AliucordPlugin
class NitroSpoof : Plugin() {

    private val reflectionCache = HashMap<String, Field>()

    override fun start(context: Context) {
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("getChatInputText"),
            PineInsteadFn { callFrame ->
                val thisObject = callFrame.thisObject
                val isUsable = thisObject.getCachedField<Boolean>("isUsable")

                if (isUsable) {
                    return@PineInsteadFn callFrame.result
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
                finalUrl
            }
        )
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("isUsable"),
            PineInsteadFn { true }
        )
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
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
            clazz.getField(name).also {
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