package com.aliucord.plugins;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PinePatchFn;
import com.discord.databinding.WidgetChannelMembersListItemAddBinding;
import com.discord.widgets.channels.list.WidgetChannelsListAdapter;
import com.discord.widgets.channels.list.items.ChannelListItem;

import java.lang.reflect.Field;

import kotlin.jvm.functions.Function0;

@SuppressWarnings("unused")
public class HideInviteButton extends Plugin {

    private static final String widgetChannelsListAdapter = "com.discord.widgets.channels.list.WidgetChannelsListAdapter$ItemInvite";
    private static final String onConfigure = "onConfigure";

    private static final String channelMembersListAdapter = "com.discord.widgets.channels.memberlist.adapter.ChannelMembersListViewHolderAdd";
    private static final String onCreateViewHolder = "bind";

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[]{new Manifest.Author("Xinto", 423915768191647755L)};
        manifest.description = "Hides the invite button in the channel and member lists.";
        manifest.version = "1.1.1";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        patcher.patch(widgetChannelsListAdapter, onConfigure, new Class[] { int.class, ChannelListItem.class }, new PinePatchFn(callFrame -> {
            View itemView = ((WidgetChannelsListAdapter.ItemInvite) callFrame.thisObject).itemView;
            assert itemView != null;
            itemView.setVisibility(View.GONE);

            //Adjust LayoutParams, especially height, otherwise list
            //will include padding of the button with it's original size
            itemView.setLayoutParams(new ViewGroup.LayoutParams(0, 10));
            callFrame.setResult(callFrame.getResult());
        }));
        patcher.patch(channelMembersListAdapter, onCreateViewHolder, new Class[] { Function0.class, int.class }, new PinePatchFn(callFrame -> {
            try {
                Object _this = callFrame.thisObject;
                Field bindingField = _this.getClass().getDeclaredField("binding");
                bindingField.setAccessible(true);
                WidgetChannelMembersListItemAddBinding binding = (WidgetChannelMembersListItemAddBinding) bindingField.get(_this);
                assert binding != null;
                binding.a.setVisibility(View.GONE);

                //Adjust LayoutParams, especially height, otherwise list
                //will include padding of the button with it's original size
                binding.a.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
            } catch (IllegalAccessException | NoSuchFieldException e) {
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