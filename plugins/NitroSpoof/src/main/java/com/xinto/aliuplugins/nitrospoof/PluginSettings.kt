package com.xinto.aliuplugins.nitrospoof

import android.annotation.SuppressLint
import android.text.InputType
import android.view.View
import com.aliucord.Utils
import com.aliucord.api.SettingsAPI
import com aliucord.PluginManager
import com.aliucord.fragments.SettingsPage
import com.aliucord.views.Button
import com.aliucord.views.TextInput
import com.discord.views.CheckedSetting

class PluginSettings(
    private val settingsAPI: SettingsAPI
) : SettingsPage() {

    @SuppressLint("SetTextI18n")
    override fun onViewBound(view: View) {
        super.onViewBound(view)

        val context = requireContext()
        val warning = "This allows you to embed your emote without the URL being present. Some servers do not allow empty character."

        setActionBarTitle("NitroSpoof")

        val textInput = TextInput(context).apply {
            setHint("Emote Size (leave empty for discord default)")
            editText.setText(settingsAPI.getString(EMOTE_SIZE_KEY, EMOTE_SIZE_DEFAULT).toString())
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.maxLines = 1
        }

        val saveButton = Button(context).apply {
            text = "Save"
            setOnClickListener {
                settingsAPI.setString(EMOTE_SIZE_KEY, textInput.editText.text.toString())
                Utils.showToast("Successfully saved!")
                close()
            }
        }

        val emptyToggle = Utils.createCheckedSetting(context, CheckedSetting.ViewType.SWITCH, "Enable Empty Character", warning)
        emptyToggle.isChecked = settingsAPI.getBool("emptyChar", false)
        emptyToggle.setOnCheckedListener{
        c: Boolean? -> if(PluginManager.isPluginEnabled("MoreHighlight") settingsAPI.setBool("emptyChar", c!!) else Utils.showToast("Please install MoreHighlight for this to work.")
        }

        addView(emptyToggle)

        addView(textInput)
        addView(saveButton)
    }
}
