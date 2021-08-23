package com.aliucord.plugins

import android.content.Context
import com.aliucord.entities.Plugin
import com.aliucord.entities.Plugin.Manifest.Author
import com.aliucord.plugins.layoutcontroller.PluginSettings
import com.aliucord.plugins.layoutcontroller.util.patches

class LayoutController : Plugin() {

    override fun getManifest() =
        Manifest().apply {
            authors = arrayOf(Author("Xinto", 423915768191647755L))
            description = "Lets you control the discord mobile layout."
            version = "1.0.0"
            updateUrl ="https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json"
        }

    override fun start(context: Context) {
        for (bloatPatcher in patches) {
            bloatPatcher.patch(patcher, settings)
        }
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
    }

    init {
        settingsTab = SettingsTab(PluginSettings::class.java, SettingsTab.Type.PAGE)
            .withArgs(settings)
    }
}