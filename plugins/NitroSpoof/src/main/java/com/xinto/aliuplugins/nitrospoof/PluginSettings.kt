package com.xinto.aliuplugins.nitrospoof

import android.annotation.SuppressLint
import android.text.InputType
import android.view.View
import com.aliucord.Utils
import com.aliucord.api.SettingsAPI
import com.aliucord.fragments.SettingsPage
import com.aliucord.views.Button
import com.aliucord.views.TextInput

class PluginSettings(
    private val settingsAPI: SettingsAPI
) : SettingsPage() {

    @SuppressLint("SetTextI18n")
    override fun onViewBound(view: View) {
        super.onViewBound(view)

        val context = requireContext()

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

        addView(textInput)
        addView(saveButton)
    }
}