package com.aliucord.plugins.layoutcontroller.patchers

import android.view.View
import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher
import com.aliucord.plugins.layoutcontroller.util.Description
import com.aliucord.plugins.layoutcontroller.util.Key
import com.lytefast.flexinput.fragment.FlexInputFragment
import com.lytefast.flexinput.fragment.`FlexInputFragment$d`
import de.robv.android.xposed.XC_MethodHook

class NitroGiftButtonPatch : BasePatcher(
    key = Key.GIFT_BUTTON_KEY,
    description = Description.GIFT_BUTTON_DESCRIPTION,
    classMember = `FlexInputFragment$d`::class.java.getDeclaredMethod(
        "invoke",
        Any::class.java
    )
) {
    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        val fragment = (callFrame.thisObject as `FlexInputFragment$d`).receiver as FlexInputFragment
        val binding = fragment.j() ?: return
        binding.h.visibility = View.GONE
        binding.m.visibility = View.GONE
        binding.l.visibility = View.VISIBLE
    }
}