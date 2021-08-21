package com.aliucord.plugins.layoutcontroller.patchers;

import com.aliucord.plugins.layoutcontroller.patchers.base.BasePatcher;
import com.aliucord.plugins.layoutcontroller.util.Const;
import com.aliucord.plugins.layoutcontroller.util.Util;
import com.discord.databinding.WidgetUserSheetBinding;
import com.discord.widgets.user.usersheet.WidgetUserSheet;
import com.discord.widgets.user.usersheet.WidgetUserSheetViewModel;

import top.canyie.pine.Pine;

public class CallButtonsPatch extends BasePatcher {

    public CallButtonsPatch() throws Exception {
        super(Const.Key.CALL_BUTTONS_KEY, Const.ViewName.CALL_BUTTONS_NAME, WidgetUserSheet.class.getDeclaredMethod("configureProfileActionButtons", WidgetUserSheetViewModel.ViewState.Loaded.class));
    }

    @Override
    public void patchBody(Pine.CallFrame callFrame) {
        var _this = (WidgetUserSheet) callFrame.thisObject;

        try {
            var _binding = _this.getClass().getDeclaredMethod("getBinding");
            _binding.setAccessible(true);
            var binding = (WidgetUserSheetBinding) _binding.invoke(_this);

            if (binding == null) return;

            Util.hideViewCompletely(binding.i); //Voice call button
            Util.hideViewCompletely(binding.K); //Video call button
        } catch (Throwable ignored) {}

        callFrame.setResult(callFrame.getResult());
    }
}
