package com.aliucord.plugins.layoutcontroller.widgets;

import android.annotation.SuppressLint;
import android.content.Context;

import com.aliucord.Utils;
import com.aliucord.api.SettingsAPI;
import com.aliucord.utils.DimenUtils;
import com.aliucord.plugins.layoutcontroller.util.Const;
import com.discord.utilities.color.ColorCompat;
import com.discord.views.CheckedSetting;
import com.google.android.material.card.MaterialCardView;
import com.lytefast.flexinput.R;

@SuppressLint("ViewConstructor")
public class SwitchItem extends MaterialCardView {

    public final String description;

    public SwitchItem(Context context, SettingsAPI settingsAPI, String key, String description) {
        super(context);

        this.description = description;

        setRadius(DimenUtils.getDefaultCardRadius());
        setCardBackgroundColor(ColorCompat.getThemedColor(context, R.b.colorBackgroundSecondary));
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        CheckedSetting setting = Utils.createCheckedSetting(context, CheckedSetting.ViewType.SWITCH, description, null);
        setting.setChecked(settingsAPI.getBool(key, Const.PREFERENCE_DEFAULT_VALUE));
        setting.setOnCheckedListener(v -> settingsAPI.setBool(key, v));

        addView(setting);
    }


}
