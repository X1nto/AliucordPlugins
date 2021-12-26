package com.xinto.aliuplugins.layoutcontroller.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import com.aliucord.Utils
import com.aliucord.api.SettingsAPI
import com.xinto.aliuplugins.layoutcontroller.util.PREFERENCE_DEFAULT_VALUE
import com.aliucord.utils.DimenUtils
import com.discord.utilities.color.ColorCompat
import com.discord.views.CheckedSetting
import com.google.android.material.card.MaterialCardView
import com.lytefast.flexinput.R
import com.xinto.aliuplugins.layoutcontroller.patchers.base.BasePatcher

@SuppressLint("ViewConstructor")
class SwitchItem(
    context: Context,
    settingsAPI: SettingsAPI,
    val patch: BasePatcher
) : MaterialCardView(context) {
    init {
        radius = DimenUtils.defaultPadding.toFloat()
        setCardBackgroundColor(ColorCompat.getThemedColor(context, R.b.colorBackgroundSecondary))
        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        Utils.createCheckedSetting(context, CheckedSetting.ViewType.SWITCH, patch.description, null)
            .apply {
                isChecked = settingsAPI.getBool(patch.key, PREFERENCE_DEFAULT_VALUE)
                setOnCheckedListener {
                    settingsAPI.setBool(patch.key, it)
                    if (patch.requiresRestart)
                        Utils.promptRestart()
                }
                this@SwitchItem.addView(this)
            }
    }
}
