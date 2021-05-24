package com.aliucord.plugins;

import android.content.Context;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;
import com.discord.api.channel.Channel;
import com.discord.utilities.permissions.PermissionUtils;
import com.discord.widgets.channels.list.WidgetChannelListModel;
import com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1;
import com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3;
import com.discord.widgets.channels.list.items.ChannelListItemTextChannel;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowHiddenChannels extends Plugin {

    private static final String suffix = " (hidden)";

    public static Map<String, List<String>> getClassesToPatch() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3", Collections.singletonList("invoke"));
        map.put("com.discord.widgets.channels.list.WidgetChannelsListAdapter.ItemChannelText", Collections.singletonList("getHashIcon"));
        //map.put("com.discord.utilities.channel.GuildChannelIconUtilsKt", Collections.singletonList("guildChannelIcon"));
        return map;
    }

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto",423915768191647755L) };
        manifest.description = "Show hidden channels in servers";
        manifest.version = "0.1.1";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        patcher.patch("com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3", "invoke", (_this, args, ret) -> {
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

                if (!channelName.contains(suffix)) {
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
        patcher.patch("com.discord.widgets.channels.list.WidgetChannelsListAdapter.ItemChannelText", "getHashIcon", (_this, args, ret) -> {
            ChannelListItemTextChannel textChannel = (ChannelListItemTextChannel) args.get(0);
            Log.d("test", String.valueOf(ret));
            if (textChannel.getChannel().l().contains(suffix)) {
                @DrawableRes int drawable = resources.getIdentifier("ic_channel_hidden", "drawable", "com.aliucord.plugins");
                Log.d("test", String.valueOf(drawable));
                return drawable;
            }
            return ret;
        });
//        patcher.patch("com.discord.utilities.channel.GuildChannelIconUtilsKt", "guildChannelIcon", (_this, args, ret) -> {
//            Channel channel = (Channel) args.get(0);
//            Log.d("testRet", String.valueOf(ret));
//            Log.d("testChannelName", channel.l());
//            if (channel.l().contains(suffix)) {
//                @DrawableRes int drawable = resources.getIdentifier("ic_channel_hidden", "drawable", "com.aliucord.plugins");
//                Log.d("testDrawable", String.valueOf(drawable));
//                return drawable;
//            }
//            return ret;
//        });
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }
}
