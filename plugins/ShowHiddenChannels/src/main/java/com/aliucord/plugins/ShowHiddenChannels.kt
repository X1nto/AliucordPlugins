package com.aliucord.plugins

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.aliucord.Constants
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.PinePatchFn
import com.aliucord.wrappers.ChannelWrapper
import com.aliucord.wrappers.ChannelWrapper.Companion.id
import com.aliucord.wrappers.ChannelWrapper.Companion.name
import com.discord.api.channel.Channel
import com.discord.databinding.WidgetChannelsListItemActionsBinding
import com.discord.databinding.WidgetChannelsListItemChannelBinding
import com.discord.utilities.SnowflakeUtils
import com.discord.utilities.color.ColorCompat
import com.discord.utilities.permissions.PermissionUtils
import com.discord.widgets.channels.list.WidgetChannelsListAdapter
import com.discord.widgets.channels.list.WidgetChannelsListItemChannelActions
import com.discord.widgets.channels.list.`WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1`
import com.discord.widgets.channels.list.`WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3`
import com.discord.widgets.channels.list.items.ChannelListItem
import com.discord.widgets.channels.list.items.ChannelListItemTextChannel
import com.lytefast.flexinput.R
import top.canyie.pine.Pine.CallFrame
import java.text.SimpleDateFormat
import java.util.*

@AliucordPlugin
class ShowHiddenChannels : Plugin() {
    private val suffix = "\u200B"

    override fun start(context: Context) {
        patchChannelList()
        patchHiddenChannelLayout()
        patchHiddenChannelActions()
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
    }

    private fun patchChannelList() {
        patcher.patch(
            `WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3`::class.java
                .getDeclaredMethod("invoke"),
            PinePatchFn { callFrame: CallFrame ->
                val thisObject = callFrame.thisObject
                val ret = callFrame.result
                val guildListBuilder = thisObject as `WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3`
                val channel = guildListBuilder.`$channel`
                val channelName = channel.name

                if (ret != null) {
                    //Rename back channel if it was previously hidden
                    //in order to let user see the channel after permission
                    //was granted.
                    if (!isChannelVisible(channelName)) {
                        renameChannel(channel, channelName.removeSuffix(suffix))
                    }
                    callFrame.result = ret
                    return@PinePatchFn
                }

                if (PermissionUtils.INSTANCE.hasAccess(guildListBuilder.`$channel`, guildListBuilder.`$permissions`)) {
                    callFrame.result = null
                    return@PinePatchFn
                }

                val textLikeChannelData =
                    `WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$1`.`invoke$default`(
                        guildListBuilder.`$getTextLikeChannelData$1`,
                        guildListBuilder.`$channel`,
                        guildListBuilder.`$muted`,
                        null,
                        4,
                        null
                    )

                //Check if category is collapsed
                if (textLikeChannelData == null || textLikeChannelData.hide) {
                    callFrame.result = null
                    return@PinePatchFn
                }
                if (isChannelVisible(channelName)) {
                    renameChannel(channel, channelName + suffix)
                }
                callFrame.result = ChannelListItemTextChannel(
                    channel,
                    textLikeChannelData.selected,
                    textLikeChannelData.mentionCount,
                    textLikeChannelData.unread,
                    true,
                    textLikeChannelData.locked,
                    guildListBuilder.`$channelsWithActiveThreads$inlined`.contains(
                        guildListBuilder.`$channel`.id
                    )
                )
            }
        )
    }

    private fun patchHiddenChannelLayout() {
        patcher.patch(
            WidgetChannelsListAdapter.ItemChannelText::class.java
                .getDeclaredMethod(
                    "onConfigure",
                    Int::class.javaPrimitiveType,
                    ChannelListItem::class.java
                ),
            PinePatchFn { callFrame: CallFrame ->
                val thisObject = callFrame.thisObject as WidgetChannelsListAdapter.ItemChannelText
                val textChannel = callFrame.args[1] as ChannelListItemTextChannel
                val channel = textChannel.channel
                val channelName = channel.name

                if (isChannelVisible(channelName)) {
                    callFrame.result = null
                    return@PinePatchFn
                }

                val binding = thisObject.javaClass
                    .getDeclaredField("binding")
                    .let {
                        it.isAccessible = true
                        it.get(thisObject) as WidgetChannelsListItemChannelBinding
                    }

                val hiddenDrawable = ResourcesCompat.getDrawable(
                    resources,
                    resources.getIdentifier(
                        "ic_text_channel_hidden",
                        "drawable",
                        "com.aliucord.plugins"
                    ),
                    null
                )

                binding.b.setImageDrawable(hiddenDrawable)
                binding.a.setOnClickListener(null)
                callFrame.result = callFrame.result
            }
        )
    }

    private fun patchHiddenChannelActions() {
        patcher.patch(
            WidgetChannelsListItemChannelActions::class.java
                .getDeclaredMethod(
                    "configureUI",
                    WidgetChannelsListItemChannelActions.Model::class.java
                ),
            PinePatchFn { callFrame: CallFrame ->
                val thisObject = callFrame.thisObject
                val model = callFrame.args[0] as WidgetChannelsListItemChannelActions.Model
                val channel = model.channel
                val channelWrapper = ChannelWrapper(channel)

                if (isChannelVisible(channelWrapper.name)) {
                    callFrame.result = null
                    return@PinePatchFn
                }

                val binding = thisObject.javaClass
                    .getDeclaredMethod("getBinding")
                    .let {
                        it.isAccessible = true
                        it.invoke(thisObject) as WidgetChannelsListItemActionsBinding
                    }

                val channelTopic = channelWrapper.topic
                val lastSentMessageID = channelWrapper.lastMessageId
                val context = binding.a.context
                val channelTopicView = getThemedTextView(context)
                channelTopicView.text = String.format("Topic: %s", channelTopic ?: "none")
                val lastSendMessageView = getThemedTextView(context)
                lastSendMessageView.text = String.format(
                    "Last sent message date: %s", SimpleDateFormat.getDateTimeInstance().format(
                        Date(SnowflakeUtils.toTimestamp(lastSentMessageID))
                    )
                )
                val container = binding.a.getChildAt(0) as LinearLayout
                val childCount = container.childCount
                for (childIndex in 1 until childCount - 2) {
                    val child = container.getChildAt(childIndex)
                    child.visibility = View.GONE
                }
                container.addView(channelTopicView, 2)
                container.addView(lastSendMessageView, 3)

                callFrame.result = callFrame.result
            }
        )
    }

    private fun renameChannel(channel: Channel, newName: String) {
        val nameField = channel.javaClass.getDeclaredField("name")
        nameField.isAccessible = true
        nameField[channel] = newName
    }

    private fun getThemedTextView(context: Context): TextView {
        val paddingHorizontal = 36
        val paddingVertical = 8
        return TextView(context, null, 0, R.h.UiKit_TextView_Semibold).apply {
            typeface = ResourcesCompat.getFont(context, Constants.Fonts.whitney_semibold)
            setTextColor(ColorCompat.getThemedColor(context, R.b.colorTextMuted))
            setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
        }
    }

    private fun isChannelVisible(channelName: String): Boolean {
        return !channelName.contains(suffix)
    }

    init {
        needsResources = true
    }
}