package com.aliucord.plugins

import android.content.Context
import com.aliucord.Http
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.CommandContext
import com.aliucord.entities.MessageEmbedBuilder
import com.aliucord.entities.Plugin
import com.aliucord.plugins.lyrics.Data
import com.aliucord.plugins.lyrics.ResponseModel
import com.aliucord.utils.RxUtils.subscribe
import com.discord.api.commands.ApplicationCommandType
import com.discord.models.domain.spotify.ModelSpotifyTrack
import com.discord.stores.StoreStream
import com.discord.utilities.spotify.SpotifyApiClient
import rx.Subscriber
import java.util.*

@AliucordPlugin
class Lyrics : Plugin() {

    private val baseUrl = "https://lyrics-api.powercord.dev/lyrics?input="
    private val maxMessageLength = 2000

    override fun start(context: Context) {
        val songNameArg = Utils.createCommandOption(
            type = ApplicationCommandType.STRING,
            name = "name",
            description = "The song name to search lyrics for",
        )

        val shouldSendArg = Utils.createCommandOption(
            type = ApplicationCommandType.STRING,
            name = "send",
            description = "Whether to send the lyrics to chat",
        )

        commands.registerCommand(
            "lyrics",
            "Grab a song lyrics",
            listOf(songNameArg, shouldSendArg)
        ) { ctx: CommandContext ->
            val shouldSend = ctx.getBool("send") ?: false
            var songName = ctx.getString("name")

            if (songName == null) {
                val storeSpotify = StoreStream.getSpotify()
                val spotifyApiClient = storeSpotify.javaClass
                    .getDeclaredField("spotifyApiClient")
                    .let {
                        it.isAccessible = true
                        it.get(storeSpotify) as SpotifyApiClient
                    }

                //This sucks so much
                spotifyApiClient.spotifyTrack.subscribe(
                    object : Subscriber<ModelSpotifyTrack?>() {
                        override fun onCompleted() {}
                        override fun onError(th: Throwable) {
                            songName = ""
                        }

                        override fun onNext(modelSpotifyTrack: ModelSpotifyTrack?) {
                            if (modelSpotifyTrack == null) return
                            songName = "${modelSpotifyTrack.artists[0].name} ${modelSpotifyTrack.name}"
                        }
                    }
                )
            }

            //don't fetch anything until songName is not null
            @Suppress("ControlFlowWithEmptyBody")
            while (songName == null) {}

            if (songName == "") {
                return@registerCommand CommandResult(
                    "Failed to get current Spotify activity.",
                    null,
                    false
                )
            }

            val data = fetch(songName)
            return@registerCommand if (shouldSend) lyricsText(data) else lyricsEmbed(data)
        }
    }

    override fun stop(context: Context) {
        commands.unregisterAll()
    }

    private fun lyricsText(data: Data): CommandResult {
        var lyrics = data.lyrics
        if (lyrics.length > maxMessageLength) {
            val fullLyricsText = "\n\nFull Lyrics: ${data.url}"
            lyrics = lyrics.substring(0, maxMessageLength - fullLyricsText.length - 3) + "..." + fullLyricsText
        }
        return CommandResult(lyrics)
    }

    private fun lyricsEmbed(data: Data): CommandResult {
        val embed = MessageEmbedBuilder()
            .setAuthor(data.artist, null, null)
            .setTitle(data.name)
            .setDescription(data.lyrics)
            .setUrl(data.url)
            .setThumbnail(data.album_art)
            .setColor(0x209CEE)
            .setFooter(
                "Lyrics provided by KSoft.Si | © ${data.artist} ${data.album_year.split(",")[0]}",
                "https://external-content.duckduckgo.com/iu/?u=https://cdn.ksoft.si/images/Logo128.png"
            )
            .build()
        return CommandResult(null, listOf(embed), false, "Lyrics")
    }

    private fun fetch(song: String?): Data {
        val responseModel = Http.simpleJsonGet(baseUrl + song, ResponseModel::class.java) as ResponseModel
        return responseModel.data[0]
    }

}