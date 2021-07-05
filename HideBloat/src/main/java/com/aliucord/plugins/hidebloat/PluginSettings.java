package com.aliucord.plugins.hidebloat;

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
import com.aliucord.widgets.LinearLayout;
import com.discord.app.AppBottomSheet;
import com.discord.utilities.color.ColorCompat;
import com.discord.views.CheckedSetting;
import com.lytefast.flexinput.R$b;

import java.util.Objects;

public class PluginSettings extends AppBottomSheet {

    @Override
    public int getContentViewResId() {
        return 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = inflater.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(ColorCompat.getThemedColor(context, R$b.colorBackgroundPrimary));

        SettingsAPI sets = Objects.requireNonNull(PluginManager.plugins.get("HideBloat")).sets;

        layout.addView(createSwitch(context, sets, Const.CHANNELS_INVITE_BUTTON_KEY, "Invite button in channels list"));
        layout.addView(createSwitch(context, sets, Const.MEMBERS_INVITE_BUTTON_KEY, "Invite button in members list"));
        layout.addView(createSwitch(context, sets, Const.GIFT_BUTTON_KEY, "Nitro Gift button"));
        layout.addView(createSwitch(context, sets, Const.SEARCH_BOX_KEY, "Search box in DM list"));
        return layout;
    }

    private CheckedSetting createSwitch(Context context, SettingsAPI sets, String key, String viewName) {
        CheckedSetting setting = Utils.createCheckedSetting(context, CheckedSetting.ViewType.SWITCH, "Hide " + viewName, null);
        setting.setChecked(sets.getBool(key, true));
        setting.setOnCheckedListener(v -> sets.setBool(key, v));
        return setting;
    }
}
