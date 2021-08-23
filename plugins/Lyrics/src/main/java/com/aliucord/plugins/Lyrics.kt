package com.aliucord.plugins

import android.content.Context
import com.aliucord.Http
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.CommandContext
import com.aliucord.entities.MessageEmbedBuilder
import com.aliucord.entities.Plugin
import com.aliucord.entities.Plugin.Manifest.Author
import com.aliucord.plugins.lyrics.Data
import com.aliucord.plugins.lyrics.ResponseModel
import com.aliucord.utils.RxUtils
import com.discord.api.commands.ApplicationCommandType
import com.discord.models.commands.ApplicationCommandOption
import com.discord.models.domain.spotify.ModelSpotifyTrack
import com.discord.stores.StoreStream
import com.discord.utilities.spotify.SpotifyApiClient
import rx.Subscriber
import java.util.*

class Lyrics : Plugin() {

    private val baseUrl = "https://lyrics-api.powercord.dev/lyrics?input="
    private val maxMessageLength = 2000

    override fun getManifest() =
        Manifest().apply {
            authors = arrayOf(Author("Xinto", 423915768191647755L))
            description = "Get lyrics to a specific song."
            version = "1.3.0"
            updateUrl ="https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json"
        }


    override fun start(context: Context) {
        val songNameArg = ApplicationCommandOption(
            ApplicationCommandType.STRING,
            "name",
            "The song name to search lyrics for",
            null,
            false,
            false,
            null,
            null
        )

        val shouldSendArg = ApplicationCommandOption(
            ApplicationCommandType.BOOLEAN,
            "send",
            "To send output in the chat or not",
            null,
            false,
            false,
            null,
            null
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
                RxUtils.subscribe(
                    spotifyApiClient.spotifyTrack,
                    object : Subscriber<ModelSpotifyTrack?>() {
                        override fun onCompleted() {}
                        override fun onError(th: Throwable) {
                            songName = ""
                        }

                        override fun onNext(modelSpotifyTrack: ModelSpotifyTrack?) {
                            if (modelSpotifyTrack == null) return
                            songName = "${modelSpotifyTrack.artists[0].name} ${modelSpotifyTrack.name}"
                        }
                    })
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