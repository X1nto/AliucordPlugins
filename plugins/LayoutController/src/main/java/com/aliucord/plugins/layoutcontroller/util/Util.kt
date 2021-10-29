package com.aliucord.plugins.layoutcontroller.util

import android.view.View
import android.widget.LinearLayout
import com.aliucord.plugins.layoutcontroller.patchers.*

val patches = arrayOf(
    CallButtonsPatch(),
    ChannelsInviteButtonPatch(),
    DmSearchBoxPatch(),
    MembersInviteButtonPatch(),
    NitroGiftButtonPatch(),
    NotesPatch(),
    UntrustedDomainPatch(),
    WelcomeButtonPatch(),
)

fun View.hideCompletely() {
    visibility = View.GONE
    layoutParams = LinearLayout.LayoutParams(0, 0)
}