package com.aliucord.plugins.layoutcontroller.patchers;

import android.view.View;
import android.view.ViewGroup;

import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher;
import com.aliucord.plugins.layoutcontroller.util.Const;
import com.discord.databinding.WidgetChannelsListBinding;
import com.discord.widgets.channels.list.WidgetChannelListModel;
import com.discord.widgets.channels.list.WidgetChannelsList;

import top.canyie.pine.Pine;

public class DmSearchBoxPatch extends BasePatcher {

    public DmSearchBoxPatch() throws Exception {
        super(Const.Key.SEARCH_BOX_KEY, Const.Description.SEARCH_BOX_DESCRIPTION, WidgetChannelsList.class.getDeclaredMethod("configureUI", WidgetChannelListModel.class));
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
