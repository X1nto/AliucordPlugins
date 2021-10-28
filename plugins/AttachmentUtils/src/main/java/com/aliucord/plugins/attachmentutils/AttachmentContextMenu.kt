package com.aliucord.plugins.attachmentutils

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.aliucord.Utils
import com.aliucord.widgets.BottomSheet
import com.lytefast.flexinput.R

class AttachmentContextMenu : BottomSheet() {

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        val context = view.context
        val attachmentProxyUrl = arguments?.getString(ARGUMENT_KEY) ?: ""
        val textView = TextView(context, null, 0, R.h.UiKit_Settings_Item_Icon).apply {
            val copyUrlIconResId = Utils.getResId("ic_copy_24dp", "drawable")
            val iconDrawable = ResourcesCompat
                .getDrawable(resources, copyUrlIconResId, null)
                ?.apply {
                    val white = 0xFFFFFFFF
                    mutate().setTint(white.toInt())
                }
            text = "Copy URL"
            setCompoundDrawablesRelativeWithIntrinsicBounds(iconDrawable, null, null, null)
            setOnClickListener {
                Utils.setClipboard(attachmentProxyUrl, attachmentProxyUrl)
                Utils.showToast("Copied the URL to clipboard")
            }
        }
        linearLayout.addView(textView)
    }

    companion object {

        const val ARGUMENT_KEY = "attachmentProxyUrl"

        fun newInstance(
            attachmentProxyUrl: String
        ) = AttachmentContextMenu().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_KEY, attachmentProxyUrl)
            }
        }

    }

}