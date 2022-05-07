package com.xinto.aliuplugins.showhiddenchannels

import android.view.View
import com.aliucord.api.PatcherAPI
import com.aliucord.patcher.after
import com.aliucord.wrappers.ChannelWrapper.Companion.id
import com.aliucord.wrappers.ChannelWrapper.Companion.name
import com.aliucord.wrappers.ChannelWrapper.Companion.parentId
import com.discord.api.guild.GuildMaxVideoChannelUsers
import com.discord.api.permission.Permission
import com.discord.databinding.WidgetChannelsListItemChannelVoiceBinding
import com.discord.utilities.permissions.PermissionUtils
import com.discord.widgets.channels.list.WidgetChannelsListAdapter
import com.discord.widgets.channels.list.`WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$5`
import com.discord.widgets.channels.list.items.ChannelListItem
import com.discord.widgets.channels.list.items.ChannelListItemVoiceChannel
import com.xinto.aliuplugins.*

fun patchVoiceChannels(patcher: PatcherAPI) {
    patchChannelList(patcher)
    patchHiddenChannelLayout(patcher)
}

private fun patchChannelList(patcher: PatcherAPI) {
    patcher.after<`WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$5`>("invoke") {
        val ret = it.result
        val channel = `$channel`
        val channelName = channel.name

        if (ret != null) {
            if (!isChannelVisible(channelName)) {
                renameChannel(channel, channelName.removeSuffix(suffix))
            }
            return@after
        }

        if (PermissionUtils.can(Permission.VIEW_CHANNEL, `$permissions`)) {
            return@after
        }

        val connectedUsers = (`$voiceStates$inlined`[channel.id] as Collection<*>?)?.size ?: 0

        //Check if category is collapsed
        if (`$collapsedCategories$inlined`.contains(channel.parentId) && connectedUsers == 0) {
            return@after
        }
        if (isChannelVisible(channelName)) {
            renameChannel(channel, channelName + suffix)
        }
        it.result = ChannelListItemVoiceChannel(
            channel,
            false, // text channel selected
            false, // voice channel selected
            `$permissions`,
            0, // mention count
            false, // unread
            connectedUsers,
            true, // locked
            false, // nsfw
            false, // isAnyoneUsingVideo
            GuildMaxVideoChannelUsers.Unlimited.INSTANCE,
            false, // isGuildRoleSubscriptionLockedChannel
            false, // isGuildRoleSubscriptionChannel
            null // guildScheduledEvent
        )
    }
}

private fun patchHiddenChannelLayout(patcher: PatcherAPI) {
    val bindingField = WidgetChannelsListAdapter.ItemChannelVoice::class.java
        .getDeclaredField("binding").apply { isAccessible = true }

    patcher.after<WidgetChannelsListAdapter.ItemChannelVoice>(
        "onConfigure",
        Int::class.javaPrimitiveType!!,
        ChannelListItem::class.java
    ) {
        val voiceChannel = it.args[1] as ChannelListItemVoiceChannel
        val channel = voiceChannel.channel
        val channelName = channel.name

        val binding = bindingField[this] as WidgetChannelsListItemChannelVoiceBinding

        if (isChannelVisible(channelName)) {
            binding.b.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
            return@after
        }

        binding.b.apply {
            addHiddenChannelIcon(this, binding.g.imageTintList)
            visibility = View.VISIBLE
            background = null
        }
        binding.a.setOnClickListener(null)
    }
}
