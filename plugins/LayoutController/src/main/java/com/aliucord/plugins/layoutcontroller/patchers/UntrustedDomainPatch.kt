package com.aliucord.plugins.layoutcontroller.patchers

import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher
import com.aliucord.plugins.layoutcontroller.util.Description
import com.aliucord.plugins.layoutcontroller.util.Key
import com.discord.stores.StoreMaskedLinks
import de.robv.android.xposed.XC_MethodHook

class UntrustedDomainPatch : BasePatcher(
    key = Key.UNTRUSTED_DOMAINS_KEY,
    description = Description.UNTRUSTED_DOMAINS_DESCRIPTION,
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