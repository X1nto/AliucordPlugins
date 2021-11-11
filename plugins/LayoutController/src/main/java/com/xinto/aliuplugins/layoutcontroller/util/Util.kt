package com.xinto.aliuplugins.layoutcontroller.util

import android.view.View
import android.widget.LinearLayout
import com.xinto.aliuplugins.layoutcontroller.patchers.*

val patches = arrayOf(
    CallButtonsPatch(),
    ChannelsInviteButtonPatch(),
    DmSearchBoxPatch(),
    MembersInviteButtonPatch(),
    NitroGiftButtonPatch(),
    NotesPatch(),
    UntrustedDomainPatch(),
    WelcomeButtonPatch(),
    DMWavePatch(),
)

fun View.hideCompletely() {
    visibility = View.GONE
    layoutParams = LinearLayout.LayoutParams(0, 0)
}