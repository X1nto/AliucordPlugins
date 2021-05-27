package com.aliucord.plugins;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;
import com.discord.widgets.channels.list.WidgetChannelsListAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HideInviteButton extends Plugin {

    public static Map<String, List<String>> getClassesToPatch() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("com.discord.widgets.channels.list.WidgetChannelsListAdapter$ItemInvite", Collections.singletonList("onConfigure"));
        return map;
    }

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[]{new Manifest.Author("Xinto", 423915768191647755L)};
        manifest.description = "Hides invite button in channels list";
        manifest.version = "1.0.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        patcher.patch("com.discord.widgets.channels.list.WidgetChannelsListAdapter$ItemInvite", "onConfigure", (_this, args, ret) -> {
            View itemView = (View) ((WidgetChannelsListAdapter.ItemInvite) _this).itemView;
            assert itemView != null;
            itemView.setVisibility(View.GONE);

            //Adjust LayoutParams, especially height, otherwise list
            //will include padding of the button with it's original size
            itemView.setLayoutParams(new ViewGroup.LayoutParams(0, 10));
            return ret;
        });
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }

}