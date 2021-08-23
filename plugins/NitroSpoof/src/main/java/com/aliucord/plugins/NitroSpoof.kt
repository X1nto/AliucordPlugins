package com.aliucord.plugins

import android.content.Context
import com.aliucord.plugins.nitrospoof.EmoteSize
import com.aliucord.entities.Plugin
import com.aliucord.entities.Plugin.Manifest.Author
import com.aliucord.patcher.PinePatchFn
import com.aliucord.plugins.nitrospoof.PluginSettings
import com.discord.models.domain.emoji.ModelEmojiCustom

class NitroSpoof : Plugin() {

    override fun getManifest() =
        Manifest().apply {
            authors = arrayOf(Author("Xinto", 423915768191647755L))
            description = "Use all emotes in any server without Nitro."
            version = "1.0.3"
            updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json"
        }

    override fun start(context: Context) {
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("getChatInputText"),
            PinePatchFn { callFrame ->
                val thisObject = callFrame.thisObject
                val isUsable = thisObject.javaClass
                    .getDeclaredField("isUsable")
                    .let {
                        it.isAccessible = true
                        it.getBoolean(thisObject)
                    }

                if (isUsable) {
                    callFrame.result = callFrame.result
                    return@PinePatchFn
                }

                var finalUrl = "https://cdn.discordapp.com/emojis/"

                val idStr = thisObject.javaClass
                    .getDeclaredField("idStr")
                    .let {
                        it.isAccessible = true
                        it.get(thisObject) as String
                    }

                val isAnimated = thisObject.javaClass
                    .getDeclaredField("isAnimated")
                    .let {
                        it.isAccessible = true
                        it.getBoolean(thisObject)
                    }

                finalUrl += idStr
                val emoteSize = settings.getInt("emoteSize", EmoteSize.FORTY.size)

                finalUrl += if (isAnimated) ".gif" else ".png"

                finalUrl += "?size=${emoteSize}"
                callFrame.result = finalUrl
            }
        )
        patcher.patch(
            ModelEmojiCustom::class.java.getDeclaredMethod("isUsable"),
            PinePatchFn { callFrame -> callFrame.result = true }
        )
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
    }

    init {
        settingsTab = SettingsTab(
            PluginSettings::class.java,
            SettingsTab.Type.BOTTOM_SHEET
        ).withArgs(settings)
    }
}