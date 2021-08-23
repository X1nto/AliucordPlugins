package com.aliucord.plugins

import android.content.Context
import com.aliucord.entities.Plugin
import com.aliucord.entities.Plugin.Manifest.Author
import com.aliucord.patcher.PinePatchFn
import top.canyie.pine.Pine.CallFrame
import com.aliucord.wrappers.ChannelWrapper
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.discord.databinding.WidgetChannelsListItemTextActionsBinding
import android.widget.TextView
import com.aliucord.Constants
import com.discord.api.channel.Channel
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
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ShowHiddenChannels : Plugin() {
    private val suffix = " (hidden)"
    private val channelListClass = "com.discord.widgets.channels.list.WidgetChannelListModel\$Companion\$guildListBuilder$\$inlined\$forEach\$lambda$3"
    private val channelListMethod = "invoke"
    private val channelLayoutClass = "com.discord.widgets.channels.list.WidgetChannelsListAdapter\$ItemChannelText"
    private val channelLayoutMethod = "onConfigure"
    private val channelActionsClass = "com.discord.widgets.channels.list.WidgetChannelsListItemChannelActions"
    private val channelActionsMethod = "configureUI"

    override fun getManifest() =
        Manifest().apply {
            authors = arrayOf(Author("Xinto", 423915768191647755L))
            description = "Allows you to see hidden channels in servers."
            version = "1.2.0"
            updateUrl ="https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json"
        }

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
            channelListClass,
            channelListMethod,
            arrayOfNulls(0),
            PinePatchFn { callFrame: CallFrame ->
                val thisObject = callFrame.thisObject
                val ret = callFrame.result
                val guildListBuilder = thisObject as `WidgetChannelListModel$Companion$guildListBuilder$$inlined$forEach$lambda$3`
                val channel = guildListBuilder.`$channel`
                val channelName = ChannelWrapper.getName(channel)

                if (ret != null) {
                    //Rename back channel if it was previously hidden
                    //in order to let user see the channel after permission
                    //was granted.
                    if (!isChannelVisible(channelName)) {
                        renameChannel(channel, removeSuffix(channelName, suffix))
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
                        ChannelWrapper.getId(guildListBuilder.`$channel`)
                    )
                )
            }
        )
    }

    private fun patchHiddenChannelLayout() {
        patcher.patch(
            channelLayoutClass,
            channelLayoutMethod,
            arrayOf(Int::class.javaPrimitiveType, ChannelListItem::class.java),
            PinePatchFn { callFrame: CallFrame ->
                val thisObject = callFrame.thisObject as WidgetChannelsListAdapter.ItemChannelText
                val textChannel = callFrame.args[1] as ChannelListItemTextChannel
                val channel = textChannel.channel ?: return@PinePatchFn
                val channelName = ChannelWrapper.getName(channel)

                if (isChannelVisible(channelName)) {
                    callFrame.result = null
                    return@PinePatchFn
                }

                try {
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
                    binding.d.text = channelName
                    binding.a.setOnClickListener(null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                callFrame.result = callFrame.result
            }
        )
    }

    private fun patchHiddenChannelActions() {
        patcher.patch(
            channelActionsClass,
            channelActionsMethod,
            arrayOf<Class<*>>(WidgetChannelsListItemChannelActions.Model::class.java),
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
                        it.invoke(thisObject) as WidgetChannelsListItemTextActionsBinding
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
                binding.b.visibility = View.GONE
                binding.c.visibility = View.GONE
                binding.h.visibility = View.GONE
                binding.j.visibility = View.GONE
                binding.k.visibility = View.GONE
                binding.g.visibility = View.GONE
                binding.i.visibility = View.GONE
                binding.e.visibility = View.GONE
                container.addView(channelTopicView, childCount - 3)
                container.addView(lastSendMessageView, childCount - 4)

                callFrame.result = callFrame.result
            }
        )
    }

    private fun renameChannel(channel: Channel, newName: String) {
        try {
            val nameField = channel.javaClass.getDeclaredField("name")
            nameField.isAccessible = true
            nameField[channel] = newName
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getThemedTextView(context: Context): TextView {
        val paddingHorizontal = 36
        val paddingVertical = 8
        val textView = TextView(context, null, 0, R.h.UiKit_TextView_Semibold)
        textView.typeface = ResourcesCompat.getFont(context, Constants.Fonts.whitney_semibold)
        textView.setTextColor(ColorCompat.getThemedColor(context, R.b.colorTextMuted))
        textView.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
        return textView
    }

    private fun removeSuffix(sourceString: String, suffix: String): String {
        return if (sourceString.endsWith(suffix)) {
            sourceString.substring(0, sourceString.length - suffix.length)
        } else sourceString
    }

    private fun isChannelVisible(channelName: String): Boolean {
        return !channelName.contains(suffix)
    }

    init {
        needsResources = true
    }
}