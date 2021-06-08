package com.aliucord.plugins;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PinePatchFn;
import com.discord.databinding.WidgetChannelsListBinding;
import com.discord.widgets.channels.list.WidgetChannelListModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class HideSearchBox extends Plugin {

    private static final String className = "com.discord.widgets.channels.list.WidgetChannelsList";
    private static final String methodName = "configureUI";

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[]{new Manifest.Author("Xinto", 423915768191647755L)};
        manifest.description = "Hides the search box in the DM list.";
        manifest.version = "1.0.1";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        patcher.patch(className, methodName, new Class[] { WidgetChannelListModel.class }, new PinePatchFn(callFrame -> {
            try {
                Object _this = callFrame.thisObject;
                Method bindingMethod = _this.getClass().getDeclaredMethod("getBinding");
                bindingMethod.setAccessible(true);

                WidgetChannelsListBinding binding = (WidgetChannelsListBinding) bindingMethod.invoke(_this);
                assert binding != null;
                ((ViewGroup) binding.j.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            callFrame.setResult(callFrame.getResult());
        }));
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }

}