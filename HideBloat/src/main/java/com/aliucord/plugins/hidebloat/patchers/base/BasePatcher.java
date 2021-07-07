package com.aliucord.plugins.hidebloat.patchers.base;

import com.aliucord.api.PatcherAPI;
import com.aliucord.api.SettingsAPI;
import com.aliucord.patcher.PinePatchFn;

import top.canyie.pine.Pine;

public abstract class BasePatcher {

    public final String key;
    public final String viewName;

    private final String className;
    private final String methodName;
    private final Class<?>[] args;

    public BasePatcher(String key, String viewName, String className, String methodName, Class<?>[] args) {
        this.key = key;
        this.viewName = viewName;
        this.className = className;
        this.methodName = methodName;
        this.args = args;
    }

    public void patch(PatcherAPI patcher, SettingsAPI sets) {
        patcher.patch(className, methodName, args, new PinePatchFn(callFrame -> {
            if (sets.getBool(key, true)) {
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
