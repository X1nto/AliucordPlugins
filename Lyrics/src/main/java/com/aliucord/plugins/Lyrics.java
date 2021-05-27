package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.Utils;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.MessageEmbed;
import com.aliucord.entities.Plugin;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.models.commands.ApplicationCommandOption;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Lyrics extends Plugin {

    private static final String baseUrl = "https://lyrics-api.powercord.dev/lyrics?input=";
    private static final int MAX_MESSAGE_LENGTH = 2000;

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto",423915768191647755L) };
        manifest.description = "Get lyrics to a specific song.";
        manifest.version = "1.0.2";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        ApplicationCommandOption songNameArg = new ApplicationCommandOption(ApplicationCommandType.STRING, "name", "The song name to search lyrics for", null, true, false, null, null);
        ApplicationCommandOption shouldSendArg = new ApplicationCommandOption(ApplicationCommandType.BOOLEAN, "send", "To send output in the chat or not", null, false, false, null, null);
        List<ApplicationCommandOption> arguments = Arrays.asList(songNameArg, shouldSendArg);

        commands.registerCommand("lyrics", "Grab a song lyrics", arguments, args -> {
            Boolean shouldSend = (Boolean) args.get("send");

            if (shouldSend == null) {
                shouldSend = false;
            }

            String songName = (String) args.get("name");
            try {
                ResponseModel.Data data = fetch(songName);
                return shouldSend ? lyricsText(data) : lyricsEmbed(data);
            } catch (Exception e) {
                e.printStackTrace();
                return new CommandsAPI.CommandResult("Failed to fetch data", null, false);
            }
        });
    }

    @Override
    public void stop(Context context) {
        commands.unregisterAll();
    }

    private CommandsAPI.CommandResult lyricsText(ResponseModel.Data data) {
        String lyrics = data.lyrics;

        if (lyrics.length() > MAX_MESSAGE_LENGTH) {
            String fullLyricsText = String.format("\n\nFull Lyrics: %s", data.url);
            lyrics = lyrics.substring(0, MAX_MESSAGE_LENGTH - fullLyricsText.length() - 3) + "..." + fullLyricsText;
        }

        return new CommandsAPI.CommandResult(lyrics);
    }

    private CommandsAPI.CommandResult lyricsEmbed(ResponseModel.Data data) {
        MessageEmbed embed = new MessageEmbed()
                .setAuthor(new MessageEmbed.Author(data.artist))
                .setTitle(data.name)
                .setDescription(data.lyrics)
                .setUrl(data.url)
                .setColor(0x209CEE)
                .setFooter(String.format("Lyrics provided by KSoft.Si | © %s %s", data.artist, data.album_year.split(",")[0]), "https://external-content.duckduckgo.com/iu/?u=https://cdn.ksoft.si/images/Logo128.png");

        return new CommandsAPI.CommandResult("", Collections.singletonList(embed), false);
    }

    private ResponseModel.Data fetch(String song) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(baseUrl + song).openConnection();
        con.setRequestProperty("User-Agent", "Aliucord");

        String ln;
        StringBuilder res = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        while ((ln = reader.readLine()) != null) {
            res.append(ln);
        }
        reader.close();

        ResponseModel responseModel = Utils.fromJson(res.toString().trim(), ResponseModel.class);

        return responseModel.data.get(0);
    }

    @SuppressWarnings({"UnusedDeclaration", "MismatchedQueryAndUpdateOfCollection"})
    public static class ResponseModel {

        private List<Data> data;

        private static class Data {
            private String lyrics;
            private String artist;
            private String album_year;
            private String name;
            private String url;
        }

    }

}
