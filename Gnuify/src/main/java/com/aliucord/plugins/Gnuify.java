package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;
import com.aliucord.api.CommandsAPI;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.models.commands.ApplicationCommandOption;

import java.util.*;

@SuppressWarnings("unused")
public class Gnuify extends Plugin {

    private static final String prefix = "GNU/";

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto", 423915768191647755L) };
        manifest.description = String.format("Appends \"%s\" prefix to every word in the sentence.", prefix);
        manifest.version = "1.0.1";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        ApplicationCommandOption argument = new ApplicationCommandOption(ApplicationCommandType.STRING, "sentence", "A sentence to Gnuify", null, true, true, null, null);
        commands.registerCommand(
            "gnuify",
            String.format("Append \"%s\" prefix to every word in the sentence", prefix),
            Collections.singletonList(argument),
            ctx -> {
                String sentence = ctx.getRequiredString("sentence");
                StringBuilder gnuifiedSentence = new StringBuilder();
                for (String word : sentence.split("\\s+")) {
                    gnuifiedSentence.append(prefix).append(word).append(" ");
                }
                return new CommandsAPI.CommandResult(gnuifiedSentence.toString());
            }
        );
    }

    @Override
    public void stop(Context context) {
        commands.unregisterAll();
    }

}