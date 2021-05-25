package com.aliucord.plugins;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.aliucord.entities.Plugin;
import com.discord.api.channel.Channel;
import com.discord.databinding.WidgetChannelsListItemChannelBinding;
import com.discord.databinding.WidgetChannelsListItemTextActionsBinding;
import com.discord.utilities.permissions.PermissionUtils;
import com.discord.widgets.channels.list.WidgetChannelListModel;
import com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1;
import com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3;
import com.discord.widgets.channels.list.WidgetChannelsListAdapter;
import com.discord.widgets.channels.list.WidgetChannelsListItemChannelActions;
import com.discord.widgets.channels.list.items.ChannelListItemTextChannel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowHiddenChannels extends Plugin {

    private static final String suffix = " (hidden)";

    private static final String channelListClass = "com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3";
    private static final String channelListMethod = "invoke";
    private static final String channelLayoutClass = "com.discord.widgets.channels.list.WidgetChannelsListAdapter$ItemChannelText";
    private static final String channelLayoutMethod = "onConfigure";
    private static final String channelActionsClass = "com.discord.widgets.channels.list.WidgetChannelsListItemChannelActions";
    private static final String channelActionsMethod = "configureUI";

    public static Map<String, List<String>> getClassesToPatch() {
        Map<String, List<String>> map = new HashMap<>();
        map.put(channelListClass, Collections.singletonList(channelListMethod));
        map.put(channelLayoutClass, Collections.singletonList(channelLayoutMethod));
        map.put(channelActionsClass, Collections.singletonList(channelActionsMethod));
        return map;
    }

    public ShowHiddenChannels() {
        needsResources = true;
    }

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto",423915768191647755L) };
        manifest.description = "Show hidden channels in servers";
        manifest.version = "0.2.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        patchChannelList();
        patchHiddenChannelLayout();
        patchHiddenChannelActions();
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }

    private void patchChannelList() {
        patcher.patch(channelListClass, channelListMethod, (_this, args, ret) -> {
            if (ret != null) return ret;
            WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3 lambda3 = (WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3) _this;
            if (PermissionUtils.INSTANCE.hasAccess(lambda3.$channel, lambda3.$permissions)) return null;
            WidgetChannelListModel.Companion.TextLikeChannelData textLikeChannelData = WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1.invoke$default(lambda3.$getTextLikeChannelData$1, lambda3.$channel, lambda3.$muted, null, 4, null);
            if (textLikeChannelData == null || textLikeChannelData.getHide()) return null;

            Channel channel = lambda3.$channel;
            String channelName = channel.l();

            boolean isChannelMuted = lambda3.$muted;

            if (!PermissionUtils.INSTANCE.hasAccess(lambda3.$channel, lambda3.$permissions)) {
                //Mute the channel to apply muted style
                isChannelMuted = true;

                if (!isChannelHidden(channelName)) {
                    try {
                        Field nameField = channel.getClass().getDeclaredField("name");
                        nameField.setAccessible(true);
                        nameField.set(channel, channelName + suffix);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return new ChannelListItemTextChannel(channel, textLikeChannelData.getSelected(), textLikeChannelData.getMentionCount(), textLikeChannelData.getUnread(), isChannelMuted, textLikeChannelData.getLocked(), lambda3.$channelsWithActiveThreads$inlined.contains(lambda3.$channel.g()));
        });
    }

    private void patchHiddenChannelLayout() {
        patcher.patch(channelLayoutClass, channelLayoutMethod, (_this, args, ret) -> {
            ChannelListItemTextChannel textChannel = (ChannelListItemTextChannel) args.get(1);
            WidgetChannelsListAdapter.ItemChannelText _itemChannelText = (WidgetChannelsListAdapter.ItemChannelText) _this;
            Channel channel = textChannel.getChannel();
            String channelName = channel.l();
            if (isChannelHidden(channelName)) {
                try {
                    Field bindingField = _itemChannelText.getClass().getDeclaredField("binding");
                    bindingField.setAccessible(true);
                    WidgetChannelsListItemChannelBinding binding = (WidgetChannelsListItemChannelBinding) bindingField.get(_itemChannelText);
                    Drawable hiddenDrawable = ResourcesCompat.getDrawable(resources, resources.getIdentifier("ic_channel_hidden", "drawable", "com.aliucord.plugins"), null);
                    assert binding != null;
                    binding.b.setImageDrawable(hiddenDrawable);
                    binding.d.setText(channelName);
                    binding.a.setOnClickListener(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return ret;
        });
    }

    private void patchHiddenChannelActions() {
        patcher.patch(channelActionsClass, channelActionsMethod, (_this, args, ret) -> {
            WidgetChannelsListItemChannelActions.Model model = (WidgetChannelsListItemChannelActions.Model) args.get(0);

            Channel channel = model.getChannel();
            if (isChannelHidden(channel.l())) {
                try {
                    Method getBinding = _this.getClass().getDeclaredMethod("getBinding");
                    getBinding.setAccessible(true);
                    WidgetChannelsListItemTextActionsBinding binding = (WidgetChannelsListItemTextActionsBinding) getBinding.invoke(_this);

                    assert binding != null;
                    binding.b.setVisibility(View.GONE);
                    binding.c.setVisibility(View.GONE);
                    binding.h.setVisibility(View.GONE);
                    binding.j.setVisibility(View.GONE);
                    binding.k.setVisibility(View.GONE);
                    binding.g.setVisibility(View.GONE);
                    binding.i.setVisibility(View.GONE);
                    binding.e.setVisibility(View.GONE);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return ret;
        });
    }

    private boolean isChannelHidden(String channelName) {
        return channelName.contains(suffix);
    }
}
