package com.aliucord.plugins.nitrospoof

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aliucord.Utils
import com.aliucord.api.SettingsAPI
import com.aliucord.widgets.LinearLayout
import com.discord.app.AppBottomSheet
import com.discord.utilities.color.ColorCompat
import com.discord.views.CheckedSetting
import com.discord.views.RadioManager
import com.lytefast.flexinput.R

class PluginSettings(private val settingsAPI: SettingsAPI) : AppBottomSheet() {

    override fun getContentViewResId() = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = inflater.context

        val layout = LinearLayout(context).apply {
            setBackgroundColor(ColorCompat.getThemedColor(context, R.b.colorBackgroundPrimary))
        }

        val radios = listOf(
            "Big (64x64)",
            "Medium (40x40)",
            "Small (32x32)",
        ).map {
            Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, it, null)
        }

        val key = "emoteSize"
        val radioManager = RadioManager(radios)
        val radioSize = radios.size
        val savedSize = settingsAPI.getInt(key, EmoteSize.FORTY.size)
        for (i in 0 until radioSize) {
            val radio = radios[i]
            radio.e {
                settingsAPI.setInt(key, EmoteSize.values()[i].size)
                radioManager.a(radio)
            }
            layout.addView(radio)
            if (radio.k.a().text.contains(savedSize.toString())) {
                radioManager.a(radio)
            }
        }
        return layout
    }
}