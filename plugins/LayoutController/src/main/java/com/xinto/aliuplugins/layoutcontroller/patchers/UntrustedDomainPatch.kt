package com.xinto.aliuplugins.layoutcontroller.patchers

import com.xinto.aliuplugins.layoutcontroller.patchers.base.BasePatcher
import com.xinto.aliuplugins.layoutcontroller.util.Description
import com.xinto.aliuplugins.layoutcontroller.util.Key
import com.discord.stores.StoreMaskedLinks
import de.robv.android.xposed.XC_MethodHook

class UntrustedDomainPatch : BasePatcher(
    key = Key.UNTRUSTED_DOMAINS_KEY,
    description = Description.UNTRUSTED_DOMAINS_DESCRIPTION,
    requiresRestart = false,
    classMember = StoreMaskedLinks::class.java.getDeclaredMethod(
        "isTrustedDomain",
        String::class.java,
        String::class.java
    )
) {
    override fun patchBody(callFrame: XC_MethodHook.MethodHookParam) {
        callFrame.result = true
    }
}