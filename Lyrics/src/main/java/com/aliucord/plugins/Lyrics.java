package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.Utils;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.MessageEmbed;
import com.aliucord.entities.Plugin;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.models.commands.ApplicationCommandOption;
import com.discord.models.domain.ModelMessageEmbed;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class Lyrics extends Plugin {

    private static final String baseUrl = "https://lyrics-api.powercord.dev/lyrics?input=";

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto",423915768191647755L) };
        manifest.description = "Get lyrics to a specific song";
        manifest.version = "1.0.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        ApplicationCommandOption songNameArg = new ApplicationCommandOption(ApplicationCommandType.STRING, "song name", "The song name to search lyrics for", null, true, true, null, null);
        List<ApplicationCommandOption> arguments = Collections.singletonList(songNameArg);

        commands.registerCommand("lyrics", "Grab a song lyrics", arguments, args -> {
            MessageEmbed embed = new MessageEmbed();

            try {
                ResponseModel.Data data = fetch((String) args.get("song name"));

                embed.setTitle(data.getName());
                embed.setDescription(data.getLyrics());
                embed.setUrl(data.getUrl());
                embed.setColor(0x209CEE);

                ModelMessageEmbed.Item footerItem = new ModelMessageEmbed.Item();
                Utils.setPrivateField(footerItem.getClass(), footerItem, "text", String.format("Lyrics provided by KSoft.Si | © %s %s", data.getArtist(), data.getAlbum_year().split(",")[0]));
                embed.setFooter(footerItem);
            } catch (Exception e) {
                return new CommandsAPI.CommandResult("Failed to fetch data", null, false);
            }

            return new CommandsAPI.CommandResult(null, Collections.singletonList(embed), false);
        });
    }

    @Override
    public void stop(Context context) {
        commands.unregisterAll();
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

        return responseModel.getData().get(0);
    }

    public static class ResponseModel {

        private List<Data> data;

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }

        private static class Data {

            private String lyrics;
            private String artist;
            private String album_year;
            private String name;
            private String url;

            public String getAlbum_year() {
                return album_year;
            }

            public String getArtist() {
                return artist;
            }

            public String getLyrics() {
                return lyrics;
            }

            public String getName() {
                return name;
            }

            public String getUrl() {
                return url;
            }

            public void setAlbum_year(String album_year) {
                this.album_year = album_year;
            }

            public void setArtist(String artist) {
                this.artist = artist;
            }

            public void setLyrics(String lyrics) {
                this.lyrics = lyrics;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

    }

}
