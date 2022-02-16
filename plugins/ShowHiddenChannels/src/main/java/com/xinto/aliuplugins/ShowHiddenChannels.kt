package com.xinto.aliuplugins

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.aliucord.Constants
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.discord.api.channel.Channel
import com.discord.utilities.color.ColorCompat
import com.lytefast.flexinput.R
import com.xinto.aliuplugins.showhiddenchannels.patchTextChannels
import com.xinto.aliuplugins.showhiddenchannels.patchVoiceChannels
import java.lang.reflect.Field

const val suffix = "\u200B"

fun isChannelVisible(channelName: String) = !channelName.contains(suffix)

val nameField: Field =
    Channel::class.java.getDeclaredField("name").apply { isAccessible = true }

fun renameChannel(channel: Channel, newName: String) {
    nameField[channel] = newName
}

fun getThemedTextView(context: Context): TextView {
    val paddingHorizontal = 36
    val paddingVertical = 8
    return TextView(context, null, 0, R.i.UiKit_TextView_Semibold).apply {
        typeface = ResourcesCompat.getFont(context, Constants.Fonts.whitney_semibold)
        setTextColor(ColorCompat.getThemedColor(context, R.b.colorTextMuted))
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun addHiddenChannelIcon(view: TextView, tint: ColorStateList?) {
    view.setCompoundDrawablesRelativeWithIntrinsicBounds(
        null,
        null,
        view.resources.getDrawable(R.e.avd_show_password, null)
            .mutate().apply { setTintList(tint) },
        null
    )
}

@AliucordPlugin
class ShowHiddenChannels : Plugin() {

    override fun start(context: Context) {
        patchTextChannels(patcher)
        patchVoiceChannels(patcher)
    }

    override fun stop(context: Context) = patcher.unpatchAll()
}
