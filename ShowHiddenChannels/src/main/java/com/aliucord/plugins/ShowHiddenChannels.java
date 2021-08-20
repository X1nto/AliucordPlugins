package com.aliucord.plugins;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.aliucord.Constants;
import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PinePatchFn;
import com.aliucord.wrappers.ChannelWrapper;
import com.discord.api.channel.Channel;
import com.discord.databinding.WidgetChannelsListItemChannelBinding;
import com.discord.databinding.WidgetChannelsListItemTextActionsBinding;
import com.discord.utilities.SnowflakeUtils;
import com.discord.utilities.color.ColorCompat;
import com.discord.utilities.permissions.PermissionUtils;
import com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1;
import com.discord.widgets.channels.list.WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3;
import com.discord.widgets.channels.list.WidgetChannelsListAdapter;
import com.discord.widgets.channels.list.WidgetChannelsListItemChannelActions;
import com.discord.widgets.channels.list.items.ChannelListItem;
import com.discord.widgets.channels.list.items.ChannelListItemTextChannel;
import com.lytefast.flexinput.R;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unused")
public class ShowHiddenChannels extends Plugin {

    private static final String suffix = " (hidden)";

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
        var manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto",423915768191647755L) };
        manifest.description = "Allows you to see hidden channels in servers.";
        manifest.version = "1.2.0";
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
            var _this = callFrame.thisObject;
            var ret = callFrame.getResult();
            var guildListBuilder = (WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3) _this;

            var channel = guildListBuilder.$channel;
            var channelName = ChannelWrapper.getName(channel);

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

            if (PermissionUtils.INSTANCE.hasAccess(guildListBuilder.$channel, guildListBuilder.$permissions)){
                callFrame.setResult(null);
                return;
            }

            var textLikeChannelData = WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1.invoke$default(guildListBuilder.$getTextLikeChannelData$1, guildListBuilder.$channel, guildListBuilder.$muted, null, 4, null);

            //Check if category is collapsed
            if (textLikeChannelData == null || textLikeChannelData.getHide()) {
                callFrame.setResult(null);
                return;
            }

            if (isChannelVisible(channelName)) {
                renameChannel(channel, channelName + suffix);
            }
            callFrame.setResult(new ChannelListItemTextChannel(channel, textLikeChannelData.getSelected(), textLikeChannelData.getMentionCount(), textLikeChannelData.getUnread(), true, textLikeChannelData.getLocked(), guildListBuilder.$channelsWithActiveThreads$inlined.contains(ChannelWrapper.getId(guildListBuilder.$channel))));
        }));
    }

    private void patchHiddenChannelLayout() {
        patcher.patch(channelLayoutClass, channelLayoutMethod, new Class[]{ int.class, ChannelListItem.class}, new PinePatchFn (callFrame -> {
            var ret = callFrame.thisObject;
            var _this = callFrame.thisObject;

            var textChannel = (ChannelListItemTextChannel) callFrame.args[1];
            var _itemChannelText = (WidgetChannelsListAdapter.ItemChannelText) _this;
            var channel = textChannel.getChannel();

            if (channel == null) return;

            var channelName = ChannelWrapper.getName(channel);

            if (isChannelVisible(channelName)) {
                callFrame.setResult(null);
                return;
            }

            try {
                var bindingField = _itemChannelText.getClass().getDeclaredField("binding");
                bindingField.setAccessible(true);
                var binding = (WidgetChannelsListItemChannelBinding) bindingField.get(_itemChannelText);
                var hiddenDrawable = ResourcesCompat.getDrawable(resources, resources.getIdentifier("ic_text_channel_hidden", "drawable", "com.aliucord.plugins"), null);

                if (binding == null) return;

                binding.b.setImageDrawable(hiddenDrawable);
                binding.d.setText(channelName);
                binding.a.setOnClickListener(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            callFrame.setResult(ret);
        }));
    }

    private void patchHiddenChannelActions() {
        patcher.patch(channelActionsClass, channelActionsMethod, new Class[] { WidgetChannelsListItemChannelActions.Model.class }, new PinePatchFn(callFrame -> {
            var ret = callFrame.getResult();
            var _this = callFrame.thisObject;

            var model = (WidgetChannelsListItemChannelActions.Model) callFrame.args[0];
            var channel = model.getChannel();
            var channelWrapper = new ChannelWrapper(channel);

            if (isChannelVisible(channelWrapper.getName())) {
                callFrame.setResult(null);
                return;
            }

            try {
                var getBinding = _this.getClass().getDeclaredMethod("getBinding");
                getBinding.setAccessible(true);
                var binding = (WidgetChannelsListItemTextActionsBinding) getBinding.invoke(_this);

                assert binding != null;

                var channelTopic = channelWrapper.getTopic();
                long lastSentMessageID = channelWrapper.getLastMessageId();

                var context = binding.a.getContext();

                var channelTopicView = getThemedTextView(context);
                channelTopicView.setText(String.format("Topic: %s", channelTopic != null ? channelTopic : "none"));

                var lastSendMessageView = getThemedTextView(context);
                lastSendMessageView.setText(String.format("Last sent message date: %s", SimpleDateFormat.getDateTimeInstance().format(new Date(SnowflakeUtils.toTimestamp(lastSentMessageID)))));

                var container = (LinearLayout) binding.a.getChildAt(0);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            callFrame.setResult(ret);
        }));
    }

    private void renameChannel(Channel channel, String newName) {
        try {
            var nameField = channel.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(channel, newName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextView getThemedTextView(Context context) {
        int paddingHorizontal = 36;
        int paddingVertical = 8;
        var textView = new TextView(context, null, 0, R.h.UiKit_TextView_Semibold);
        textView.setTypeface(ResourcesCompat.getFont(context, Constants.Fonts.whitney_semibold));
        textView.setTextColor(ColorCompat.getThemedColor(context, R.b.colorTextMuted));
        textView.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        return textView;
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
