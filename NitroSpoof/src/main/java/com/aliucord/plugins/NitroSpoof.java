package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PinePatchFn;
import com.aliucord.plugins.nitrospoof.EmoteSize;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class NitroSpoof extends Plugin {

    private static final String modelEmojiCustom = "com.discord.models.domain.emoji.ModelEmojiCustom";
    private static final String getChatInputText = "getChatInputText";
    private static final String isUsable = "isUsable";

    public NitroSpoof() {
        settings = new Settings(com.aliucord.plugins.nitrospoof.Settings.class, Plugin.Settings.Type.BOTTOMSHEET);
    }

    @NonNull
    @Override
    public Manifest getManifest() {
        Manifest manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Xinto",423915768191647755L) };
        manifest.description = "Use all emotes in any server without Nitro.";
        manifest.version = "1.0.2";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        patcher.patch(modelEmojiCustom, getChatInputText, new Class[0], new PinePatchFn(callFrame -> {
            try {
                Object _this = callFrame.thisObject;

                Field isUsableField = _this.getClass().getDeclaredField("isUsable");
                isUsableField.setAccessible(true);
                boolean isUsable = isUsableField.getBoolean(_this);

                if (isUsable) {
                    callFrame.setResult(callFrame.getResult());
                    return;
                }

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
                callFrame.setResult(finalUrl.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        patcher.patch(modelEmojiCustom, isUsable, new Class[0], new PinePatchFn(callFrame -> callFrame.setResult(true)));
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }

}