package com.aliucord.plugins.nitrospoof;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aliucord.Utils;
import com.aliucord.api.SettingsAPI;
import com.aliucord.widgets.LinearLayout;
import com.discord.app.AppBottomSheet;
import com.discord.utilities.color.ColorCompat;
import com.discord.views.CheckedSetting;
import com.discord.views.RadioManager;
import com.lytefast.flexinput.R;

import java.util.Arrays;
import java.util.List;

public class PluginSettings extends AppBottomSheet {

    private final SettingsAPI settingsAPI;

    public PluginSettings(SettingsAPI settingsAPI) {
        this.settingsAPI = settingsAPI;
    }

    @Override
    public int getContentViewResId() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = inflater.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(ColorCompat.getThemedColor(context, R.b.colorBackgroundPrimary));

        List<CheckedSetting> radios = Arrays.asList(
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "Big (64x64)", null),
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "Medium (40x40)", null),
                Utils.createCheckedSetting(context, CheckedSetting.ViewType.RADIO, "Small (32x32)", null)
        );

        RadioManager radioManager = new RadioManager(radios);
        EmoteSize emoteSize = EmoteSize.valueOf(settingsAPI.getString("emotesize", EmoteSize.FORTY.name()));

        int radioSize = radios.size();
        for (int i = 0; i < radioSize; i++) {
            int finalSize = i;
            CheckedSetting radio = radios.get(i);
            radio.e(e -> {
                settingsAPI.setString("emotesize", EmoteSize.values()[finalSize].name());
                radioManager.a(radio);
            });
            layout.addView(radio);
            if (i == emoteSize.ordinal()) radioManager.a(radio);
        }

        return layout;
    }

}
