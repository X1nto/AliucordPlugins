package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;
import com.aliucord.plugins.hidebloat.PluginSettings;
import com.aliucord.plugins.hidebloat.patchers.CallButtonsPatch;
import com.aliucord.plugins.hidebloat.patchers.ChannelsInviteButtonPatch;
import com.aliucord.plugins.hidebloat.patchers.DmSearchBoxPatch;
import com.aliucord.plugins.hidebloat.patchers.MembersInviteButtonPatch;
import com.aliucord.plugins.hidebloat.patchers.NitroGiftButtonPatch;
import com.aliucord.plugins.hidebloat.patchers.base.BasePatcher;

@SuppressWarnings("unused")
public class HideBloat extends Plugin {

    private final CallButtonsPatch callButtonsPatch = new CallButtonsPatch();
    private final ChannelsInviteButtonPatch channelsInviteButtonPatch = new ChannelsInviteButtonPatch();
    private final DmSearchBoxPatch dmSearchBoxPatch = new DmSearchBoxPatch();
    private final MembersInviteButtonPatch membersInviteButtonPatch = new MembersInviteButtonPatch();
    private final NitroGiftButtonPatch nitroGiftButtonPatch = new NitroGiftButtonPatch();

    public HideBloat() {
        settingsTab = new SettingsTab(PluginSettings.class, SettingsTab.Type.PAGE)
                .withArgs(settings, new BasePatcher[] { callButtonsPatch, channelsInviteButtonPatch, dmSearchBoxPatch, membersInviteButtonPatch, nitroGiftButtonPatch } );
    }

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[]{new Manifest.Author("Xinto", 423915768191647755L)};
        manifest.description = "Hides various discord bloat.";
        manifest.version = "1.0.2";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        callButtonsPatch.patch(patcher, settings);
        channelsInviteButtonPatch.patch(patcher, settings);
        dmSearchBoxPatch.patch(patcher, settings);
        membersInviteButtonPatch.patch(patcher, settings);
        nitroGiftButtonPatch.patch(patcher, settings);
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }

}