package com.xinto.aliuplugins.showhiddenchannels

import android.view.View
import android.widget.LinearLayout
import com.aliucord.api.PatcherAPI
import com.aliucord.patcher.after
import com.aliucord.wrappers.ChannelWrapper.Companion.id
import com.aliucord.wrappers.ChannelWrapper.Companion.lastMessageId
import com.aliucord.wrappers.ChannelWrapper.Companion.name
import com.aliucord.wrappers.ChannelWrapper.Companion.topic
import com.discord.databinding.WidgetChannelsListItemActionsBinding
import com.discord.databinding.WidgetChannelsListItemChannelBinding
import com.discord.utilities.SnowflakeUtils
import com.discord.utilities.permissions.PermissionUtils
import com.discord.widgets.channels.list.WidgetChannelsListAdapter
import com.discord.widgets.channels.list.WidgetChannelsListItemChannelActions
import com.discord.widgets.channels.list.`WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1`
import com.discord.widgets.channels.list.`WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3`
import com.discord.widgets.channels.list.items.ChannelListItem
import com.discord.widgets.channels.list.items.ChannelListItemTextChannel
import com.xinto.aliuplugins.*
import java.text.SimpleDateFormat
import java.util.*

fun patchTextChannels(patcher: PatcherAPI) {
    patchChannelList(patcher)
    patchHiddenChannelLayout(patcher)
    patchHiddenChannelActions(patcher)
}

private fun patchChannelList(patcher: PatcherAPI) {
    patcher.after<`WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3`>("invoke") {
        val ret = it.result
        val channel = `$channel`
        val channelName = channel.name

        if (ret != null) {
            if (!isChannelVisible(channelName)) {
                renameChannel(channel, channelName.removeSuffix(suffix))
            }
            return@after
        }

        if (PermissionUtils.INSTANCE.hasAccess(channel, `$permissions`)) {
            return@after
        }

        val textLikeChannelData =
            `WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1`.`invoke$default`(
                `$getTextLikeChannelData$1`,
                channel,
                `$muted`,
                null,
                4,
                null
            )

        //Check if category is collapsed
        if (textLikeChannelData == null || textLikeChannelData.hide) {
            return@after
        }
        if (isChannelVisible(channelName)) {
            renameChannel(channel, channelName + suffix)
        }
        it.result = ChannelListItemTextChannel(
            channel,
            textLikeChannelData.selected,
            textLikeChannelData.mentionCount,
            textLikeChannelData.unread,
            true,
            textLikeChannelData.locked,
            `$channelsWithActiveThreads$inlined`.contains(
                channel.id
            ),
            false,
            false
        )
    }
}

private fun patchHiddenChannelLayout(patcher: PatcherAPI) {
    val bindingField = WidgetChannelsListAdapter.ItemChannelText::class.java
        .getDeclaredField("binding").apply { isAccessible = true }

    patcher.after<WidgetChannelsListAdapter.ItemChannelText>(
        "onConfigure",
        Int::class.javaPrimitiveType!!,
        ChannelListItem::class.java
    ) {
        val textChannel = it.args[1] as ChannelListItemTextChannel
        val channel = textChannel.channel
        val channelName = channel.name

        val binding = bindingField[this] as WidgetChannelsListItemChannelBinding

        if (isChannelVisible(channelName)) {
            binding.e.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
            return@after
        }

        addHiddenChannelIcon(binding.e, binding.c.imageTintList)
        binding.a.setOnClickListener(null)
    }
}

private fun patchHiddenChannelActions(patcher: PatcherAPI) {
    val getBinding = WidgetChannelsListItemChannelActions::class.java
        .getDeclaredMethod("getBinding").apply { isAccessible = true }

    patcher.after<WidgetChannelsListItemChannelActions>(
        "configureUI",
        WidgetChannelsListItemChannelActions.Model::class.java
    ) {
        val model = it.args[0] as WidgetChannelsListItemChannelActions.Model
        val channel = model.channel

        if (isChannelVisible(channel.name)) {
            return@after
        }

        val binding = getBinding.invoke(this) as WidgetChannelsListItemActionsBinding

        val context = binding.a.context
        val channelTopicView = getThemedTextView(context).apply {
            text = String.format("Topic: %s", channel.topic ?: "none")
        }
        val lastSendMessageView = getThemedTextView(context).apply {
            text = String.format(
                "Last sent message date: %s", SimpleDateFormat.getDateTimeInstance().format(
                    Date(SnowflakeUtils.toTimestamp(channel.lastMessageId))
                )
            )
        }

        val container = binding.a.getChildAt(0) as LinearLayout
        val childCount = container.childCount
        for (childIndex in 1 until childCount - 2) {
            val child = container.getChildAt(childIndex)
            child.visibility = View.GONE
        }
        container.addView(channelTopicView, 2)
        container.addView(lastSendMessageView, 3)
    }
}
