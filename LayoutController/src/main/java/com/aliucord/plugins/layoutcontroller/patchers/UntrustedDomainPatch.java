package com.aliucord.plugins.layoutcontroller.patchers;

import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher;
import com.aliucord.plugins.layoutcontroller.util.Const;
import com.discord.stores.StoreMaskedLinks;

import top.canyie.pine.Pine;

public class UntrustedDomainPatch extends BasePatcher {

    public UntrustedDomainPatch() throws Exception {
        super(Const.Key.UNTRUSTED_DOMAINS_KEY, Const.ViewName.UNTRUSTED_DOMAINS_NAME, StoreMaskedLinks.class.getDeclaredMethod("isTrustedDomain", String.class, String.class));
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) {
        callFrame.setResult(true);
    }
}
