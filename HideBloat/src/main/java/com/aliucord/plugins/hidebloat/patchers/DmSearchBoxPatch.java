package com.aliucord.plugins.hidebloat.patchers;

import android.view.View;
import android.view.ViewGroup;

import com.aliucord.plugins.hidebloat.Const;
import com.aliucord.plugins.hidebloat.patchers.base.BasePatcher;
import com.discord.databinding.WidgetChannelsListBinding;
import com.discord.widgets.channels.list.WidgetChannelListModel;

import java.lang.reflect.Method;

import top.canyie.pine.Pine;

public class DmSearchBoxPatch extends BasePatcher {

    private static final String WIDGET_CHANNELS_LIST = "com.discord.widgets.channels.list.WidgetChannelsList";
    private static final String CONFIGURE_UI = "configureUI";

    public DmSearchBoxPatch() {
        super(Const.Key.SEARCH_BOX_KEY, Const.ViewName.SEARCH_BOX_NAME, WIDGET_CHANNELS_LIST, CONFIGURE_UI, new Class[] { WidgetChannelListModel.class });
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) throws Exception {
        Object _this = callFrame.thisObject;
        Method bindingMethod = _this.getClass().getDeclaredMethod("getBinding");
        bindingMethod.setAccessible(true);

        WidgetChannelsListBinding binding = (WidgetChannelsListBinding) bindingMethod.invoke(_this);
        assert binding != null;
        ((ViewGroup) binding.j.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        callFrame.setResult(callFrame.getResult());
    }
}
