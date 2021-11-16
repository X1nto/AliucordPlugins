package com.xinto.aliuplugins.attachmentutils

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.aliucord.Utils
import com.aliucord.widgets.BottomSheet
import com.lytefast.flexinput.R

class AttachmentContextMenu : BottomSheet() {

    private val copyUrlIconResId = Utils.getResId("ic_copy_24dp", "drawable")

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        val context = view.context
        val attachmentUrl = arguments?.getString(ATTACHMENT_URL)!!
        val textView = TextView(context, null, 0, R.i.UiKit_Settings_Item_Icon).apply {
            val iconDrawable = ResourcesCompat
                .getDrawable(resources, copyUrlIconResId, null)
            text = "Copy URL"
            setCompoundDrawablesRelativeWithIntrinsicBounds(iconDrawable, null, null, null)
            setOnClickListener {
                Utils.setClipboard(attachmentUrl, attachmentUrl)
                Utils.showToast("Copied the URL to clipboard")
            }
        }
        linearLayout.addView(textView)
    }

    companion object {

        const val ATTACHMENT_URL = "ATTACHMENT_URL"

        fun newInstance(
            attachmentUrl: String
        ) = AttachmentContextMenu().apply {
            arguments = Bundle().apply {
                putString(ATTACHMENT_URL, attachmentUrl)
            }
        }

    }

}