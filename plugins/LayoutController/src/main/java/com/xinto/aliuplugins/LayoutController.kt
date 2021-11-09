package com.xinto.aliuplugins

import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.xinto.aliuplugins.layoutcontroller.PluginSettings
import com.xinto.aliuplugins.layoutcontroller.util.patches

@AliucordPlugin
class LayoutController : Plugin() {

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