package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;
import com.aliucord.plugins.hidebloat.PluginSettings;
import com.aliucord.plugins.hidebloat.views.ChannelsInviteButtonPatch;
import com.aliucord.plugins.hidebloat.views.DmSearchBoxPatch;
import com.aliucord.plugins.hidebloat.views.MembersInviteButtonPatch;
import com.aliucord.plugins.hidebloat.views.NitroGiftButtonPatch;

@SuppressWarnings("unused")
public class HideBloat extends Plugin {

    public HideBloat() {
        settings = new Settings(PluginSettings.class, Settings.Type.BOTTOMSHEET);
    }

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[]{new Manifest.Author("Xinto", 423915768191647755L)};
        manifest.description = "Hides various discord bloat.";
        manifest.version = "1.0.1";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        new ChannelsInviteButtonPatch().patch(patcher, sets);
        new DmSearchBoxPatch().patch(patcher, sets);
        new MembersInviteButtonPatch().patch(patcher, sets);
        new NitroGiftButtonPatch().patch(patcher, sets);
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }

}