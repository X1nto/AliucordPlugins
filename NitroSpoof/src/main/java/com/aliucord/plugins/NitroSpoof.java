package com.aliucord.plugins;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aliucord.PluginManager;
import com.aliucord.Utils;
import com.aliucord.api.SettingsAPI;
import com.aliucord.entities.Plugin;
import com.aliucord.widgets.LinearLayout;
import com.discord.app.AppBottomSheet;
import com.discord.utilities.color.ColorCompat;
import com.discord.views.CheckedSetting;
import com.discord.views.RadioManager;
import com.lytefast.flexinput.R$b;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NitroSpoof extends Plugin {

    private static final String className = "com.discord.models.domain.emoji.ModelEmojiCustom";
    private static final String getChatInputText = "getChatInputText";
    private static final String isUsable = "isUsable";

    public static Map<String, List<String>> getClassesToPatch() {
        Map<String, List<String>> map = new HashMap<>();
        map.put(className, Arrays.asList(getChatInputText, isUsable));
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
        manifest.description = "Use all emotes in any server without Nitro.";
        manifest.version = "1.0.1";
        manifest.updateUrl = "https://raw.githubusercontent.com/X1nto/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        patcher.patch(className, getChatInputText, (_this, args, ret) -> {
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
        patcher.patch(className, isUsable, (_this, args, ret) -> true);
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }

    public static class EmoteSizeSettings extends AppBottomSheet {

        @Override
        public int getContentViewResId() {
            return 0;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Context context = inflater.getContext();
            LinearLayout layout = new LinearLayout(context);
            layout.setBackgroundColor(ColorCompat.getThemedColor(context, R$b.colorBackgroundPrimary));

            List<CheckedSetting> radios = Arrays.asList(
                    Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "Big (64x64)", null),
                    Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "Medium (40x40)", null),
                    Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "Small (32x32)", null)
            );

            RadioManager radioManager = new RadioManager(radios);
            SettingsAPI sets = PluginManager.plugins.get("NitroSpoof").sets;
            EmoteSize emoteSize = EmoteSize.valueOf(sets.getString("emotesize", EmoteSize.FORTY.name()));

            int radioSize = radios.size();
            for (int i = 0; i < radioSize; i++) {
                int finalSize = i;
                CheckedSetting radio = radios.get(i);
                radio.e(e -> {
                    sets.setString("emotesize", EmoteSize.values()[finalSize].name());
                    radioManager.a(radio);
                });
                layout.addView(radio);
                if (i == emoteSize.ordinal()) radioManager.a(radio);
            }

            return layout;
        }
    }

    public enum EmoteSize {

        SIXTY_FOUR,
        FORTY,
        THIRTY_TWO

    }

}