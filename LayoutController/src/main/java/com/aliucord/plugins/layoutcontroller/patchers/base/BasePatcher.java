package com.aliucord.plugins.layoutcontroller.patchers.base;

import com.aliucord.api.PatcherAPI;
import com.aliucord.api.SettingsAPI;
import com.aliucord.patcher.PinePatchFn;
import com.aliucord.plugins.layoutcontroller.util.Const;

import java.lang.reflect.Member;

import top.canyie.pine.Pine;

public abstract class BasePatcher {

    public final String key;
    public final String description;

    private final Member classMember;

    public BasePatcher(String key, String description, Member classMember) {
        this.key = key;
        this.description = description;
        this.classMember = classMember;
    }

    public void patch(PatcherAPI patcher, SettingsAPI sets) {
        patcher.patch(classMember, new PinePatchFn(callFrame -> {
            if (sets.getBool(key, Const.PREFERENCE_DEFAULT_VALUE)) {
                try {
                    patchBody(callFrame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                callFrame.setResult(callFrame.getResult());
            }
        }));
    }

    public abstract void patchBody(Pine.CallFrame callFrame) throws Exception;

}
