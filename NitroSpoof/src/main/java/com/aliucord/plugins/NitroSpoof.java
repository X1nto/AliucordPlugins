package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NitroSpoof extends Plugin {

    public static Map<String, List<String>> getClassesToPatch() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("com.discord.models.domain.emoji.ModelEmojiCustom", Arrays.asList("getChatInputText", "isUsable"));
        return map;
    }

    public NitroSpoof() {
        settings = new Settings(EmoteSizeSettings.class, Settings.Type.BOTTOMSHEET);
    }

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto",423915768191647755L) };
        manifest.description = "Use all emotes in any server without nitro";
        manifest.version = "1.0.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        patcher.patch("com.discord.models.domain.emoji.ModelEmojiCustom", "getChatInputText", (_this, args, ret) -> {
            try {
                Field isUsableField = _this.getClass().getDeclaredField("isUsable");
                isUsableField.setAccessible(true);
                boolean isUsable = isUsableField.getBoolean(_this);

                if (!isUsable) {
                    StringBuilder finalUrl = new StringBuilder();
                    finalUrl.append("https://cdn.discordapp.com/emojis/");

                    Field idStrField = _this.getClass().getDeclaredField("idStr");
                    idStrField.setAccessible(true);
                    String idStr = (String) idStrField.get(_this);

                    Field isAnimatedField = _this.getClass().getDeclaredField("isAnimated");
                    isAnimatedField.setAccessible(true);
                    boolean isAnimated = isAnimatedField.getBoolean(_this);

                    finalUrl.append(idStr);

                    EmoteSize emoteValue = EmoteSize.valueOf(sets.getString("emotesize", EmoteSize.FORTY.name()));
                    int emoteSize;

                    switch (emoteValue) {
                        case SIXTY_FOUR:
                            emoteSize = 64;
                            break;
                        case FORTY:
                            emoteSize = 40;
                            break;
                        default:
                            emoteSize = 32;
                            break;
                    }

                    if (isAnimated) {
                        finalUrl.append(".gif");
                    } else {
                        finalUrl.append(".png");
                    }
                    finalUrl.append("?size=").append(emoteSize);
                    return finalUrl.toString();
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return ret;
        });
        patcher.patch("com.discord.models.domain.emoji.ModelEmojiCustom", "isUsable", (_this, args, ret) -> true);
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }

}