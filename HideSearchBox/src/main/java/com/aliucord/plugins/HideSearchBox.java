package com.aliucord.plugins;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;
import com.discord.databinding.WidgetChannelsListBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HideSearchBox extends Plugin {

    private static final String className = "com.discord.widgets.channels.list.WidgetChannelsList";
    private static final String methodName = "configureUI";

    public static Map<String, List<String>> getClassesToPatch() {
        Map<String, List<String>> map = new HashMap<>();
        map.put(className, Collections.singletonList(methodName));
        return map;
    }

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[]{new Manifest.Author("Xinto", 423915768191647755L)};
        manifest.description = "Hides the search box in the DM list.";
        manifest.version = "1.0.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        patcher.patch(className, methodName, (_this, args, ret) -> {
            try {
                Method bindingMethod = _this.getClass().getDeclaredMethod("getBinding");
                bindingMethod.setAccessible(true);

                WidgetChannelsListBinding binding = (WidgetChannelsListBinding) bindingMethod.invoke(_this);
                assert binding != null;
                ((ViewGroup) binding.j.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return ret;
        });
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }

}