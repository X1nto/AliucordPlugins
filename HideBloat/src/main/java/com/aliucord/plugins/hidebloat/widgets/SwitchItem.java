package com.aliucord.plugins.hidebloat.widgets;

import android.annotation.SuppressLint;
import android.content.Context;

import com.aliucord.Utils;
import com.aliucord.api.SettingsAPI;
import com.aliucord.utils.DimenUtils;
import com.aliucord.plugins.hidebloat.util.Const;
import com.discord.utilities.color.ColorCompat;
import com.discord.views.CheckedSetting;
import com.google.android.material.card.MaterialCardView;
import com.lytefast.flexinput.R;

@SuppressLint("ViewConstructor")
public class SwitchItem extends MaterialCardView {

    public final String viewName;

    public SwitchItem(Context context, SettingsAPI settingsAPI, String key, String viewName) {
        super(context);

        this.viewName = viewName;

        setRadius(DimenUtils.getDefaultCardRadius());
        setCardBackgroundColor(ColorCompat.getThemedColor(context, R.b.colorBackgroundSecondary));
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        CheckedSetting setting = Utils.createCheckedSetting(context, CheckedSetting.ViewType.SWITCH, "Hide " + viewName, null);
        setting.setChecked(settingsAPI.getBool(key, Const.PREFERENCE_DEFAULT_VALUE));
        setting.setOnCheckedListener(v -> settingsAPI.setBool(key, v));

        addView(setting);
    }


}
