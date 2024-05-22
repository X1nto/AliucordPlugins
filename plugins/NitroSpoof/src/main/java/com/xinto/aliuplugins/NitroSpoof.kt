package com.xinto.aliuplugins

// import java.io.File
import android.content.Context
import com.aliucord.Constants.ALIUCORD_GUILD_ID
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI
import com.aliucord.entities.Plugin
import com.aliucord.patcher.Hook
import com.aliucord.patcher.InsteadHook
import com.aliucord.wrappers.ChannelWrapper
import com.discord.models.domain.emoji.ModelEmojiCustom
import com.discord.models.guild.Guild
import com.discord.stores.StoreStream
import com.xinto.aliuplugins.nitrospoof.EMOTE_SIZE_DEFAULT
import com.xinto.aliuplugins.nitrospoof.EMOTE_SIZE_KEY
import com.xinto.aliuplugins.nitrospoof.PluginSettings
import com.xinto.aliuplugins.nitrospoof.permBlacklist
import com.xinto.aliuplugins.nitrospoof.servBlacklist
import de.robv.android.xposed.XC_MethodHook
import java.lang.reflect.Field

@AliucordPlugin
class NitroSpoof : Plugin() {

  private val reflectionCache = HashMap<String, Field>()

  fun getGuildInfo(): Triple<Long, String, Boolean> {
    val CW = ChannelWrapper(StoreStream.getChannelsSelected().getSelectedChannel())
    val guild = if (CW.isDM()) { StoreStream.getGuilds().getGuild(769749710479032340) } else { StoreStream.getGuilds().getGuild(CW.guildId()) }
    val name = guild.getName()
    val dm = CW.isDM()
    return Triple(CW.guildId, name, dm)
  }

  override fun start(context: Context) {
    patcher.patch(
        ModelEmojiCustom::class.java.getDeclaredMethod("getChatInputText"),
        Hook { getChatReplacement(it) }
    )
    patcher.patch(
        ModelEmojiCustom::class.java.getDeclaredMethod("getMessageContentReplacement"),
        Hook { getChatReplacement(it) }
    )
    patcher.patch(ModelEmojiCustom::class.java.getDeclaredMethod("isUsable"), InsteadHook { true })
    patcher.patch(
        ModelEmojiCustom::class.java.getDeclaredMethod("isAvailable"),
        InsteadHook { true }
    )
    commands.registerCommand("blacklist", "Blacklist current server to not use empty character.") {
    val (gId, gName, dm) = getGuildInfo()
      if (servBlacklist.contains(gId)) {
        CommandsAPI.CommandResult("Current server is already in blacklist.", null, false)
      } else if (dm) {
        CommandsAPI.CommandResult("You cannot blacklist a DM", null, false)
      } else {
        servBlacklist.put(gId, gName)
        CommandsAPI.CommandResult("Current server is blacklisted.", null, false)
      }
    }
    commands.registerCommand("whitelist", "Remove current server from blacklist.") {
      if (gId == ALIUCORD_GUILD_ID) CommandsAPI.CommandResult("Nop.", null, false)
      else if (dm) {
        CommandsAPI.CommandResult("Why would you", null, false)
      } else {
        servBlacklist.remove(gId)
        CommandsAPI.CommandResult("Current server removed from blacklist.", null, false)
      }
    }
  }

  override fun stop(context: Context) {
    patcher.unpatchAll()
  }

  private fun getChatReplacement(callFrame: XC_MethodHook.MethodHookParam) {
    val thisObject = callFrame.thisObject as ModelEmojiCustom
    val isUsable = thisObject.getCachedField<Boolean>("isUsable")
    val (gId, gName, dm) = getGuildInfo()

    if (isUsable) {
      callFrame.result = callFrame.result
      return
    }

    var finalUrl = "https://cdn.discordapp.com/emojis/"

    val idStr = thisObject.getCachedField<String>("idStr")
    val isAnimated = thisObject.getCachedField<Boolean>("isAnimated")

    finalUrl += idStr
    val emoteSize = settings.getString(EMOTE_SIZE_KEY, EMOTE_SIZE_DEFAULT).toIntOrNull()

    finalUrl += (if (isAnimated) ".gif" else ".png") + "?quality=lossless"

    if (emoteSize != null) {
      finalUrl += "&size=${emoteSize}"
    }

    if (settings.getBool("emptyChar", false) &&
            !servBlacklist.contains(gId) &&
            !permBlacklist.contains(gId)
    ) {
      finalUrl = "[‎ ]" + "(" + finalUrl + ")"
    }

    callFrame.result = finalUrl
  }

  /**
   * Get a reflected field from cache or compute it if cache is absent
   * @param V type of the field value
   */
  private inline fun <reified V> Any.getCachedField(
      name: String,
      instance: Any? = this,
  ): V {
    val clazz = this::class.java
    return reflectionCache
        .computeIfAbsent(clazz.name + name) {
          clazz.getDeclaredField(name).also { it.isAccessible = true }
        }
        .get(instance) as
        V
  }

  init {
    settingsTab = SettingsTab(PluginSettings::class.java, SettingsTab.Type.PAGE).withArgs(settings)
  }

}
