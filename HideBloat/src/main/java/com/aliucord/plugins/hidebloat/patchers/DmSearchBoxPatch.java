package com.aliucord.plugins.hidebloat.patchers;

import android.view.View;
import android.view.ViewGroup;

import com.aliucord.plugins.hidebloat.util.Const;
import com.aliucord.plugins.hidebloat.patchers.base.BasePatcher;
import com.discord.databinding.WidgetChannelsListBinding;
import com.discord.widgets.channels.list.WidgetChannelListModel;
import com.discord.widgets.channels.list.WidgetChannelsList;

import java.lang.reflect.Method;

import top.canyie.pine.Pine;

public class DmSearchBoxPatch extends BasePatcher {

    public DmSearchBoxPatch() throws Exception{
        super(Const.Key.SEARCH_BOX_KEY, Const.ViewName.SEARCH_BOX_NAME, WidgetChannelsList.class.getDeclaredMethod("configureUI", WidgetChannelListModel.class));
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) throws Exception {
        var _this = callFrame.thisObject;
        var _binding = _this.getClass().getDeclaredMethod("getBinding");
        _binding.setAccessible(true);

        var binding = (WidgetChannelsListBinding) _binding.invoke(_this);

        if (binding == null) return;

        ((ViewGroup) binding.j.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        callFrame.setResult(callFrame.getResult());
    }
}
