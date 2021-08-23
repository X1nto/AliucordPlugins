package com.aliucord.plugins.layoutcontroller.patchers.base

import com.aliucord.api.PatcherAPI
import com.aliucord.api.SettingsAPI
import com.aliucord.patcher.PinePatchFn
import com.aliucord.plugins.layoutcontroller.util.PREFERENCE_DEFAULT_VALUE
import top.canyie.pine.Pine.CallFrame
import java.lang.reflect.Member

abstract class BasePatcher(
    val key: String,
    val description: String,
    private val classMember: Member
) {

    fun patch(patcher: PatcherAPI, sets: SettingsAPI) {
        patcher.patch(classMember, PinePatchFn { callFrame ->
            if (sets.getBool(key, PREFERENCE_DEFAULT_VALUE)) {
                patchBody(callFrame)
            } else {
                callFrame.result = callFrame.result
            }
        })
    }

    abstract fun patchBody(callFrame: CallFrame)
}