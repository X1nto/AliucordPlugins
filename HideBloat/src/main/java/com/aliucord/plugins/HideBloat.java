package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;
import com.aliucord.plugins.hidebloat.PluginSettings;
import com.aliucord.plugins.hidebloat.patchers.base.BasePatcher;
import com.aliucord.plugins.hidebloat.util.Util;

@SuppressWarnings("unused")
public class HideBloat extends Plugin {

    public HideBloat() {
        settingsTab = new SettingsTab(PluginSettings.class, SettingsTab.Type.PAGE)
                .withArgs(settings);
    }

    @NonNull
    @Override
    public Manifest getManifest() {
        var manifest = new Manifest();
        manifest.authors = new Manifest.Author[]{new Manifest.Author("Xinto", 423915768191647755L)};
        manifest.description = "Hides various discord bloat.";
        manifest.version = "1.1.3";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        for (BasePatcher bloatPatcher : Util.patches) {
            bloatPatcher.patch(patcher, settings);
        }
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }

}
