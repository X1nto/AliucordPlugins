package com.aliucord.plugins;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PinePatchFn;
import com.discord.api.channel.Channel;
import com.discord.databinding.WidgetChannelsListItemChannelBinding;
import com.discord.databinding.WidgetChannelsListItemTextActionsBinding;
import com.discord.utilities.SnowflakeUtils;
import com.discord.utilities.permissions.PermissionUtils;
import com.discord.widgets.channels.list.WidgetChannelListModel;
import com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1;
import com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3;
import com.discord.widgets.channels.list.WidgetChannelsListAdapter;
import com.discord.widgets.channels.list.WidgetChannelsListItemChannelActions;
import com.discord.widgets.channels.list.items.ChannelListItem;
import com.discord.widgets.channels.list.items.ChannelListItemTextChannel;
import com.lytefast.flexinput.R$h;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unused")
public class ShowHiddenChannels extends Plugin {

    private static final String suffix = " ";

    private static final String channelListClass = "com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3";
    private static final String channelListMethod = "invoke";
    private static final String channelLayoutClass = "com.discord.widgets.channels.list.WidgetChannelsListAdapter$ItemChannelText";
    private static final String channelLayoutMethod = "onConfigure";
    private static final String channelActionsClass = "com.discord.widgets.channels.list.WidgetChannelsListItemChannelActions";
    private static final String channelActionsMethod = "configureUI";

    public ShowHiddenChannels() {
        needsResources = true;
    }

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto",423915768191647755L) };
        manifest.description = "Allows you to see hidden channels in servers.";
        manifest.version = "1.1.1";
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
        patcher.patch(channelListClass, channelListMethod, new Class[0], new PinePatchFn(callFrame -> {
            Object _this = callFrame.thisObject;
            Object ret = callFrame.getResult();
            WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3 lambda3 = (WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3) _this;

            Channel channel = lambda3.$channel;
            String channelName = channel.l();

            if (ret != null) {
                //Rename back channel if it was previously hidden
                //in order to let user see the channel after permission
                //was granted.
                if (!isChannelVisible(channelName)) {
                    renameChannel(channel, removeSuffix(channelName, suffix));
                }
                callFrame.setResult(ret);
                return;
            }

            if (PermissionUtils.INSTANCE.hasAccess(lambda3.$channel, lambda3.$permissions)){
                callFrame.setResult(null);
                return;
            }

            WidgetChannelListModel.Companion.TextLikeChannelData textLikeChannelData = WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1.invoke$default(lambda3.$getTextLikeChannelData$1, lambda3.$channel, lambda3.$muted, null, 4, null);

            //Check if category is collapsed
            if (textLikeChannelData == null || textLikeChannelData.getHide()) {
                callFrame.setResult(null);
                return;
            }

            if (isChannelVisible(channelName)) {
                renameChannel(channel, channelName + suffix);
            }
            callFrame.setResult(new ChannelListItemTextChannel(channel, textLikeChannelData.getSelected(), textLikeChannelData.getMentionCount(), textLikeChannelData.getUnread(), true, textLikeChannelData.getLocked(), lambda3.$channelsWithActiveThreads$inlined.contains(lambda3.$channel.g())));
        }));
    }

    private void patchHiddenChannelLayout() {
        patcher.patch(channelLayoutClass, channelLayoutMethod, new Class[]{ int.class, ChannelListItem.class}, new PinePatchFn (callFrame -> {
            Object ret = callFrame.thisObject;
            Object _this = callFrame.thisObject;

            ChannelListItemTextChannel textChannel = (ChannelListItemTextChannel) callFrame.args[1];
            WidgetChannelsListAdapter.ItemChannelText _itemChannelText = (WidgetChannelsListAdapter.ItemChannelText) _this;
            Channel channel = textChannel.getChannel();
            String channelName = channel.l();

            if (isChannelVisible(channelName)) {
                callFrame.setResult(null);
                return;
            }

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
            callFrame.setResult(ret);
        }));
    }

    private void patchHiddenChannelActions() {
        patcher.patch(channelActionsClass, channelActionsMethod, new Class[] { WidgetChannelsListItemChannelActions.Model.class }, new PinePatchFn(callFrame -> {
            Object ret = callFrame.getResult();
            Object _this = callFrame.thisObject;

            WidgetChannelsListItemChannelActions.Model model = (WidgetChannelsListItemChannelActions.Model) callFrame.args[0];
            Channel channel = model.getChannel();

            if (isChannelVisible(channel.l())) {
                callFrame.setResult(null);
                return;
            }

            try {
                Method getBinding = _this.getClass().getDeclaredMethod("getBinding");
                getBinding.setAccessible(true);
                WidgetChannelsListItemTextActionsBinding binding = (WidgetChannelsListItemTextActionsBinding) getBinding.invoke(_this);

                assert binding != null;

                String channelTopic = channel.y();
                long lastSentMessageID = (long) Channel.class.getMethod("h").invoke(channel);

                TextView channelTopicView = new TextView(binding.a.getContext(), null, 0, R$h.UiKit_Settings_Item_Header);
                channelTopicView.setText(String.format("Topic: %s", channelTopic != null ? channelTopic : "none"));

                TextView lastSendMessageView = new TextView(binding.a.getContext(), null, 0, R$h.UiKit_Settings_Item_Header);
                lastSendMessageView.setText(String.format("Last sent message date: %s", SimpleDateFormat.getDateTimeInstance().format(new Date(SnowflakeUtils.toTimestamp(lastSentMessageID)))));

                LinearLayout container = (LinearLayout) binding.a.getChildAt(0);
                int childCount = container.getChildCount();

                binding.b.setVisibility(View.GONE);
                binding.c.setVisibility(View.GONE);
                binding.h.setVisibility(View.GONE);
                binding.j.setVisibility(View.GONE);
                binding.k.setVisibility(View.GONE);
                binding.g.setVisibility(View.GONE);
                binding.i.setVisibility(View.GONE);
                binding.e.setVisibility(View.GONE);
                container.addView(channelTopicView, childCount - 3);
                container.addView(lastSendMessageView, childCount - 4);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            callFrame.setResult(ret);
        }));
    }

    private void renameChannel(Channel channel, String newName) {
        try {
            Field nameField = channel.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(channel, newName);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private String removeSuffix(String sourceString, String suffix) {
        if (sourceString.endsWith(suffix)) {
            return sourceString.substring(0, sourceString.length() - suffix.length());
        }
        return sourceString;
    }

    private boolean isChannelVisible(String channelName) {
        return !channelName.contains(suffix);
    }

}
