package com.aliucord.plugins.layoutcontroller.util

import android.view.View
import android.view.ViewGroup
import com.aliucord.plugins.layoutcontroller.patchers.*

val patches = arrayOf(
    CallButtonsPatch(),
    ChannelsInviteButtonPatch(),
    DmSearchBoxPatch(),
    MembersInviteButtonPatch(),
    NitroGiftButtonPatch(),
    NotesPatch(),
    UntrustedDomainPatch()
)

fun View.hideCompletely() {
    visibility = View.GONE
    layoutParams = ViewGroup.LayoutParams(0, 0)
}